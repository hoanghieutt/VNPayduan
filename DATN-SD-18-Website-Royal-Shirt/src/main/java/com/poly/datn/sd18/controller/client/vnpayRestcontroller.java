package com.poly.datn.sd18.controller.client;


import com.poly.datn.sd18.controller.VnpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vnpay")
public class vnpayRestcontroller {

    @Autowired
    private VnpService vnpService;

@GetMapping("/payment")
public String payment() {
try {
return vnpService.createOrderClient();
}catch (Exception ex){
    return ex.getMessage();
}

                      }
}
