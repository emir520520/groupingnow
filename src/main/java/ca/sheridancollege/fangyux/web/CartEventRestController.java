package ca.sheridancollege.fangyux.web;

import ca.sheridancollege.fangyux.beans.User;
import ca.sheridancollege.fangyux.repository.UserRepository;
import ca.sheridancollege.fangyux.service.CartEventServices;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@AllArgsConstructor
public class CartEventRestController {
    @Autowired
    private CartEventServices cartEventServices;

    private UserRepository userRepo;



}
