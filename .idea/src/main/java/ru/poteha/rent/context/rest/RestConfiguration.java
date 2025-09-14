package ru.poteha.rent.context.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import ru.poteha.rent.context.rest.interceptor.MinioClientHttpRequestInterceptor;
import ru.poteha.rent.context.rest.properties.MinioProperties;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RestConfiguration {

    @Bean
    MinioClient minioClient(MinioProperties prop) {
        var restClient = RestClient.builder()
                .requestInterceptor(new MinioClientHttpRequestInterceptor(prop))
                .baseUrl(prop.getBaseUri())
                .build();
        return builderFor(restClient).createClient(MinioClient.class);
    }

    //
    //
    //
    private HttpServiceProxyFactory builderFor(RestClientAdapter adapter) {
        return HttpServiceProxyFactory.builderFor(adapter).build();
    }

    private HttpServiceProxyFactory builderFor(RestClient restClient) {
        return builderFor(RestClientAdapter.create(restClient));
    }
}
