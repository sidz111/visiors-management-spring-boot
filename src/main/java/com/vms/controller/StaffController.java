package com.vms.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
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
	        Path uploadDir = Paths.get("static/images"); // Directory for saving images
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

	@GetMapping("/removestaff/{staffId}")
	public String removeStaff(@PathVariable Long staffId) {
		staffService.deleteStaffById(staffId);
		return "redirect:/";
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
	                File savedFile = new ClassPathResource("static/images").getFile();
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
