package com.vms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vms.entity.Visitor;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Long> {

	List<Visitor> findByName(String name);

	Visitor findByRandomId(Integer randomId);

	@Query("SELECT v FROM Visitor v WHERE v.checkIn BETWEEN :startTime AND :endTime")
	List<Visitor> findVisitorsByCheckInTimeRange(@Param("startTime") String startTime,
			@Param("endTime") String endTime);

}
