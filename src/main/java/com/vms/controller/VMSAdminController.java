package com.vms.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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

import jakarta.mail.internet.MimeMessage;

@Controller
public class VMSAdminController {
	
	@Autowired
	VMSAdminService vmsAdminService;
	
	@Autowired
	JavaMailSender javaMailSender;
	
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
	        
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	        LocalDateTime now = LocalDateTime.now();
	        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
	        helper.setFrom("sssurwade2212@gmail.com");
	        helper.setTo(email);
	        helper.setSubject("🎉 Congratulations, " + admin.getName() + "! You're Now an Admin! 🚀");

	        helper.setText(
	            "<html>" +
	            "<body style='font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f9; color: #333;'>" +
	            
	            // Header Section with Gradient
	            "<div style='background: linear-gradient(90deg, #FF9800, #FF5722); padding: 20px; text-align: center; color: white;'>" +
	            "   <h1 style='margin: 0; font-size: 32px;'>🎉 Congratulations, " + admin.getName() + "!</h1>" +
	            "   <p style='font-size: 18px;'>You are officially an Admin of the Visitor Management System! 🚀</p>" +
	            "</div>" +

	            // Main Content Section
	            "<div style='padding: 20px; background-color: #ffffff; margin: 20px auto; border-radius: 12px; box-shadow: 0 4px 10px rgba(0,0,0,0.1); max-width: 650px;'>" +
	            
	            "   <p style='font-size: 18px;'>Welcome aboard! As an Admin, you have full control over the Visitor Management System. Here are your admin details:</p>" +
	            
	            "   <h3 style='color: #2196F3;'>🔑 Admin Credentials:</h3>" +
	            "   <ul style='font-size: 16px; color: #333;'>" +
	            "       <li><strong>Username:</strong> <span style='color: #2196F3;'>" + admin.getEmail() + "</span></li>" +
	            "       <li><strong>Password:</strong> <span style='color: #FF5722;'>" + password + "</span></li>" +
	            "   </ul>" +
	            
	            "   <h3 style='color: #4CAF50;'>📜 Admin Responsibilities:</h3>" +
	            "   <ul style='font-size: 16px; color: #333;'>" +
	            "       <li><strong>Manage Visitors:</strong> Approve or reject visitor check-ins and check-outs.</li>" +
	            "       <li><strong>Maintain User Data:</strong> Ensure that user data is accurate and up-to-date.</li>" +
	            "       <li><strong>Monitor Activity:</strong> Keep an eye on system activity for any anomalies or issues.</li>" +
	            "       <li><strong>Ensure Security:</strong> Make sure that all system operations are secure and compliant with regulations.</li>" +
	            "   </ul>" +
	            
	            "   <h3 style='color: #FF5722;'>⚖️ Rules & Regulations:</h3>" +
	            "   <ul style='font-size: 16px; color: #333;'>" +
	            "       <li><strong>Confidentiality:</strong> Always keep user data confidential and secure.</li>" +
	            "       <li><strong>Proper Use:</strong> Do not use your admin privileges for personal or unauthorized activities.</li>" +
	            "       <li><strong>Follow Procedures:</strong> Follow all system procedures and guidelines when managing the system.</li>" +
	            "       <li><strong>Regular Monitoring:</strong> Regularly check the system to ensure smooth operation.</li>" +
	            "   </ul>" +

	            // Closing Section
	            "   <p style='font-size: 16px; color: #333;'>We’re excited to have you on board as an Admin! Should you need any assistance, feel free to reach out to the team.</p>" +
	            "</div>" +

	            // Footer Section
	            "<footer style='text-align: center; padding: 15px; background-color: #2196F3; color: white; font-size: 14px; margin-top: 20px;'>" +
	            "   Best regards,<br><strong>Visitor Management Team</strong>" +
	            "</footer>" +

	            "</body>" +
	            "</html>", 
	            true // Indicates HTML content
	        );

	        javaMailSender.send(mimeMessage);

	        
	        
	        
	        
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
