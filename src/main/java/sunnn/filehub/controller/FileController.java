package sunnn.filehub.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileController {

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
    public void upload() {

    }

    /**
     * 获取一次上传的信息
     *
     * 有密码给密码，没有密码直接打开
     * 返回名字、文件列表详情（名字、大小）、上传时间、有效期
     */
    @PostMapping("/getFile")
    public void getFile() {

    }

    /**
     * 下载文件
     *
     * 会先在session里记录有没在getFile里授权，有的话能下，没有的话不能下
     * 两段式———— /id/详细文件
     */
    @GetMapping("/download")
    public void download() {

    }
}
