package sunnn.filehub.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import sunnn.filehub.entity.Share;
import sunnn.filehub.util.StatusCode;
import sunnn.filehub.util.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class ShareListResponse extends Response {

    List<ShareInfo> list;

    public ShareListResponse(StatusCode statusCode) {
        super(statusCode);
    }

    public ShareListResponse setList(List<Share> list) {
        if (list.isEmpty())
            return this;

        List<ShareInfo> res = new ArrayList<>();
        for (Share s : list) {
            ShareInfo info = new ShareInfo();

            info.id = s.getId();
            info.name = s.getName();
            info.totalSize = Utils.formatSize(s.getTotalSize());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            info.expireTime = formatter.format(s.getExpireTime());

            res.add(info);
        }

        this.list = res;
        return this;
    }
}

@Getter
@Setter
class ShareInfo {

    String id;

    String name;

    String totalSize;

    String expireTime;
}
