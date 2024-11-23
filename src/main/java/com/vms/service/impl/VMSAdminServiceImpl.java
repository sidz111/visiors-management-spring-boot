package com.vms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vms.entity.VMSAdmin;
import com.vms.repository.VMSAdminRepository;
import com.vms.service.VMSAdminService;

@Service
public class VMSAdminServiceImpl implements VMSAdminService{
	
	@Autowired
	VMSAdminRepository vmsAdminRepository;
	

	@Override
	public VMSAdmin addVMSAdmin(VMSAdmin vmsAdmin) {
		return this.vmsAdminRepository.save(vmsAdmin);
	}

	@Override
	public VMSAdmin updateVmsAdmin(String email, VMSAdmin vmsAdmin) {
		VMSAdmin admin = vmsAdminRepository.findByEmail(email);
		if(admin==null) {
			return null;
		}
		else {
			vmsAdminRepository.save(admin);
			return admin;
		}
	}

	@Override
	public String deleteVMSAdString(String email) {
		vmsAdminRepository.deleteByEmail(email);
		return "deleted";
	}

	@Override
	public VMSAdmin findVmasAdminByEmail(String email) {
		VMSAdmin vmsAdmin = vmsAdminRepository.findByEmail(email);
		return vmsAdmin;
	}

	@Override
	public List<VMSAdmin> findAllVMSAdmins() {
		return vmsAdminRepository.findAll();
	}

}
