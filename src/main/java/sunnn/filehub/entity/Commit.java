package sunnn.filehub.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class Commit {

    private long sequence;

    private String name;

    private boolean encrypt;

    private String key;

    private int expire;

    private List<Files> files;

    private long totalSize;

    private List<Type> types;

    private Timestamp uploadTime;
}
