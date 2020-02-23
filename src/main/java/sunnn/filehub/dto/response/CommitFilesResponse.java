package sunnn.filehub.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import sunnn.filehub.entity.Commit;
import sunnn.filehub.util.StatusCode;
import sunnn.filehub.util.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class CommitFilesResponse extends Response {

    private int count;

    List<CommitInfo> list;

    public CommitFilesResponse(StatusCode statusCode) {
        super(statusCode);
    }

    public CommitFilesResponse setList(List<Commit> list) {
        if (list.isEmpty())
            return this;

        List<CommitInfo> res = new ArrayList<>();
        for (Commit c : list) {
            CommitInfo info = new CommitInfo();

            info.sequence = String.valueOf(c.getSequence());
            info.name = c.getName();
            info.encrypt = c.isEncrypt();
            info.totalSize = Utils.formatSize(c.getTotalSize());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            info.uploadTime = formatter.format(c.getUploadTime());

            res.add(info);
        }

        this.list = res;
        return this;
    }
}

@Getter
@Setter
class CommitInfo {

    String sequence;

    String name;

    boolean encrypt;

    String totalSize;

    String uploadTime;
}