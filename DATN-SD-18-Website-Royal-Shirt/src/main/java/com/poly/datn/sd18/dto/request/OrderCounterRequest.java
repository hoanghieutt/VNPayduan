package com.poly.datn.sd18.dto.request;

import com.poly.datn.sd18.entity.Customer;
import com.poly.datn.sd18.entity.Order;
import com.poly.datn.sd18.entity.Staff;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class OrderCounterRequest {
    private Integer staffId;
    private Integer customerId;
    private Float totalPrice;
    private String note;
    private String shopping;
    private Integer status;
    private Date successDate;

    public Order map(Order order){
        order.setPhone(Customer.builder().id(this.customerId).build().getPhone());
        order.setUsername(Customer.builder().id(this.customerId).build().getName());
        order.setTotalPrice(this.totalPrice);
        order.setNote(this.note);
        order.setShopping(this.shopping);
        order.setStatus(this.status);
        order.setSuccessDate(this.successDate);
        order.setStaff(Staff.builder().id(this.staffId).build());
        order.setCustomer(Customer.builder().id(this.customerId).build());
        return order;
    }
}
