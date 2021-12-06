package ru.netology.moneytransferservice.model;

import java.util.Objects;

public class OperationIdentifier {
    protected final String operationId;

    public OperationIdentifier(String operationId) {
        this.operationId = operationId;
    }

    public String getOperationId() {
        return operationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperationIdentifier that = (OperationIdentifier) o;
        return operationId.equals(that.operationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operationId);
    }
}
