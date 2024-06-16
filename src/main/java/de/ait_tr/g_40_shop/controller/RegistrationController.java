package de.ait_tr.g_40_shop.controller;

import de.ait_tr.g_40_shop.domain.entity.User;
import de.ait_tr.g_40_shop.exception_handling.Response;
import de.ait_tr.g_40_shop.service.interfaces.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
public class RegistrationController {

    private final UserService service;

    public RegistrationController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public Response register(@RequestBody User user) {
        service.register(user);
        return new Response("Registration complete. Please check your email.");
    }

//    @GetMapping
//    public Response activate(@RequestBody User user) {
//        return new Response("Activation successful");
//    }


    @GetMapping
    public Response activate(@RequestParam String code) {
        service.activate(code);
        return new Response("Activation successful");
    }


}
