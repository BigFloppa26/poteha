package ru.poteha.rent.context.rest.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import ru.poteha.rent.context.rest.properties.MinioProperties;

import java.io.IOException;
import java.net.URI;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.springframework.http.HttpHeaders.*;

@Slf4j
@RequiredArgsConstructor
public class MinioClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
    static final DateTimeFormatter ISO_8601 = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'", Locale.US);
    static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.US);

    static final String X_AMZ_CONTENT_SHA256 = "X-Amz-Content-SHA256";
    static final String CONTENT_MD5 = "Content-MD5";
    static final String X_AMZ_DATE = "X-Amz-Date";

    private final MinioProperties prop;

    @NonNull
    @Override
    public ClientHttpResponse intercept(@NonNull HttpRequest request,
                                        @NonNull byte[] body,
                                        @NonNull ClientHttpRequestExecution execution) throws IOException {
        var currentDate = ZonedDateTime.now(ZoneOffset.systemDefault());

        setAuthorization(request, currentDate, body);
        return execution.execute(request, body);
    }

    void setAuthorization(HttpRequest request, ZonedDateTime date, byte[] body) {
        var scope = date.format(ISO_DATE) + "/" + prop.getRegion() + "/s3/aws4_request";
        var headers = new TreeMap<String, String>(Comparator.naturalOrder()) {{
            put(X_AMZ_CONTENT_SHA256, sha256Hex(body));
            put(CONTENT_MD5, base64Encode(md5(body)));
            put(X_AMZ_DATE, date.format(ISO_8601));
            put(HOST, request.getURI().getHost());
        }};

        var signedHeaders = new StringBuilder();
        var canonicalHeaders = new StringBuilder();
        headers.forEach((key, value) -> {
            canonicalHeaders.append(key).append(":").append(value.trim()).append("\n");
            signedHeaders.append(key).append(";");
            request.getHeaders().set(key, value);
        });

        var signature = hexEncode(hmacSha256(
                //  Формирование ключа
                signatureKey(date.format(ISO_DATE)),
                //  Формирование строки которую будем подписывать
                signatureStr(request, canonicalHeaders, signedHeaders, date.format(ISO_8601), scope, body)
        ));

        request.getHeaders().set(AUTHORIZATION, "%s Credential=%s/%s, SignedHeaders=%s, Signature=%s".formatted(
                prop.getAlgorithm(), prop.getAccessKey(), scope, signedHeaders, signature
        ));
    }

    byte[] signatureKey(String isoDate) {
        var kSecret = "AWS4%s".formatted(prop.getSecretKey()).getBytes();
        var kDate = hmacSha256(kSecret, isoDate);
        var kRegion = hmacSha256(kDate, prop.getRegion());
        var kService = hmacSha256(kRegion, "s3");
        return hmacSha256(kService, "aws4_request");
    }

    String signatureStr(HttpRequest rq, StringBuilder cH, StringBuilder sH, String date, String scope, byte[] body) {
        return createSignatureToString(date, scope, createCanonicalRequest(rq, cH, sH, body));
    }

    String createSignatureToString(String date, String scope, String canonicalRq) {
        return "%s\n%s\n%s\n%s".formatted(prop.getAlgorithm(), date, scope, sha256Hex(canonicalRq.getBytes()));
    }

    String createCanonicalRequest(String method, String path, String cH, String sH, byte[] body) {
        return "%s\n%s\n\n%s\n%s\n%s".formatted(method, (path != null ? path : "/"), cH, sH, sha256Hex(body));
    }

    String createCanonicalRequest(HttpRequest request, StringBuilder cH, StringBuilder sH, byte[] body) {
        return createCanonicalRequest(request.getMethod(), request.getURI(), cH, sH, body);
    }

    String createCanonicalRequest(HttpMethod method, URI uri, StringBuilder cH, StringBuilder sH, byte[] body) {
        return createCanonicalRequest(method.name(), uri.getPath(), cH.toString(), sH.toString(), body);
    }

    //
    //
    //

    private byte[] md5(byte[] data) {
        return DigestUtils.md5(data);
    }

    private String sha256Hex(byte[] data) {
        return DigestUtils.sha256Hex(data);
    }

    private String hexEncode(byte[] data) {
        return Hex.encodeHexString(data);
    }

    private String base64Encode(byte[] data) {
        return Base64.encodeBase64String(data);
    }

    private byte[] hmacSha256(byte[] key, String data) {
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_256, key).hmac(data);
    }

}