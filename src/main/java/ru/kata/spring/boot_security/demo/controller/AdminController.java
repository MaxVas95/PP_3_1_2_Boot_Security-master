package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private UserService userService;
    private RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String showAllUsers(Model model) {
        model.addAttribute("admin", userService.getAllUsers());
        model.addAttribute("roles", roleService.findAll());
        return "adminPage";
    }


    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("user", userService.showUser(id));
        model.addAttribute("roles", roleService.findAll());
        return "editUser";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "editUser";
        }
        userService.edit(id, user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }

    @GetMapping(value = "/{id}/showUser")
    public String showUser(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", userService.showUser(id));
        return "showUser";
    }

    @GetMapping(value = "/newUser")
    public String newUser(Model model) {
        User user = new User();
        user.setRoles(new ArrayList<>());

        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.findAll());
        return "createUser";
    }


    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "createUser";
        }
        userService.save(user);
        return "redirect:/admin";
    }

}
