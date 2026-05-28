package betcore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ProblemDetail problem(HttpStatus status, String title, String detail) {
        ProblemDetail p = ProblemDetail.forStatusAndDetail(status, detail);
        p.setTitle(title);
        p.setProperty("timestamp", Instant.now());
        return p;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFound(ResourceNotFoundException e) {
        return problem(HttpStatus.NOT_FOUND, e.getMessage(), "Resource not found");
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ProblemDetail handleDuplicate(DuplicateResourceException e) {
        return problem(HttpStatus.CONFLICT, e.getMessage(), "Duplicate resource");
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ProblemDetail handleInsufficientBalance(InsufficientBalanceException e) {
        return problem(HttpStatus.BAD_REQUEST, e.getMessage(), "Insufficient balance");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleBadArgument(IllegalArgumentException e) {
        return problem(HttpStatus.BAD_REQUEST, e.getMessage(), "Bad argument");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (existing, replacement) -> existing
                ));
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Validation Failed"
        );
        problem.setTitle("Validation Failed");
        problem.setProperty("errors", errors);
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
}
