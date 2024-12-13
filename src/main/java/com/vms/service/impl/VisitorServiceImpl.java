package com.vms.service.impl;

import com.vms.entity.Visitor;
import com.vms.repository.VisitorRepository;
import com.vms.service.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class VisitorServiceImpl implements VisitorService {

	@Autowired
    private final VisitorRepository visitorRepository;

    public VisitorServiceImpl(VisitorRepositoryreturn visitorRepository.save(visitor);
        }
        throw new RuntimeException("Visitor not found with id: " + visitorId);
    }

    @Override


	@Override
	public List<Visitor> particularDateDatas(String checkInTime, String checkOutTime) {
	    return visitorRepository.findVisitorsByCheckInTimeRange(checkInTime, checkOutTime);
	}

}
