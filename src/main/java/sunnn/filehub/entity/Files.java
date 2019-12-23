package sunnn.filehub.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class Files {

    private long sequence;

    private String name;

    private long size;
}
