package com.vms.configuration;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.vms.entity.VMSAdmin;

public class CustomUser implements UserDetails {

	private VMSAdmin vms;

	public CustomUser(VMSAdmin vms) {
		super();
		this.vms = vms;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(vms.getRole());
		return Arrays.asList(authority);
	}

	@Override
	public String getPassword() {
		return vms.getPassword();
	}

	@Override
	public String getUsername() {
		return vms.getEmail();
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

}
