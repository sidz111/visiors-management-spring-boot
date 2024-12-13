package com.vms.service.impl;

import java.util.List;
import java.util.Optional;

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
	public ;
	}
	
	@Override
	public VMSAdmin findVmasAdminById(Integer id) {
		Optional<VMSAdmin> vmsAdmin = vmsAdminRepository.findById(id);
		if(vmsAdmin.isEmpty()) {
			return null;
		}
		else {
			return vmsAdmin.get();
		}
	}

	@Override
	public List<VMSAdmin> findAllVMSAdmins() {
		return vmsAdminRepository.findAll();
	}

}
