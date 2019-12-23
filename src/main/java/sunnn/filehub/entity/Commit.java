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
public class Commit {

    private long sequence;

    private String name;

    private String key;

    private Files[] files;

    private long totalSize;

    private Timestamp uploadTime;

    private int expire;
}
