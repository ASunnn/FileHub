package sunnn.filehub.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import sunnn.filehub.util.StatusCode;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class Response {

    private int code;

    private String msg;

    private String detail;

    public Response(StatusCode statusCode) {
        this.code = statusCode.getCode();
        this.msg = statusCode.name();
    }
}
