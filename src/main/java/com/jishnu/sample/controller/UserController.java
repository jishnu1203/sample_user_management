package com.jishnu.sample.controller;

import javax.validation.Valid;

import com.jishnu.sample.model.User;
import com.jishnu.sample.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = {"/", "/login"}, method = RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView model = new ModelAndView();
        model.setViewName("user/login");
        return model;
    }

    @RequestMapping(value = {"/signup"}, method = RequestMethod.GET)
    public ModelAndView signup() {
        ModelAndView model = new ModelAndView();
        User user = new User();
        model.addObject("user" , user);
        model.setViewName("user/signup");

        return model;
    }

    @RequestMapping(value = {"/signup"}, method = RequestMethod.POST)
    public ModelAndView createUser(@Valid User user, @RequestParam(value = "userRole" , required = false) List<String> userRole, BindingResult bindingResult) {
        ModelAndView model = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());

        if (userExists != null) {
            bindingResult.rejectValue("email", "error.user", "This email already exists!");
        }
        if (bindingResult.hasErrors()) {
            model.setViewName("user/signup");
        } else {
            if(userRole == null || userRole.size() == 0) {
                userRole = Arrays.asList("USER");
            }

            userService.saveUser(user,userRole);
            model.addObject("msg", "User has been registered successfully!");
            model.addObject("user", new User());
            model.setViewName("user/login");
        }

        return model;
    }

    @RequestMapping(value = {"/home/dashboard"}, method = RequestMethod.GET)
    public ModelAndView home(Authentication authentication) {
        ModelAndView model = new ModelAndView();

        if (authentication == null) {
            model.setViewName("/login");
        }
        Set<String> roles = authentication.getAuthorities().stream()
                .map(r -> r.getAuthority()).collect(Collectors.toSet());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        model.addObject("userName", user.getUserName());
        model.addObject("firstName", user.getFirstName());
        model.addObject("role", String.join(",", new ArrayList<String>(roles)));
        model.addObject("email", user.getEmail());
        if (roles.contains("ADMIN")) {
            model.setViewName("admin/home");
            model.addObject("userList", userService.getUserList());
            return model;
        }
        model.setViewName("home/home");
        return model;
    }
}
