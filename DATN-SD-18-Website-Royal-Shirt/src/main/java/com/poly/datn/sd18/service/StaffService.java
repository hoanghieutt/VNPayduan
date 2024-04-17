package com.poly.datn.sd18.service;

import com.poly.datn.sd18.entity.Staff;
import com.poly.datn.sd18.model.dto.StaffDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StaffService {
    List<Staff> getAllActive();
    Staff findById(Integer id);
    List<Staff> getAllStaff();
    Page<Staff> getAllStaffPages(Integer pageNo);
    List<Staff> searchStaffByName(String name);
    List<Staff> searchStaffByStatus(Integer status);
    Staff createStaff(StaffDTO staffDTO, MultipartFile multipartFile);
    Staff findStaffById(Integer id);
    Staff updateStaff(StaffDTO staffDTO, Integer id, MultipartFile file);
    Staff setStatusStaff(Integer id);
}
