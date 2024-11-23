package com.vms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.vms.entity.VMSAdmin;
import com.vms.service.VMSAdminService;

@Controller
public class VMSAdminController {
	
	@Autowired
	VMSAdminService vmsAdminService;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	@GetMapping("/add-vms-admin")
	public String addVmsAdmin(Model model) {
		model.addAttribute("vmsAdmin", new VMSAdmin());
		return "vms-admin";
	}
	
	@PostMapping("/add-admin")
	public String addAdmin(@ModelAttribute VMSAdmin admin) {
		admin.setPassword(passwordEncoder.encode(admin.getPassword()));
		admin.setRole("ROLE_ADMIN");
		vmsAdminService.addVMSAdmin(admin);
		return "redirect:/";		
	}
}
