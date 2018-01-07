package ru.rooxtest.partnersmappings.controller.support;

/**
 * Исключение бросаем когда абонент пытается прочитать/изменить не свои привязки
 */
public class WrongCustomerException extends RuntimeException {
    public WrongCustomerException() {
    }

    public WrongCustomerException(String message) {
        super(message);
    }

    public WrongCustomerException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongCustomerException(Throwable cause) {
        super(cause);
    }

    public WrongCustomerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
