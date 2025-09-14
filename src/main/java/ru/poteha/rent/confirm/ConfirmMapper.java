package ru.poteha.rent.confirm;

import ru.poteha.rent.confirm.impl.jpa.Confirm;
import ru.poteha.rent.confirm.model.ConfirmShort;

public interface ConfirmMapper {
    ConfirmShort toShort(Confirm confirm);
}
