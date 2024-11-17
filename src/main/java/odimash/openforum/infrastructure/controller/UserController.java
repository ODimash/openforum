package odimash.openforum.infrastructure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import odimash.openforum.domain.entity.User;
import odimash.openforum.domain.repository.UserRepository;


@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired private UserRepository userRepo;

    @GetMapping("/info")
    public User getUserDetails(){
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepo.findByEmail(email).get();
    }


}
