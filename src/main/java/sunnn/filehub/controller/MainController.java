package sunnn.filehub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sunnn.filehub.dto.request.CommitListRequest;
import sunnn.filehub.dto.request.CommitFilesRequest;
import sunnn.filehub.dto.response.Response;
import sunnn.filehub.service.FileService;
import sunnn.filehub.service.ShareService;
import sunnn.filehub.util.StatusCode;

@RestController
public class MainController {

    private final FileService fileService;

    private final ShareService shareService;

    @Autowired
    public MainController(FileService fileService, ShareService shareService) {
        this.fileService = fileService;
        this.shareService = shareService;
    }

    /**
     * 上传文件
     *
     * 必须有文件
     * 必须有name
     * 密码可以选填
     * 一小时内，限制大于1G内容
     * 一小时内，限制大于128个文件的传输
     */
    @PostMapping("/upload")
    public Response upload(@RequestParam("file") MultipartFile[] files, @RequestParam("name") String name,
                           @RequestParam("key") String key, @RequestParam("expire") int expire) {
        // 参数检查
        if (files.length == 0)
            return new Response(StatusCode.ILLEGAL_REQUEST).setDetail("必须上传文件");
        if (name.trim().isEmpty() || name.length() > 32
                || key.length() > 32)
            return new Response(StatusCode.ILLEGAL_REQUEST).setDetail("不允许的上传信息");
        expire = expire > 0 ? expire : -1;

        // save
        return fileService.saveUploadFiles(
                new CommitFilesRequest()
                        .setName(name.trim())
                        .setKey(key.trim())
                        .setExpire(expire)
                        .setFiles(files));
    }

    /**
     * 获取上传列表
     *
     * @param page      分页
     * @param name      筛选选条件
     * @param startDate 起始上传日期
     * @param endDate   结束上传日期
     * @param filter    类型过滤
     */
    @GetMapping("/commits")
    public Response commits(@RequestParam("p") int page,
                            @RequestParam(value = "n", required = false, defaultValue = "") String name,
                            @RequestParam(value = "s", required = false, defaultValue = "") String startDate,
                            @RequestParam(value = "e", required = false, defaultValue = "") String endDate,
                            @RequestParam(value = "f", required = false, defaultValue = "") String filter) {
        if (page < 0)
            return new Response(StatusCode.ILLEGAL_REQUEST).setDetail("非法的分页请求");

        return fileService.getCommitList(
                new CommitListRequest()
                        .setPage(page)
                        .setName(name)
                        .setStartDate(startDate).setEndDate(endDate).setFilter(filter));
    }

    @GetMapping("/shares")
    public Response shareList() {
        return shareService.getShareList();
    }
}
