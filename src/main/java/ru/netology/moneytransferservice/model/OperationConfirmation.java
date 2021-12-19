package ru.netology.moneytransferservice.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

public class OperationConfirmation {
    @NotNull(message = "Operation ID is null")
    @Size(min = 1, message = "Operation ID is empty")
    @Pattern(regexp = "[0-9]+", message = "Operation ID contains not only numeric characters")
    protected final String operationId;

    @NotNull(message = "Confirmation code is null")
    @Size(min = 1, message = "Confirmation code is empty")
    @Pattern(regexp = "[0-9]+", message = "Confirmation code contains not only numeric characters")
    protected final String code;

    public OperationConfirmation(String operationId, String code) {
        this.operationId = operationId;
        this.code = code;
    }

    public String getOperationId() {
        return operationId;
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperationConfirmation that = (OperationConfirmation) o;
        return Objects.equals(operationId, that.operationId) && Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operationId, code);
    }
}
