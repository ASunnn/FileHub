package sunnn.filehub.controller;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sunnn.filehub.dto.request.CreateShareRequest;
import sunnn.filehub.dto.response.Response;
import sunnn.filehub.exception.FileNotFoundException;
import sunnn.filehub.service.FileService;
import sunnn.filehub.service.ShareService;

import java.io.File;
import java.io.IOException;

@RestController
public class FileController {

    private final FileService fileService;

    private final ShareService shareService;

    @Autowired
    public FileController(FileService fileService, ShareService shareService) {
        this.fileService = fileService;
        this.shareService = shareService;
    }

    /**
     * 获取一次上传的信息
     *
     * 有密码给密码，没有密码直接打开
     * 返回名字、文件列表详情（名字、大小）、上传时间、有效期
     */
    @PostMapping("/files/{sequence}")
    public Response getFile(@PathVariable("sequence") long sequence, @RequestParam("key") String key) {
        return fileService.getCommitInfo(sequence);
    }

    /**
     * 下载文件
     *
     * 会先在session里记录有没在getFile里授权，有的话能下，没有的话不能下
     * 两段式———— /id/详细文件
     */
    @GetMapping("/files/download/{sequence}")
    public ResponseEntity download(@PathVariable("sequence") long sequence) throws FileNotFoundException, IOException {
        File file = fileService.downloadFiles(sequence);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment",
                new String(file.getName().getBytes(), "ISO-8859-1"));
        return new ResponseEntity<>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
    }

    @GetMapping("/files/download/{sequence}/{name}")
    public ResponseEntity download(@PathVariable("sequence") long sequence, @PathVariable("name") String name) throws FileNotFoundException, IOException {
        File file = fileService.downloadFile(sequence, name);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment",
                new String(file.getName().getBytes(), "ISO-8859-1"));
        return new ResponseEntity<>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
    }

    /**
     * 创建一个分享
     *
     * 选是否加密，有效期1-7天
     */
    @PostMapping("/files/share/{sequence}")
    @ResponseBody
    public Response share(@PathVariable("sequence") long sequence, @RequestBody CreateShareRequest request) {
        return shareService.createShare(sequence, request);
    }

    @PostMapping("/files/delete/{sequence}")
    public Response delete(@PathVariable("sequence") long sequence) {
        return fileService.deleteCommit(sequence);
    }
}
