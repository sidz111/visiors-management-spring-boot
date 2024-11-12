package com.vms.service.impl;

import com.vms.entity.Staff;
import com.vms.repository.StaffRepository;
import com.vms.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffServiceImpl implements StaffService {

	@Autowired
    private final StaffRepository staffRepository;

    public StaffServiceImpl(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    public Staff addStaff(Staff staff) {
        return staffRepository.save(staff);
    }

    @Override
    public Staff updateStaff(Long staffId, Staff staff) {
        if (staffRepository.existsById(staffId)) {
            staff.setStaffId(staffId); // Set ID to the existing staff ID
            return staffRepository.save(staff);
        }
        throw new RuntimeException("Staff not found with id: " + staffId);
    }


    @Override
    public Staff getStaffById(Long staffId) {
        Optional<Staff> list = staffRepository.findById(staffId);
        if(list.isEmpty()) {
        	return null;
        }
        else {
        	return list.get();
        }
    }

    @Override
    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    @Override
    public void deleteStaffById(Long staffId) {
        staffRepository.deleteById(staffId);
    }

	@Override
	public List<Staff> searchStaffByDepartment(String department) {
		return staffRepository.findByDepartment(department);
	}

	@Override
	public List<Staff> getStaffByName(String name) {
		return staffRepository.findByName(name);
	}
}
