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
public class CreateShareResponse extends Response {

    private String url;

    private String key;

    public CreateShareResponse(StatusCode statusCode) {
        super(statusCode);
    }
}
