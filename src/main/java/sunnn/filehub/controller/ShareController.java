package sunnn.filehub.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/share")
public class ShareController {

    /**
     * 创建一个分享
     *
     * 选是否加密，有效期固定一天
     */
    @PostMapping("create")
    public void create() {

    }

    /**
     * 获取分享的东西详情
     *
     * 有密码给密码，没有密码直接打开
     * 只能看到文件列表和名字
     */
    @PostMapping("/getShare")
    public void getShare() {

    }
}
