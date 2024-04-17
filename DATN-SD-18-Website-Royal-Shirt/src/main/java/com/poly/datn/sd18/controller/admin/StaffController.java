package com.poly.datn.sd18.controller.admin;

import com.poly.datn.sd18.entity.Role;
import com.poly.datn.sd18.entity.Staff;
import com.poly.datn.sd18.model.dto.StaffDTO;
import com.poly.datn.sd18.service.RoleService;
import com.poly.datn.sd18.service.StaffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/staffs")
public class StaffController {
    private final RoleService roleService;
    private final StaffService staffService;

    @GetMapping("")
    public String admin(Model model){
        List<Staff> list = staffService.getAllStaff();
        model.addAttribute("listStaff", list);
        return "admin/staff/index";
    }

    @GetMapping("/all")
    public String getStaff(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                           Model model) {
        Page<Staff> listStaff = staffService.getAllStaffPages(pageNo);
        model.addAttribute("listStaff", listStaff);
        model.addAttribute("totalPage",listStaff.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        return "admin/staff/index";
    }

    @GetMapping("/formCreate")
    public String formCreate(Model model) {
        List<Role> listRole = roleService.getAllRole();
        model.addAttribute("listRole", listRole);
        model.addAttribute("staff", new Staff());
        return "admin/staff/create";
    }

    @PostMapping("/create")
    public String saveStaff(@Valid @ModelAttribute StaffDTO staffDTO,
                            @RequestParam("imageStaff") MultipartFile file,
                            BindingResult result) {
        if (result.hasErrors()) {
            return "admin/staff/create";
        }else {
            Staff staff = staffService.createStaff(staffDTO, file);
            return "redirect:/admin/staffs";
        }
    }

    @GetMapping("/findId/{id}")
    @ResponseBody
    public ResponseEntity<?> getStaffById(@PathVariable("id") Integer id) {
        try {
            Staff staff = staffService.findStaffById(id);
            if (staff != null) {
                return ResponseEntity.ok(staff);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateStaff(@Valid @ModelAttribute StaffDTO staffDTO,
                                         @RequestParam("imageStaff") MultipartFile file,
                                         @PathVariable("id") Integer id,
                                         BindingResult result) {
        try {
            Staff staff = staffService.updateStaff(staffDTO, id, file);
            return ResponseEntity.ok(staff);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}