package sunnn.filehub.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import sunnn.filehub.entity.Files;
import sunnn.filehub.util.StatusCode;
import sunnn.filehub.util.Utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class ShareInfoResponse extends Response {

    private String id;

    private String name;

    private String totalSize;

    private String expireTime;

    private List<FileInfo> files;

    public ShareInfoResponse(StatusCode statusCode) {
        super(statusCode);
    }

    public ShareInfoResponse setFiles(List<Files> list) {
        if (list.isEmpty())
            return this;

        List<FileInfo> res = new ArrayList<>();
        for (Files f : list) {
            FileInfo info = new FileInfo();

            info.id = f.getId();
            info.name = f.getName();
            info.size = Utils.formatSize(f.getSize());

            res.add(info);
        }

        this.files = res;
        return this;
    }

    public ShareInfoResponse setTotalSize(long totalSize) {
        this.totalSize = Utils.formatSize(totalSize);
        return this;
    }

    public ShareInfoResponse setExpireTime(Timestamp expireTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        this.expireTime = formatter.format(expireTime);
        return this;
    }
}
