package pl.krysinski.bugtracker.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;


@Service
public class SecurityService {

    public String getUsernameLoggedUser() {
        String usernameLoggedUser = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return usernameLoggedUser;
    }
}
