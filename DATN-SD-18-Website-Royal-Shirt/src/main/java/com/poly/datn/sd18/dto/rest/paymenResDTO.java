package com.poly.datn.sd18.dto.rest;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class paymenResDTO implements Serializable {
    private String status;
    private String message;
    private String URL;
}
