package sunnn.filehub.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class Share extends Commit {

    private String id;

    private long sequence;

    private String key;

    private Timestamp expireTime;
}
