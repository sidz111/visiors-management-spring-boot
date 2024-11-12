package com.vms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.vms.service.StaffService;

@Controller
public class HomeController {
	
	@Autowired
	StaffService staffService;
	
	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("staffList", staffService.getAllStaff());
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
	

}
