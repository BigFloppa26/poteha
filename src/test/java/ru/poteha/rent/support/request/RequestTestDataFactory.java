package ru.poteha.rent.support.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.poteha.rent.support.request.impl.jpa.SupportRequest;
import ru.poteha.rent.support.request.model.SupportRequestCreate;
import ru.poteha.rent.support.request.model.SupportRequestStatus;
import ru.poteha.rent.user.UserTestDataFactory;

import java.util.UUID;

public class RequestTestDataFactory {

    public static byte[] bytesCorrectSupportRequestCreate(boolean message) throws JsonProcessingException {
        var supportRequestCreate = new SupportRequestCreate();
        supportRequestCreate.setTitle("Title");
        if (message) {
            supportRequestCreate.setMessage("Message");
        } else supportRequestCreate.setMessage("");
        return new ObjectMapper().writeValueAsBytes(supportRequestCreate);
    }

    public static SupportRequest supportRequest() {
        var supportRequest = new SupportRequest();
        supportRequest.setTitle("Title");
        supportRequest.setDescription("Description");
        supportRequest.setStatus(SupportRequestStatus.OPEN);
        supportRequest.setAuthor(UserTestDataFactory.rentUser());
        supportRequest.setModified(null);
        supportRequest.setCreated(null);
        supportRequest.setId(UUID.randomUUID());
        return supportRequest;
    }
}
