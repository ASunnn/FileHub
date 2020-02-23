package sunnn.filehub.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import sunnn.filehub.dto.response.Response;
import sunnn.filehub.exception.FileNotFoundException;
import sunnn.filehub.exception.NoAccessException;
import sunnn.filehub.util.StatusCode;

@ControllerAdvice
public class GlobalExceptionResolver {

    @ExceptionHandler(value = Exception.class)
//    @ResponseBody
    public String errorResolver(Exception e) {
        e.printStackTrace();
//        return new Response(StatusCode.ERROR).setDetail("发生了预期之外的错误");
        return "error";
    }

    @ExceptionHandler(value = NoAccessException.class)
    @ResponseBody
    public Response noAccessResolver(Exception e) {
        return new Response(StatusCode.NO_ACCESS).setDetail("无权限 or 已达访问上限");
    }

    @ExceptionHandler(value = FileNotFoundException.class)
    public String fileNotFoundResolver(Exception e) {
        return "reject";
    }
}
