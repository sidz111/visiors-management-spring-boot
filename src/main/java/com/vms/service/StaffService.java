package com.vms.service;

import java.util.List;

import com.vms.entity.Staff;

public interface StaffService {

	Staff addStaff(Staff staff);		//add

	Staff updateStaff(Long staffId, Staff staff);    //update

	Staff getStaffById(Long staffId);     //find By Id

	List<Staff> getAllStaff();       //findAll

	List<Staff> searchStaffByDepartment(String department);    //findByDepartment

	void deleteStaffById(Long staffId);           //delete by id
	
	List<Staff> getStaffByName(String name);        // find by name
}
