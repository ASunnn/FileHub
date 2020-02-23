package sunnn.filehub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sunnn.filehub.dao.CommitDao;
import sunnn.filehub.dao.FilesDao;
import sunnn.filehub.dao.ShareDao;
import sunnn.filehub.dto.CheckShareResponse;
import sunnn.filehub.dto.request.CreateShareRequest;
import sunnn.filehub.dto.response.CreateShareResponse;
import sunnn.filehub.dto.response.Response;
import sunnn.filehub.dto.response.ShareInfoResponse;
import sunnn.filehub.dto.response.ShareListResponse;
import sunnn.filehub.entity.Files;
import sunnn.filehub.entity.Share;
import sunnn.filehub.exception.FileNotFoundException;
import sunnn.filehub.util.FileHubProperties;
import sunnn.filehub.util.StatusCode;
import sunnn.filehub.util.Utils;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class ShareService {

    private final FileService fileService;

    @Resource
    private CommitDao commitDao;

    @Resource
    private ShareDao shareDao;

    @Resource
    private FilesDao filesDao;

    @Autowired
    public ShareService(FileService fileService) {
        this.fileService = fileService;
    }

    public Response createShare(long sequence, CreateShareRequest request) {
        if (commitDao.find(sequence) == null)
            return new Response(StatusCode.ILLEGAL_REQUEST).setDetail("无效的seq");

        int expire = request.getExpire();
        if (expire < 1 || expire > 168)
            return new Response(StatusCode.ILLEGAL_REQUEST).setDetail("有效期只能1-7天");

        String id = generateShareId();
        String key = generateShareKey(request.isEncrypt());
        long expireTime = Utils.now() + (expire * 3600 * 1000);

        Share ns = new Share()
                .setId(id)
                .setSequence(sequence)
                .setKey(key)
                .setExpireTime(new Timestamp(expireTime));
        shareDao.insert(ns);

        String url = FileHubProperties.path + "/s/" + id;
        return  new CreateShareResponse(StatusCode.OJBK)
                .setUrl(url)
                .setKey(key);
    }

    public Response checkShare(String id) {
        Share s = shareDao.find(id);
        if (s == null)
            return new Response(StatusCode.ILLEGAL_REQUEST).setDetail("Share不存在或已过期");

        return new CheckShareResponse(StatusCode.OJBK).setEncrypt(!s.getKey().isEmpty());
    }

    public Response getShareList() {
        List<Share> list = shareDao.findAll();

        return new ShareListResponse(StatusCode.OJBK).setList(list);
    }

    public Response getShareInfo(String id) {
        Share s = shareDao.find(id);
        if (s == null)
            return new Response(StatusCode.ILLEGAL_REQUEST).setDetail("无效的id");
        List<Files> files = filesDao.findAllBySeq(s.getSequence());

        return new ShareInfoResponse(StatusCode.OJBK)
                .setId(s.getId())
                .setName(s.getName())
                .setTotalSize(s.getTotalSize())
                .setExpireTime(s.getExpireTime())
                .setFiles(files);
    }

    public File downloadFiles(String id) throws FileNotFoundException, IOException {
        Share s = shareDao.find(id);
        if (s == null)
            throw new FileNotFoundException();

        return fileService.downloadFiles(s.getSequence());
    }

    public File downloadFile(String id, String name) throws FileNotFoundException {
        Share s = shareDao.find(id);
        if (s == null)
            throw new FileNotFoundException();

        return fileService.downloadFile(s.getSequence(), name);
    }

    public synchronized void clearShare() {
        List<Share> shares = shareDao.findAll();

        for (Share s : shares) {
            long expireTime = s.getExpireTime().getTime();
            if (Utils.now() > expireTime) {
                shareDao.delete(s.getId());
            }
        }
    }

    private String generateShareId() {
        String[] src = UUID.randomUUID().toString().split("-");
        return src[0] + src[4];
    }

    private String generateShareKey(boolean encrypt) {
        if (!encrypt)
            return "";

        String[] src = UUID.randomUUID().toString().split("-");
        int index = new Random(System.currentTimeMillis()).nextInt(3);

        return src[index + 1];
    }
}
