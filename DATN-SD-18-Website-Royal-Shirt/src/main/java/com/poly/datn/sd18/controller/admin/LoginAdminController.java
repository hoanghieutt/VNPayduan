package com.poly.datn.sd18.controller.admin;

import com.poly.datn.sd18.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LoginAdminController {
    private final StaffService staffService;

    @GetMapping("/formLogin")
    public String homeLogin() {
        return "admin/login/index";
    }

    @GetMapping("/formForgot")
    public String homeForgot() {
        return "admin/login/forgot";
    }
}