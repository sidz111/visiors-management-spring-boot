package com.vms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.vms.service.StaffService;
import com.vms.service.VMSAdminService;

@Controller
public class HomeController {
	
	@Autowired
	StaffService staffService;
	
	@Autowired
	VMSAdminService vmsAdminService;
	
	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("staffList", staffService.getAllStaff());
		
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        VMSAdmin admin = vmsAdminService.findVmasAdminByEmail(username);
//        model.addAttribute("admin", admin);
		
		return "index";
	}
	
	
	@GetMapping("staff")
	public String staff() {
		return "staffs/add-staff";
	}
	
	@GetMapping("visitor")
	public String visitor() {
		return "visitors/add-visitor";
	}
	
	@GetMapping("/signin")
	public String signinPage() {
		return "signin";
	}
	

}
