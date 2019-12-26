package sunnn.filehub.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sunnn.filehub.dto.response.BaseResponse;
import sunnn.filehub.util.StatusCode;

@Controller
public class VerifyController {

    @PostMapping(value = "/verify")
    @ResponseBody
    public BaseResponse verify(@RequestParam("code") String passCode) {
        UsernamePasswordToken token = new UsernamePasswordToken("", passCode);
        token.setRememberMe(true);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            return new BaseResponse(StatusCode.VERIFY_FAILED);
        }

        Session session = subject.getSession();
        return new BaseResponse(StatusCode.OJBK);
    }

    /**
     * 认证
     *
     * @return 若认证成功，请求转发至主页，否则需要登录
     */
    @GetMapping(value = "/try")
    @ResponseBody
    public BaseResponse tryVerify() {
        Subject subject = SecurityUtils.getSubject();

        if (subject.isAuthenticated() || subject.isRemembered())
            return new BaseResponse(StatusCode.OJBK);
        return new BaseResponse(StatusCode.VERIFY_FAILED);
    }
}
