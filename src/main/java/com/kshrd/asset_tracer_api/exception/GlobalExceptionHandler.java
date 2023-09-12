package com.kshrd.asset_tracer_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundExceptionHandler.class)
    ProblemDetail handleNotFoundException(NotFoundExceptionHandler e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Not Found Exception");
        problemDetail.setProperty("timestamp ", Instant.now());
        problemDetail.setType(URI.create("localhost:8080/errors/not-found"));
        return problemDetail;
    }

    @ExceptionHandler(UserDuplicateExceptionHandler.class)
    ProblemDetail handleUserDuplicateException(UserDuplicateExceptionHandler e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("User Duplicate Exception");
        problemDetail.setProperty("timestamp ", Instant.now());
        problemDetail.setType(URI.create("localhost:8080/errors/user-duplicate"));
        return problemDetail;
    }

    @ExceptionHandler(FieldEmptyExceptionHandler.class)
    ProblemDetail handleFieldEmptyExceptionHandler(FieldEmptyExceptionHandler e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("Filed Is Empty Exception");
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setType(URI.create("localhost:8080/errors/bad-request"));
        return problemDetail;
    }

    @ExceptionHandler(FieldBlankExceptionHandler.class)
    ProblemDetail handleFieldBlankExceptionHandler(FieldBlankExceptionHandler e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("Filed Is Blank Exception");
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setType(URI.create("localhost:8080/errors/bad-request"));
        return problemDetail;
    }

    @ExceptionHandler(NotValidValueExceptionHandler.class)
    ProblemDetail handleNotValidValueExceptionHandler(NotValidValueExceptionHandler e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("Not Valid Value Exception");
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setType(URI.create("localhost:8080/errors/bad-request"));
        return problemDetail;
    }

    @ExceptionHandler(EmptyDataExceptionHandler.class)
    ProblemDetail handleEmptyDataExceptionHandler(EmptyDataExceptionHandler e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("Empty Data Exception");
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setType(URI.create("localhost:8080/errors/bad-request"));
        return problemDetail;
    }

    @ExceptionHandler(InvalidPasswordExceptionHandler.class)
    ProblemDetail handleInvalidPasswordExceptionHandler(InvalidPasswordExceptionHandler e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("Invalid password");
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setType(URI.create("localhost:8080/errors/bad-request"));
        return problemDetail;
    }

    @ExceptionHandler(IsEnableExceptionHandler.class)
    ProblemDetail handleIsEnableExceptionHandler(IsEnableExceptionHandler e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("User is disabled");
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setType(URI.create("localhost:8080/errors/bad-request"));
        return problemDetail;
    }

    @ExceptionHandler(IsAccountNonExpiredExceptionHandler.class)
    ProblemDetail handleIsAccountNonExpiredExceptionHandler(IsAccountNonExpiredExceptionHandler e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("User account has expired");
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setType(URI.create("localhost:8080/errors/bad-request"));
        return problemDetail;
    }

    @ExceptionHandler(IsAccountNonLockedExceptionHandler.class)
    ProblemDetail handleIsAccountNonLockedExceptionHandler(IsAccountNonLockedExceptionHandler e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("User account is locked");
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setType(URI.create("localhost:8080/errors/bad-request"));
        return problemDetail;
    }

    @ExceptionHandler(IsCredentialsNonExpiredExceptionHandler.class)
    ProblemDetail handleIsCredentialsNonExpiredExceptionHandler(IsCredentialsNonExpiredExceptionHandler e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("User credentials have expired");
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setType(URI.create("localhost:8080/errors/bad-request"));
        return problemDetail;
    }
    @ExceptionHandler(BadCredentialsExceptionHandler.class)
    ProblemDetail handleBadCredentialsExceptionHandler(BadCredentialsExceptionHandler e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("User account is locked");
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setType(URI.create("localhost:8080/errors/bad-request"));
        return problemDetail;
    }

    @ExceptionHandler(PasswordNotMatchExceptionHandler.class)
    ProblemDetail handlePasswordNotMatchExceptionHandler(PasswordNotMatchExceptionHandler e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("Password not correct");
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setType(URI.create("localhost:8080/errors/bad-request"));
        return problemDetail;
    }

    @ExceptionHandler(ForbiddenExceptionHandler.class)
    ProblemDetail handleForbiddenExceptionHandler(ForbiddenExceptionHandler e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage());
        problemDetail.setTitle("403 Forbidden");
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setType(URI.create("localhost:8080/errors/forbiddden"));
        return problemDetail;
    }

    @ExceptionHandler(UnauthorizedExceptionHandler.class)
    ProblemDetail handleUnauthenticatedExceptionHandler(UnauthorizedExceptionHandler e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
        problemDetail.setTitle("User Not Authorized");
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setType(URI.create("localhost:8080/errors/unauthorized"));
        return problemDetail;
    }

    @ExceptionHandler(DataDuplicateExceptionHandler.class)
    ProblemDetail handleDataDuplicateExceptionHandler(DataDuplicateExceptionHandler e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("Data Duplication");
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setType(URI.create("localhost:8080/errors/bad-request"));
        return problemDetail;
    }

    @ExceptionHandler(OutOfAmountExceptionHandler.class)
    ProblemDetail handleOutOfAmountExceptionHandler(OutOfAmountExceptionHandler e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("Out Of Amount");
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setType(URI.create("localhost:8080/errors/bad-request"));
        return problemDetail;
    }
}
