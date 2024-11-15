package com.vms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vms.entity.VMSAdmin;

@Repository
public interface VMSAdminRepository extends JpaRepository<VMSAdmin, Integer>{

	public VMSAdmin findByEmail(String email);
}
