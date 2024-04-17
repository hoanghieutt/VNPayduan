package com.poly.datn.sd18.controller.admin;

import com.poly.datn.sd18.dto.response.ProductDetailCounterResponse;
import com.poly.datn.sd18.entity.Customer;
import com.poly.datn.sd18.entity.ProductDetail;
import com.poly.datn.sd18.entity.Staff;
import com.poly.datn.sd18.service.CounterService;
import com.poly.datn.sd18.service.CustomerService;
import com.poly.datn.sd18.service.ProductDetailService;
import com.poly.datn.sd18.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/counter")
public class CounterController {
    @Autowired
    CounterService counterService;

    @Autowired
    StaffService staffService;

    @Autowired
    CustomerService customerService;

    @GetMapping()
    public String getAll(Model model){
        List<ProductDetailCounterResponse> listProductDetail = counterService.getListProductDetailCounter();
        List<Staff> listStaff = staffService.getAllActive();
        List<Customer> listCustomer = customerService.getAllActive();
        model.addAttribute("listProductDetail",listProductDetail);
        model.addAttribute("listStaff",listStaff);
        model.addAttribute("listCustomer",listCustomer);
        return "/admin/counter/index";
    }
}
