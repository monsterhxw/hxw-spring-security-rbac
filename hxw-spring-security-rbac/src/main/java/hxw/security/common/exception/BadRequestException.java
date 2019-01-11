package hxw.security.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class BadRequestException extends RuntimeException {

    @Getter
    private Integer status = HttpStatus.BAD_REQUEST.value();

    public BadRequestException(String msg) {
        super(msg);
    }

    public BadRequestException(HttpStatus httpStatus, String msg) {
        super(msg);
        this.status = httpStatus.value();
    }

}
