package sunnn.filehub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class View {

    @RequestMapping(value = "/index")
    public String index() {
        return "index";
    }
}
