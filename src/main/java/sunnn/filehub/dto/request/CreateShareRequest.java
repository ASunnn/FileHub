package sunnn.filehub.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class CreateShareRequest {

    private int expire;

    private boolean encrypt;
}
