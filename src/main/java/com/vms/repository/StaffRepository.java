package com.vms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vms.entity.Staff;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

	List<Staff> findByDepartment(String department);
	List<Staff> findByName(String name);
}
