package com.github.morihara.transactional.sample.exception;

public class TransactionalRuntimeException extends RuntimeException {

    private static final long serialVersionUID = -8657246884646848085L;

    public TransactionalRuntimeException(Throwable e) {
        super(e);
    }

    public TransactionalRuntimeException(String message) {
        super(message);
    }

    public TransactionalRuntimeException(String message, Throwable e) {
        super(message, e);
    }

    public TransactionalRuntimeException() {}

}
