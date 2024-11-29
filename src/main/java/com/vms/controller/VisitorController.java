package com.vms.controller;

import java.io.File;
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

import com.vms.entity.Visitor;
import com.vms.service.VisitorService;

import jakarta.mail.internet.MimeMessage;

@Controller
public class VisitorController {

	@Autowired
	VisitorService visitorService;
	
	@Autowired
	JavaMailSender javaMailSender;

	@GetMapping("/addvisitor")
	public String showAddVisitorForm(Model model) {
		model.addAttribute("visitor", new Visitor());
		return "visitors/add-visitor";
	}

	@PostMapping("/addvisitor")
	public String addVisitor(
	        @RequestParam("name") String name,
	        @RequestParam("contactNumber") String contactNumber,
	        @RequestParam("email") String email,
	        @RequestParam("imgBase64") String imgBase64,
	        RedirectAttributes redirectAttributes) {

	    try {
	       
	        String base64Image = imgBase64.split(",")[1];
	        byte[] imageBytes = Base64.getDecoder().decode(base64Image);

	        String fileName = "visitor_" + System.currentTimeMillis() + ".png";
	        Path uploadDir = Paths.get("static/images/visitors"); 
	        Files.createDirectories(uploadDir);
	        Path filePath = uploadDir.resolve(fileName);
	        Files.write(filePath, imageBytes);

	        Visitor visitor = new Visitor();
	        visitor.setName(name);
	        visitor.setContactNumber(contactNumber);
	        visitor.setEmail(email);
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	        LocalDateTime now = LocalDateTime.now();
	        visitor.setCheckIn(now.format(formatter));
	        visitor.setCheckOut("pending");
	        visitor.setIsCheckOut(false);
	        visitor.setImg(fileName);
	        visitorService.addVisitor(visitor);
	        
	        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
	        helper.setFrom("sssurwade2212@gmail.com");
	        helper.setTo(email);
	        helper.setSubject("Checked in into Visitor Management System");
	        helper.setText(
	        	    "<html>" +
	        	    "<body style='font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f9f9f9; color: #333;'>" +
	        	    "<div style='background: linear-gradient(90deg, #4CAF50, #2196F3); padding: 20px; text-align: center; color: white;'>" +
	        	    "   <h1 style='margin: 0; font-size: 28px;'>Welcome, " + visitor.getName() + "!</h1>" +
	        	    "</div>" +
	        	    "<div style='padding: 20px; background-color: #ffffff; margin: 20px auto; border-radius: 10px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); max-width: 600px;'>" +
	        	    "   <p style='font-size: 18px;'>We are delighted to have you with us at the <strong>Visitor Management System</strong>.</p>" +
	        	    "   <p style='font-size: 16px; margin-top: 20px;'>Your <strong style='color: #2196F3;'>Check-In</strong> has been successfully registered.</p>" +
	        	    "   <p style='font-size: 16px; margin-top: 20px;'><strong>Checked-In At:</strong> <span style='color: #2196F3;'>" + now.format(formatter) + "</span></p>" +
	        	    "   <p style='margin-top: 20px;'>Thank you for visiting us. We hope you have a great experience!</p>" +
	        	    "</div>" +
	        	    "<footer style='text-align: center; padding: 10px; background-color: #2196F3; color: white; font-size: 14px; margin-top: 20px;'>" +
	        	    "   Best regards,<br><strong>Visitor Management Team</strong>" +
	        	    "</footer>" +
	        	    "</body>" +
	        	    "</html>", 
	        	    true // Indicates HTML content
	        	);


	        javaMailSender.send(mimeMessage);


	        redirectAttributes.addFlashAttribute("message", "Visitor added successfully!");
	    } catch (Exception e) {
	        e.printStackTrace();
	        redirectAttributes.addFlashAttribute("message", "Error saving visitor data!");
	    }
	    
	    return "redirect:/visitorlist";
	}


//	@PostMapping("/addvisitor")
//	public String addVisitor(Visitor visitor) {
//		visitorService.addVisitor(visitor);
//		return "redirect:/visitorlist";
//	}

	@PostMapping("/removevisitor")
	public String removeVisitor(@RequestParam("visitorId") Long visitorId) {
		visitorService.deleteVisitorById(visitorId);
		return "redirect:/visitorlist";
	}

	@GetMapping("/visitor-search-id")
	public String searchByVisitorId(@RequestParam("visitorId") Long visitorId, Model model) {
		Visitor visitor = visitorService.getVisitorById(visitorId);
		if (visitor != null) {
			model.addAttribute("visitor", visitor);
			return "visitorDetail";
		} else {
			model.addAttribute("visitorerror", "Visitor not found with ID: " + visitorId);
			model.addAttribute("visitorList", visitorService.getAllVisitors());
			return "visitorDetail";
		}
	}

