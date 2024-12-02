package com.vms.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vms.entity.Staff;
import com.vms.service.StaffService;

import jakarta.mail.internet.MimeMessage;

@Controller
public class StaffController {

	@Autowired
	StaffService staffService;
	
	@Autowired
	JavaMailSender javaMailSender;

	@GetMapping("/addstaff")
	public String showAddStaffForm(Model model) {
		model.addAttribute("staff", new Staff());
		return "staffs/add-staff";
	}

	@PostMapping("/addstaff")
	public String addStaff(
	        @RequestParam("name") String name,
	        @RequestParam("department") String department,
	        @RequestParam("email") String email,
	        @RequestParam("phoneNumber") String phoneNumber,
	        @RequestParam("imgBase64") String imgBase64,
	        RedirectAttributes redirectAttributes) {

	    try {
	        // Decode Base64 image
	        String base64Image = imgBase64.split(",")[1]; // Extract Base64 content
	        byte[] imageBytes = Base64.getDecoder().decode(base64Image);

	        // Save the image to a file
	        String fileName = "staff_" + System.currentTimeMillis() + ".png";
	        Path uploadDir = Paths.get("static/images/staffs"); // Directory for saving images
	        System.out.println(uploadDir);
	        Files.createDirectories(uploadDir); // Create directory if it doesn't exist
	        Path filePath = uploadDir.resolve(fileName);
	        System.out.println(filePath);
	        Files.write(filePath, imageBytes);

	        // Save staff details in the database
	        Staff staff = new Staff();
	        staff.setName(name);
	        staff.setDepartment(department);
	        staff.setEmail(email);
	        staff.setPhoneNumber(phoneNumber);
	        staff.setImg(fileName);
	        staffService.addStaff(staff);
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	        LocalDateTime now = LocalDateTime.now();
	        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
	        helper.setFrom("sssurwade2212@gmail.com");
	        helper.setTo(email);
	        helper.setSubject("🎉 Congratulations on Joining Us!");

	        helper.setText(
	            "<html>" +
	            "<body style='font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f9f9f9; color: #333;'>" +
	            "<div style='background: linear-gradient(90deg, #FFC107, #FF5722); padding: 20px; text-align: center; color: white;'>" +
	            "   <h1 style='margin: 0; font-size: 28px;'>🎉 Congratulations, " + staff.getName() + "!</h1>" +
	            "</div>" +
	            "<div style='padding: 20px; background-color: #ffffff; margin: 20px auto; border-radius: 10px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); max-width: 600px;'>" +
	            "   <p style='font-size: 18px;'>We are thrilled to welcome you to the <strong>Visitor Management Trust</strong>!</p>" +
	            "   <p style='font-size: 16px; margin-top: 20px;'>You are now a valued <strong style='color: #4CAF50;'>" + staff.getDepartment() + "</strong> member in our system.</p>" +
	            "   <p style='font-size: 16px; margin-top: 20px;'><strong>Joined On:</strong> <span style='color: #2196F3;'>" + now.format(formatter) + "</span></p>" +
	            "   <p style='margin-top: 20px;'>We are excited to work together and achieve great things!</p>" +
	            "</div>" +
	            "<footer style='text-align: center; padding: 10px; background-color: #FF5722; color: white; font-size: 14px; margin-top: 20px;'>" +
	            "   Best regards,<br><strong>Visitor Management Team</strong>" +
	            "</footer>" +
	            "</body>" +
	            "</html>", 
	            true // Indicates HTML content
	        );

		    javaMailSender.send(mimeMessage);

	        redirectAttributes.addFlashAttribute("message", "Staff added successfully!");
	    } catch (Exception e) {
	        e.printStackTrace();
	        redirectAttributes.addFlashAttribute("message", "Error saving staff data!");
	    }

	    return "redirect:/stafflist";
	}

	
	
	
//	@PostMapping("/addstaff")
//	public String handleStaffForm(
//	        @RequestParam("name") String name,
//	        @RequestParam("department") String department,
//	        @RequestParam("email") String email,
//	        @RequestParam("phoneNumber") String phoneNumber,
//	        @RequestParam("img") String imgBase64,
//	        RedirectAttributes redirectAttributes) {
//	    try {
//	        // Validate Base64 image data
//	        if (imgBase64 == null || !imgBase64.startsWith("data:image/")) {
//	            redirectAttributes.addFlashAttribute("message", "Invalid image data!");
//	            return "redirect:/";
//	        }
//
//	        // Decode the Base64 image string
//	        String base64Image = imgBase64.split(",")[1];
//	        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
//
//	        // Generate a unique file name (e.g., staff_<timestamp>.png)
//	        String fileName = "staff_" + System.currentTimeMillis() + ".png";
//	        Path outputPath = Paths.get("static/images/", fileName); // Save in an 'uploads' directory
//
//	        // Ensure the directory exists
//	        Files.createDirectories(outputPath.getParent());
//
//	        // Write the image to the file
//	        Files.write(outputPath, imageBytes);
//
//	        // Add success message
//	        redirectAttributes.addFlashAttribute("message", "Staff added successfully!");
//	    } catch (Exception e) {
//	        // Handle errors gracefully
//	        redirectAttributes.addFlashAttribute("message", "Error saving staff data: " + e.getMessage());
//	        e.printStackTrace();
//	    }
//	    return "redirect:/";
//	}


	
	
