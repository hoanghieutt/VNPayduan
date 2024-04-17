package com.poly.datn.sd18.model.dto;

import com.poly.datn.sd18.entity.Customer;
import com.poly.datn.sd18.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Integer customerId;
    private String userName;
    private String phone;
    private String address;
    private String note;
    private Float totalPrice;
    private String shopping;
    private Integer status;
    private Boolean type;
    private Date conformDate;

    public Order map(Order order){
        order.setCustomer(Customer.builder().id(this.customerId).build());
        order.setUsername(this.userName);
        order.setPhone(this.phone);
        order.setAddress(this.address);
        order.setNote(this.note);
        order.setTotalPrice(this.totalPrice);
        order.setShopping(this.shopping);
        order.setStatus(this.status);
        order.setType(true);
        order.setConfirmDate(new Date(System.currentTimeMillis()));
        return order;
    }
}
