package sunnn.filehub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class View {

    @RequestMapping(value = "/login")
    public String login() {
        return "/index.html";
    }

    @RequestMapping(value = "/index")
    public String index() {
        return "/index.html";
    }

    @RequestMapping(value = "/home")
    public String home() {
        return "/index.html";
    }

    @RequestMapping(value = "/list")
    public String list() {
        return "/index.html";
    }

    @RequestMapping(value = "/share")
    public String copy() {
        return "/index.html";
    }

    @RequestMapping(value = "/s/*")
    public String share() {
        return "/index.html";
    }
}
