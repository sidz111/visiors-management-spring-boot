package com.vms.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
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
	
	
	@GetMapping("/updateadmin/{id}")
	public String showUpdateAdminForm(@PathVariable Integer id, Model model) {
	   VMSAdmin admin = vmsAdminService.findVmasAdminById(id);
	    if (admin != null) {
	        model.addAttribute("admin", admin);
	        return "update-admin";  // Display the update form
	    } else {
	        model.addAttribute("error", "Admin not found with ID: " + id);
	        return "redirect:/admin-profile";
	    }
	}
	
	
	
	@PostMapping("/updateadmin/{id}")
	public String updateAdmin(
	        @PathVariable Integer id,
	        @RequestParam("img") MultipartFile photoFile,
	        @RequestParam("name") String name,
	        @RequestParam("email") String email,
	        @RequestParam("password") String password
	) {
		VMSAdmin admin = vmsAdminService.findVmasAdminById(id);
	    if (admin != null) {
	        admin.setName(name);
	        admin.setEmail(email);
	        admin.setPassword(passwordEncoder.encode(password));
	        
	        if (!photoFile.isEmpty()) {
	            admin.setProfile(photoFile.getOriginalFilename());
	            try {
	                File savedFile = new ClassPathResource("static/images").getFile();
	                Path path = Paths.get(savedFile.getAbsolutePath() + File.separator + photoFile.getOriginalFilename());
	                Files.copy(photoFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	        vmsAdminService.updateVmsAdmin(email, admin);
	    }
	    return "redirect:/admin-profile";
	}
	
}
