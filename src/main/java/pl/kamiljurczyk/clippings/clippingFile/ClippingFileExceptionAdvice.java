package pl.kamiljurczyk.clippings.clippingFile;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ClippingFileExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(ClippingFileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String projectNotFoundHandler(ClippingFileNotFoundException exception) {
        return exception.getMessage();
    }
}
