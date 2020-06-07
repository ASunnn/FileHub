package sunnn.filehub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sunnn.filehub.dto.request.CreateShareRequest;
import sunnn.filehub.dto.response.Response;
import sunnn.filehub.exception.FileNotFoundException;
import sunnn.filehub.service.FileService;
import sunnn.filehub.service.ShareService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

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
    public void download(@PathVariable("sequence") long sequence, HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, IOException {
        File file = fileService.downloadFiles(sequence);
        download(file, response);
    }

    @GetMapping("/files/download/{sequence}/{name}")
    public void download(@PathVariable("sequence") long sequence, @PathVariable("name") String name,
                         HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, IOException {
        File file = fileService.downloadFile(sequence, name);
        download(file, response);
    }

    private void download(File file, HttpServletResponse response) throws IOException {
        response.setHeader("Content-Type", MediaType.APPLICATION_OCTET_STREAM.toString());
        response.setHeader("Content-Disposition", "attachment; filename=" + new String(file.getName().getBytes(), "ISO-8859-1"));

        InputStream inputStream = new FileInputStream(file);
        int read;
        byte[] bytes = new byte[1024 * 1024];
        OutputStream outputStream = response.getOutputStream();
        while((read = inputStream.read(bytes))!=-1){
            outputStream.write(bytes, 0, read);
        }
        outputStream.flush();
        inputStream.close();
        outputStream.close();
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
