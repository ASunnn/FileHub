package sunnn.filehub.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;
import sunnn.filehub.entity.Files;

import java.sql.Timestamp;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class CommitFilesRequest {

    private String name;

    private String key;

    private int expire;

    private MultipartFile[] files;
}
