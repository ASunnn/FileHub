package sunnn.filehub.exception;

public class NoAccessException extends RuntimeException {
//  必须使用RuntimeException，
// 否则会被AOP框架转成UndeclaredThrowableException，
// GlobalExceptionResolver就捕获不到了

    public NoAccessException() {
    }

    public NoAccessException(String message) {
        super(message);
    }
}
