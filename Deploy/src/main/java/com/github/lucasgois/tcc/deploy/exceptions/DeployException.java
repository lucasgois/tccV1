package com.github.lucasgois.tcc.deploy.exceptions;

public class DeployException extends RuntimeException {

    public DeployException() {
    }

    public DeployException(String message) {
        super(message);
    }

    public DeployException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeployException(Throwable cause) {
        super(cause);
    }

    public DeployException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}