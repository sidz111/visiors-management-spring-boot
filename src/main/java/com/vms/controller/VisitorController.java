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

import com.vms.entity.Visitor;
import com.vms.service.VisitorService;

@Controller
public class VisitorController {

	@Autowired
	VisitorService visitorService;

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
	        @RequestParam("visitDate") String visitDate,
	        @RequestParam("inTime") String inTime,
	        @RequestParam("outTime") String outTime,
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
	        visitor.setVisitDate(visitDate);
	        visitor.setInTime(inTime);
	        visitor.setOutTime(outTime);
	        visitor.setImg(fileName);
	        visitorService.addVisitor(visitor);

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
			@RequestParam("email") String email, @RequestParam("visitDate") String visitDate,
			@RequestParam("inTime") String inTime,
			@RequestParam("outTime") String outTime 
	) {
		Visitor visitor = visitorService.getVisitorById(visitorId);

		if (visitor != null) {
			visitor.setName(name);
			visitor.setContactNumber(contactNumber);
			visitor.setEmail(email);
			visitor.setVisitDate(visitDate);
			visitor.setInTime(inTime);
			visitor.setOutTime(outTime);
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

}
