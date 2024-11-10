package com.vms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	@GetMapping("/")
	public String home() {
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
