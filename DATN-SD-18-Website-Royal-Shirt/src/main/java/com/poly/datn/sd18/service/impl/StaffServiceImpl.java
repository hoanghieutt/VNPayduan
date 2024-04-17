package com.poly.datn.sd18.service.impl;

import com.poly.datn.sd18.entity.Staff;
import com.poly.datn.sd18.model.dto.StaffDTO;
import com.poly.datn.sd18.repository.StaffRepository;
import com.poly.datn.sd18.service.StaffService;
import com.poly.datn.sd18.util.ImageUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {
    private final StaffRepository staffRepository;
    private final ImageUpload imageUpload;

    @Override
    public List<Staff> getAllActive() {
        return staffRepository.getAllActive();
    }

    @Override
    public Staff findById(Integer id) {
        return staffRepository.findById(id).get();
    }

    @Override
    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    @Override
    public Page<Staff> getAllStaffPages(Integer pageNo) {
        return null;
    }

    @Override
    public List<Staff> searchStaffByName(String name) {
        return null;
    }

    @Override
    public List<Staff> searchStaffByStatus(Integer status) {
        return null;
    }

    @Override
    public Staff createStaff(StaffDTO staffDTO, MultipartFile file) {
        try {
            Staff existingEmail = staffRepository.findStaffByEmail(staffDTO.getEmail());
            if (existingEmail != null) {
                throw new RuntimeException("Email đã tồn tại");
            }
            Staff staff = Staff.builder()
                    .name(staffDTO.getName())
                    .email(staffDTO.getEmail())
                    .phone(staffDTO.getPhone())
                    .address(staffDTO.getAddress())
                    .password(staffDTO.getPassword())
                    .role(staffDTO.getRole())
                    .status(0)
                    .build();
            if (file == null) {
                staff.setAvatar(null);
            }else {
                imageUpload.uploadImage(file);
                staff.setAvatar(Base64.getEncoder().encodeToString(file.getBytes()));
            }
            return staffRepository.save(staff);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Staff findStaffById(Integer id) {
        return staffRepository.findById(id).orElse(null);
    }

    @Override
    public Staff updateStaff(StaffDTO staffDTO, Integer id, MultipartFile file) {
        try{
            Staff staff = findStaffById(id);
            Staff existingEmail = staffRepository.findStaffByEmail(staffDTO.getEmail());
            if (existingEmail != null) {
                throw new RuntimeException("Email đã tồn tại");
            }
            if (staff != null) {
                staff.setName(staffDTO.getName());
                staff.setEmail(staffDTO.getEmail());
                staff.setPhone(staffDTO.getPhone());
                staff.setAddress(staffDTO.getAddress());
                staff.setPassword(staffDTO.getPassword());
                if (file == null) {
                    staff.setAvatar(staffDTO.getAvatar());
                }else {
                    if (!imageUpload.checkExisted(file)) {
                        imageUpload.uploadImage(file);
                    }
                    staff.setAvatar(Base64.getEncoder().encodeToString(file.getBytes()));
                }
                return staffRepository.save(staff);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Staff setStatusStaff(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID nhân viên không được để trống");
        }

        Staff staff = findStaffById(id);
        if (staff != null) {
            if (staff.getStatus() == 0) {
                staff.setStatus(1);
            } else {
                staff.setStatus(0);
            }
            return staffRepository.save(staff);
        }
        return null;
    }
}