	@GetMapping("/visitor/{visitorId}")
	public String viewVisitorDetails(@PathVariable("visitorId") Long visitorId, Model model) {
		Visitor visitor = visitorService.getVisitorById(visitorId);
		if (visitor != null) {
			model.addAttribute("visitor", visitor);
			return "visitorDetail";
		} else {
			model.addAttribute("visitorerror", "visitor not found with ID: " + visitorId);
		}
		return "visitorDetail";
	}

	@GetMapping("/visitorlist")
	public String getVisitorList(Model model) {
		List<Visitor> visitorList = visitorService.getAllVisitors();
		model.addAttribute("visitorList", visitorList);
		return "visitors/visitor-list"; // This should match the template name
	}

	@GetMapping("/updatevisitor/{visitorId}")
	public String showUpdateVisitorForm(@PathVariable Long visitorId, Model model) {
		Visitor visitor = visitorService.getVisitorById(visitorId);
		if (visitor != null) {
			model.addAttribute("visitor", visitor);
			return "visitors/update-visitor"; // Display the update form
		} else {
			model.addAttribute("error", "visitor not found with ID: " + visitorId);
			return "redirect:/visitorlist";
		}
	}

	@PostMapping("/updatevisitor/{visitorId}")
	public String updateVisitor(@PathVariable Long visitorId, @RequestParam("img") MultipartFile photoFile,
			@RequestParam("name") String name, @RequestParam("contactNumber") String contactNumber,
			@RequestParam("email") String email) {
		Visitor visitor = visitorService.getVisitorById(visitorId);

		if (visitor != null) {
			visitor.setName(name);
			visitor.setContactNumber(contactNumber);
			visitor.setEmail(email);
			if (!photoFile.isEmpty()) {
				visitor.setImg(photoFile.getOriginalFilename());
				try {
					File savedFile = new ClassPathResource("static/images/visitors").getFile();
					Path path = Paths
							.get(savedFile.getAbsolutePath() + File.separator + photoFile.getOriginalFilename());
					Files.copy(photoFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			visitorService.updateVisitor(visitorId, visitor);
		}
		return "redirect:/visitorlist";
	}
	
	
	@PostMapping("/checkoutvisitor")
	public String checkOutVisitor(@RequestParam("visitorId") Long visitorId, RedirectAttributes redirectAttributes) {
	    try {
	        Visitor visitor = visitorService.getVisitorById(visitorId);
	        if (visitor != null && visitor.getCheckOut().equals("pending")) {
	            // Set the check-out time
	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	            LocalDateTime now = LocalDateTime.now();
	            visitor.setCheckOut(now.format(formatter));
	            visitor.setIsCheckOut(true);  // Assuming 'isCheckOut' is a boolean field in the entity

	            // Update the visitor's data
	            visitorService.updateVisitor(visitorId, visitor);
	            
	            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		        helper.setFrom("sssurwade2212@gmail.com");
		        helper.setTo(visitor.getEmail());
		        helper.setSubject("Checked OUT into Visitor Management System");
		        helper.setText(
		        	    "<html>" +
		        	    "<body style='font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f9f9f9; color: #333;'>" +
		        	    "<div style='background: linear-gradient(90deg, #FF9800, #FF5722); padding: 20px; text-align: center; color: white;'>" +
		        	    "   <h1 style='margin: 0; font-size: 28px;'>Thank You, " + visitor.getName() + "!</h1>" +
		        	    "</div>" +
		        	    "<div style='padding: 20px; background-color: #ffffff; margin: 20px auto; border-radius: 10px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); max-width: 600px;'>" +
		        	    "   <p style='font-size: 18px;'>We appreciate your visit to the <strong>Visitor Management System</strong>.</p>" +
		        	    "   <p style='font-size: 16px; margin-top: 20px;'><strong>Checked-Out At:</strong> <span style='color: #2196F3;'>" + now.format(formatter) + "</span></p>" +
		        	    "   <p style='font-size: 16px; margin-top: 20px;'>We hope to see you again soon. Your presence means a lot to us!</p>" +
		        	    "</div>" +
		        	    "<footer style='text-align: center; padding: 10px; background-color: #FF5722; color: white; font-size: 14px; margin-top: 20px;'>" +
		        	    "   Best regards,<br><strong>Visitor Management Team</strong>" +
		        	    "</footer>" +
		        	    "</body>" +
		        	    "</html>",
		        	    true // Indicates HTML content
		        	);
			    javaMailSender.send(mimeMessage);

	            redirectAttributes.addFlashAttribute("message", "Visitor checked out successfully!");
	        } else {
	            redirectAttributes.addFlashAttribute("message", "Visitor has already checked out or not found!");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        redirectAttributes.addFlashAttribute("message", "Error during check out!");
	    }
	    return "redirect:/visitorlist";
	}
	



}