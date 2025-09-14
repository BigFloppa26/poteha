package ru.poteha.rent.confirm;

import ru.poteha.rent.confirm.model.ConfirmCode;
import ru.poteha.rent.confirm.model.ConfirmCreate;
import ru.poteha.rent.confirm.model.ConfirmCypher;
import ru.poteha.rent.confirm.model.ConfirmShort;

import java.util.UUID;
import java.util.function.Supplier;

public interface ConfirmService {
    <T> T getPayload(UUID id, String md5Code, Class<T> cls);

    <T> T getPayload(ConfirmCypher code, Class<T> cls);

    ConfirmShort resend(UUID id);

    ConfirmShort consume(ConfirmCode model);

    ConfirmShort consume(UUID id, String code);

    ConfirmShort generateAndSend(String feature, Supplier<ConfirmCreate> supplier);

    ConfirmShort generateAndSend(boolean feature, Supplier<ConfirmCreate> supplier);
}
