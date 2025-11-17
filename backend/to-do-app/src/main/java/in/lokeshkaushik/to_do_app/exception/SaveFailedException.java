package in.lokeshkaushik.to_do_app.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  // 500
public class SaveFailedException extends RuntimeException {
    public SaveFailedException(String message) {
        super(message);
    }
}
