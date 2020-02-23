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
public class CommitInfoResponse extends Response {

    private String sequence;

    private String name;

    private int expire;

    private List<FileInfo> files;

    private String totalSize;

    private String uploadTime;

    public CommitInfoResponse(StatusCode statusCode) {
        super(statusCode);
    }

    public CommitInfoResponse setSequence(long sequence) {
        this.sequence = String.valueOf(sequence);
        return this;
    }

    public CommitInfoResponse setFiles(List<Files> list) {
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

    public CommitInfoResponse setTotalSize(long totalSize) {
        this.totalSize = Utils.formatSize(totalSize);
        return this;
    }

    public CommitInfoResponse setUploadTime(Timestamp uploadTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        this.uploadTime = formatter.format(uploadTime);
        return this;
    }
}

@Getter
@Setter
class FileInfo {

    String id;

    String name;

    String size;
}
