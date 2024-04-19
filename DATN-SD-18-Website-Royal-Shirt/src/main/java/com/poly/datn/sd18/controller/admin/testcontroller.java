package com.poly.datn.sd18.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/admin/bill")
public class testcontroller {
@GetMapping("")
    public String hienthi(){

    return "/admin/test/index";
                            }
}
