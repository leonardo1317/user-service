package io.github.leonardofrs.user_service.infrastructure.entrypoint.rest.controller.contract;

public record ApiError(String title,
                       String message,
                       int status,
                       String path) {

}
