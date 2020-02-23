package sunnn.filehub.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class CommitListRequest {

    private int page;

    private String name;

    private String startDate;

    private String endDate;

    private String filter;

    public CommitListRequest setFilter(String filter) {
        if (filter.equals("0000000"))
            filter = "1111111";
        this.filter = filter;
        return this;
    }

    public boolean noFilter() {
        return name.isEmpty() && startDate.isEmpty() && endDate.isEmpty() && filter.isEmpty();
    }
}
