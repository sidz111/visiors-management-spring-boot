package com.vms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.vms.entity.Staff;
import com.vms.service.StaffService;

@Controller
public class StaffController {

	@Autowired
	StaffService staffService;
	

	@GetMapping("/addstaff")
	public String showAddStaffForm(Model model) {
		model.addAttribute("staff", new Staff());
		return "staffs/add-staff";
	}

	@PostMapping("/addstaff")
	public String addStaff(Staff staff) {
		staffService.addStaff(staff);
		return "/staffs/add-staff";
	}
	
//	@GetMapping("/stafflist")
//	public String getVisitorList(Model model) {
//		model.addAttribute("slist", staffService.getAllStaff());
//		return "staffs/staff-list";
//	}
	
	@GetMapping("/stafflist")
	public String getStaffList(Model model) {
	    List<Staff> staffList = staffService.getAllStaff();
	    model.addAttribute("staffList", staffList);
	    return "staffs/staff-list"; // This should match the template name
	}
	
	@PostMapping("/removestaff")
	public String removeStaff(@RequestParam("staffId") Long staffId) {
	    staffService.deleteStaffById(staffId);
	    return "redirect:/stafflist";       
	}
	
    @GetMapping("/search-id")
    public String searchByStaffId(@RequestParam("staffId") Long staffId, Model model) {
        Staff staff = staffService.getStaffById(staffId);
        if (staff != null) {
            model.addAttribute("staff", staff);
            return "staffDetail";
        } else {
            model.addAttribute("error", "Staff not found with ID: " + staffId);
            model.addAttribute("slist", staffService.getAllStaff());
            return "staffDetail";
        }
    }

    @GetMapping("/staff/{staffId}")
    public String viewStaffDetails(@PathVariable("staffId") Long staffId, Model model) {
        Staff staff = staffService.getStaffById(staffId);
        if (staff != null) {
            model.addAttribute("staff", staff);
            return "staffDetail"; 
        }
        else {
        	model.addAttribute("error", "Staff not found with ID: " + staffId);        	
        }
        return "staffDetail";
    }
}
