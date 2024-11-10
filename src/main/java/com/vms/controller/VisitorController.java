package com.vms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	public String addVisitor(Visitor visitor) {
		visitorService.addVisitor(visitor);
		return "visitors/add-visitor";
	}
	
//	@GetMapping("/visitorlist")
//	public String getVisitorList(Model model) {
//		model.addAttribute("list", visitorService.getAllVisitors());
//		return "visitors/visitor-list";
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
        }
        else {
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
	
}
