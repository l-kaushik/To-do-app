package in.lokeshkaushik.to_do_app.exception;

public class NoChangesDetectedException extends RuntimeException {
    public NoChangesDetectedException(String message) {
        super(message);
    }
}
