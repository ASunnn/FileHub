package sunnn.filehub.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sunnn.filehub.dto.response.Response;
import sunnn.filehub.util.StatusCode;

@Controller
public class VerifyController {

    @PostMapping(value = "/verify")
    @ResponseBody
    public Response verify(@RequestParam("code") String passCode) {
        UsernamePasswordToken token = new UsernamePasswordToken("", passCode);
        token.setRememberMe(true);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            return new Response(StatusCode.VERIFY_FAILED);
        }

        Session session = subject.getSession();
        return new Response(StatusCode.OJBK);
    }

    @GetMapping(value = "/try")
    @ResponseBody
    public Response tryVerify() {
        Subject subject = SecurityUtils.getSubject();

        if (subject.isAuthenticated() || subject.isRemembered())
            return new Response(StatusCode.OJBK);
        return new Response(StatusCode.VERIFY_FAILED);
    }
}
