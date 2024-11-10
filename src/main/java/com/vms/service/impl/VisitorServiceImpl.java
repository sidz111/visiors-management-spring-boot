package com.vms.service.impl;

import com.vms.entity.Visitor;
import com.vms.repository.VisitorRepository;
import com.vms.service.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VisitorServiceImpl implements VisitorService {

	@Autowired
    private final VisitorRepository visitorRepository;

    public VisitorServiceImpl(VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    @Override
    public Visitor addVisitor(Visitor visitor) {
        return visitorRepository.save(visitor);
    }

    @Override
    public Visitor updateVisitor(Long visitorId, Visitor visitor) {
        if (visitorRepository.existsById(visitorId)) {
            visitor.setVisitorId(visitorId);
            return visitorRepository.save(visitor);
        }
        throw new RuntimeException("Visitor not found with id: " + visitorId);
    }

    @Override
    public Visitor getVisitorById(Long visitorId) {
        Optional<Visitor> list = visitorRepository.findById(visitorId);
        if(list.isEmpty()) {
        	return null;
        }
        else {
        	return list.get();
        }
    }

    @Override
    public List<Visitor> getAllVisitors() {
        return visitorRepository.findAll();
    }

    @Override
    public List<Visitor> searchVisitorsByName(String name) {
        return visitorRepository.findByName(name);
    }

    @Override
    public void deleteVisitorById(Long visitorId) {
        visitorRepository.deleteById(visitorId);
    }
}
