package com.poly.datn.sd18.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "phone")
    private String phone;

    @Column(name = "username")
    private String username;

    @Column(name = "total_price")
    private Float totalPrice;

    @Column(name = "weight")
    private Float weight;

    @Column(name = "ship_cost")
    private Float shipCost;

    @Column(name = "note")
    private String note;

    @Column(name = "shopping")
    private String shopping;

    @Column(name = "address")
    private String address;

    @Column(name = "status")
    private Integer status;

    @Column(name = "confirm_date")
    private Date confirmDate;

    @Column(name = "confirm_wait_date")
    private Date confirmWaitDate;

    @Column(name = "ship_date")
    private Date shipDate;

    @Column(name = "ship_wait_date")
    private Date shipWaitDate;

    @Column(name = "success_date")
    private Date successDate;

    @Column(name = "cancel_date")
    private Date cancelDate;

    @Column(name = "type")
    private Boolean type;

    @ManyToOne
    @JoinColumn(name = "staff_id", referencedColumnName = "id")
    private Staff staff;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "transaction_id", referencedColumnName = "id")
    private Transaction transaction;

    @JsonIgnore
    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails;
}
