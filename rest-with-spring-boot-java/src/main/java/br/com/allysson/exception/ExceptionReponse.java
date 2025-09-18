package br.com.allysson.exception;

import java.util.Date;

public record ExceptionReponse(Date timestamp, String message, String details) {}
