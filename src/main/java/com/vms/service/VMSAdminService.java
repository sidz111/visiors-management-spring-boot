package com.vms.service;

import java.util.List;

import com.vms.entity.VMSAdmin;

public interface VMSAdminService {
	
	VMSAdmin addVMSAdmin(VMSAdmin vmsAdmin);			//insert
	VMSAdmin updateVmsAdmin(String email, VMSAdmin vmsAdmin);	//update
	String deleteVMSAdString(String email);				//delete
	VMSAdmin findVmasAdminByEmail(String email);        //find by email
	List<VMSAdmin> findAllVMSAdmins();					//find all admins
	
	

}
