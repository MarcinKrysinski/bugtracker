package pl.krysinski.bugtracker.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @RequestMapping ("/login") //POST i GET są obsługiwane
    public String index(){
        return "security/login";
    }
}
