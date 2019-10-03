package com.balicamp.webapp.security;

import org.acegisecurity.AuthenticationException;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.providers.dao.AbstractUserDetailsAuthenticationProvider;
import org.acegisecurity.userdetails.UserDetails;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class AdminSecurityProvider extends AbstractUserDetailsAuthenticationProvider implements ApplicationContextAware {

	@Override
	protected void additionalAuthenticationChecks(UserDetails arg0,UsernamePasswordAuthenticationToken arg1) throws AuthenticationException {
		
	}

	@Override
	protected UserDetails retrieveUser(String username,	UsernamePasswordAuthenticationToken arg1)	throws AuthenticationException {
        UserDetails loadedUser = getTemUserDetail(username);       
        return loadedUser;
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0)	throws BeansException {
		
	}
	
	private UserDetails getTemUserDetail(final String username){
		return new UserDetails() {
			
			@Override
			public boolean isEnabled() {
				return true;
			}
			
			@Override
			public boolean isCredentialsNonExpired() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isAccountNonLocked() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isAccountNonExpired() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public String getUsername() {
				return username;
			}
			
			@Override
			public String getPassword() {
				return "balicamp123";
			}
			
			@Override
			public GrantedAuthority[] getAuthorities() {
				GrantedAuthority aut = new GrantedAuthority() {					
					@Override
					public String getAuthority() {
						return "SUPERUSER";
					}
				};
				
				GrantedAuthority[] arrAut = new GrantedAuthority[1];
				arrAut[0]= aut;
				return arrAut;
			}
		};
	}

}
