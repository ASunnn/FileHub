package sunnn.filehub.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import sunnn.filehub.dto.response.Response;
import sunnn.filehub.util.StatusCode;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class CheckShareResponse extends Response {

    public CheckShareResponse(StatusCode statusCode) {
        super(statusCode);
    }

    private boolean encrypt;
}
