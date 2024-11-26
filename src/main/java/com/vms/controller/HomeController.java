package com.vms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.vms.entity.VMSAdmin;
import com.vms.service.StaffService;
import com.vms.service.VMSAdminService;

@Controller
public class HomeController {

	@Autowired
	StaffService staffService;

	@Autowired
	VMSAdminService vmsAdminService;

	@Autowired
	PasswordEncoder passwordEncoder;

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

	@GetMapping("/signin")
	public String signinPage() {
		return "signin";
	}

	@GetMapping("/reset")
	public String resetPassget(Model model) {
		return "reset-pass";
	}

	@PostMapping("/reset")
	public String resetPasspost(@RequestParam("email") String email,
			@RequestParam("password") String password,
			Model model) {
		VMSAdmin admin = vmsAdminService.findVmasAdminByEmail(email);
		if (admin == null) {
			model.addAttribute("wrong", "User not found");
			return "reset-pass";
		} else {
			admin.setPassword(passwordEncoder.encode(password));
			vmsAdminService.addVMSAdmin(admin);
			return "redirect:/";
		}
	}

}
