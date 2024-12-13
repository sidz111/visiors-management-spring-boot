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
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
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
	        	    "   <p style='font-size: 16px; margin-top: 20px;'><strong>Your ID:</strong> <span style='color: #4CAF50;'>" + visitor.getRandomId() + "</span></p>" +
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

//	@PostMapping("/removevisitor")
//	public String removeVisitor(@RequestParam("visitorId") Long visitorId) {
//		visitorService.deleteVisitorById(visitorId);
//		return "redirect:/visitorlist";
//	}

	
	@PostMapping("/removevisitor")
	public String removeVisitor(@RequestParam("visitorId") Long visitorId, RedirectAttributes redirectAttributes) {
	    try {
	        // Retrieve the visitor details from the database
	        Visitor visitor = visitorService.getVisitorById(visitorId);
	        
	        if (visitor != null) {
	            // Paths for profile and government ID images
	            Path profileImagePath = Paths.get("static/images/visitors/" + visitor.getImg());
	            Path govIdImagePath = Paths.get("static/images/gov-ids/" + visitor.getGovId());

	            // Delete profile image if it exists
	            if (Files.exists(profileImagePath)) {
	                Files.delete(profileImagePath);
	            }

	            // Delete government ID image if it exists
	            if (Files.exists(govIdImagePath)) {
	                Files.delete(govIdImagePath);
	            }

	            // Delete the visitor from the database
	            visitorService.deleteVisitorById(visitorId);

	            redirectAttributes.addFlashAttribute("message", "Visitor removed successfully!");
	        } else {
	            redirectAttributes.addFlashAttribute("message", "Visitor not found!");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	        redirectAttributes.addFlashAttribute("message", "Error deleting visitor files!");
	    } catch (Exception e) {
	        e.printStackTrace();
	        redirectAttributes.addFlashAttribute("message", "Error removing visitor!");
	    }

	    return "redirect:/visitorlist";
	}

	
	
	@GetMapping("/visitor-search-id")
	public String searchByVisitorId(@RequestParam("randomId") Integer randomId, Model model) {
		Visitor visitor = visitorService.findVisitorbyRandomId(randomId);
		if (visitor != null) {
			model.addAttribute("visitor", visitor);
			return "visitorDetail";
		} else {
			model.addAttribute("visitorerror", "Visitor not found with ID: " + randomId);
			model.addAttribute("visitorList", visitorService.getAllVisitors());
			return "visitorDetail";
		}
	}
	
	@GetMapping("/visitor-check-time")
	public String searchParticularList(@RequestParam("checkIn") String checkIn,
	                                   @RequestParam("checkOut") String checkOut,
	                                   Model model) {
	    List<Visitor> visitorList = visitorService.particularDateDatas(checkIn.toString(), checkOut.toString());
	    model.addAttribute("visitorList", visitorList);
	    return "filter-list";
	}
	
	@GetMapping("/filter-list")
	public String getFilterListPage() {
		return "filter-list";
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
		} else e) {
	        e.printStackTrace();
	        redirectAttributes.addFlashAttribute("message", "Error during check out!");
	    }
	    return "redirect:/visitorlist";
	}
	



}
