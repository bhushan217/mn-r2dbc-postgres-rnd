package com.rukbal.db.error.handler;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolationException;

import static io.micronaut.http.MediaType.APPLICATION_JSON;


@Produces(APPLICATION_JSON)
@Singleton
@Requires(classes = {ConstraintViolationException.class, ExceptionHandler.class})
public class ValidationExceptionHandler implements ExceptionHandler<ConstraintViolationException, HttpResponse> {

    @Override
    public HttpResponse handle(HttpRequest request, ConstraintViolationException exception) {
        return HttpResponse.ok(exception.getMessage());
    }
}