package com.vms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vms.entity.Visitor;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Long> {

	List<Visitor> findByName(String name);
}
