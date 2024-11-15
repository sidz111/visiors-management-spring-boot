package com.vms.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.vms.entity.VMSAdmin;
import com.vms.repository.VMSAdminRepository;

@Component  // for allowing to make object use @Component annotation
public class CustomeUserDetailsService implements UserDetailsService{

	@Autowired
	VMSAdminRepository vmsAdminRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		VMSAdmin vms = vmsAdminRepository.findByEmail(email);
		if(vms==null) {
			throw new UsernameNotFoundException("Admin Not found");
		}
		else {
			return new CustomUser(vms);
		}
	}

}