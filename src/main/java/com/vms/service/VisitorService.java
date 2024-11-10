package com.vms.service;

import java.util.List;

import com.vms.entity.Visitor;

public interface VisitorService {

	Visitor addVisitor(Visitor visitor);        //add

	Visitor updateVisitor(Long visitorId, Visitor visitor);        //update

	Visitor getVisitorById(Long visitorId);         // find by id

	List<Visitor> getAllVisitors();        // find all

	List<Visitor> searchVisitorsByName(String name);  // find by name

	void deleteVisitorById(Long visitorId);      // delete by id
}