	@GetMapping("/stafflist")
	public String getStaffList(Model model) {
		List<Staff> staffList = staffService.getAllStaff();
		model.addAttribute("staffList", staffList);
		return "staffs/staff-list";
	}

//	@GetMapping("/removestaff/{staffId}")
//	public String removeStaff(@PathVariable Long staffId) {
//		staffService.deleteStaffById(staffId);
//		return "redirect:/stafflist";
//	}
	
	@GetMapping("/removestaff/{staffId}")
	public String removeStaff(@PathVariable Long staffId, RedirectAttributes redirectAttributes) {
	    try {
	        // Retrieve the staff details from the database
	        Staff staff = staffService.getStaffById(staffId);

	        if (staff != null) {
	            // Path for staff image
	            Path staffImagePath = Paths.get("static/images/staffs" + staff.getImg());

	            // Delete the staff image file if it exists
	            if (Files.exists(staffImagePath)) {
	                Files.delete(staffImagePath);
	            }

	            // Delete the staff record from the database
	            staffService.deleteStaffById(staffId);

	            redirectAttributes.addFlashAttribute("message", "Staff removed successfully!");
	        } else {
	            redirectAttributes.addFlashAttribute("message", "Staff not found!");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	        redirectAttributes.addFlashAttribute("message", "Error deleting staff image!");
	    } catch (Exception e) {
	        e.printStackTrace();
	        redirectAttributes.addFlashAttribute("message", "Error removing staff!");
	    }

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
		} else {
			model.addAttribute("error", "Staff not found with ID: " + staffId);
		}
		return "staffDetail";
	}
	
	@GetMapping("/updatestaff/{staffId}")
	public String showUpdateStaffForm(@PathVariable Long staffId, Model model) {
	    Staff staff = staffService.getStaffById(staffId);
	    if (staff != null) {
	        model.addAttribute("staff", staff);
	        return "staffs/update-staff";  // Display the update form
	    } else {
	        model.addAttribute("error", "Staff not found with ID: " + staffId);
	        return "redirect:/stafflist";
	    }
	}

	@PostMapping("/updatestaff/{staffId}")
	public String updateStaff(
	        @PathVariable Long staffId,
	        @RequestParam("img") MultipartFile photoFile,
	        @RequestParam("name") String name,
	        @RequestParam("department") String department,
	        @RequestParam("email") String email,
	        @RequestParam("phoneNumber") String phoneNumber
	) {
	    Staff staff = staffService.getStaffById(staffId);
	    if (staff != null) {
	        staff.setName(name);
	        staff.setDepartment(department);
	        staff.setEmail(email);
	        staff.setPhoneNumber(phoneNumber);
	        
	        if (!photoFile.isEmpty()) {
	            staff.setImg(photoFile.getOriginalFilename());
	            try {
	                File savedFile = new ClassPathResource("static/images/staffs").getFile();
	                Path path = Paths.get(savedFile.getAbsolutePath() + File.separator + photoFile.getOriginalFilename());
	                Files.copy(photoFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	        
	        staffService.updateStaff(staffId, staff);
	    }
	    return "redirect:/stafflist";
	}

}
