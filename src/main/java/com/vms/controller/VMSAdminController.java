package com.vms.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vms.entity.Staff;
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
	public String addAdmin(@RequestParam("name") String name,
	        @RequestParam("email") String email,
	        @RequestParam("password") String password,
	        @RequestParam("imgBase64") String imgBase64,
	        RedirectAttributes redirectAttributes) {
		
		try {
	        // Decode Base64 image
	        String base64Image = imgBase64.split(",")[1]; // Extract Base64 content
	        byte[] imageBytes = Base64.getDecoder().decode(base64Image);

	        String fileName = "admin_" + System.currentTimeMillis() + ".png";
	        Path uploadDir = Paths.get("static/images"); // Directory for saving images
	        System.out.println(uploadDir);
	        Files.createDirectories(uploadDir); // Create directory if it doesn't exist
	        Path filePath = uploadDir.resolve(fileName);
	        System.out.println(filePath);
	        Files.write(filePath, imageBytes);
	        VMSAdmin admin = new VMSAdmin();
	        admin.setName(name);
	        admin.setEmail(email);
	        admin.setPassword(passwordEncoder.encode(password));
	        admin.setRole("ROLE_ADMIN");
	        admin.setProfile(fileName);
	        vmsAdminService.addVMSAdmin(admin);

	        redirectAttributes.addFlashAttribute("message", "Admin added successfully!");
	    } catch (Exception e) {
	        e.printStackTrace();
	        redirectAttributes.addFlashAttribute("message", "Error saving admin data!");
	    }
		return "redirect:/";		
	}
	
	@GetMapping("admin-profile")
	public String adminProfile(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        VMSAdmin admin = vmsAdminService.findVmasAdminByEmail(username);
        model.addAttribute("admin", admin);
		return "admin-profile";
	}
}
