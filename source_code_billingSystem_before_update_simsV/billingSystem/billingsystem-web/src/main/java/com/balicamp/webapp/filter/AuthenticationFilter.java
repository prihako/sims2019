package com.balicamp.webapp.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.concurrent.ConcurrentLoginException;
import org.acegisecurity.concurrent.SessionRegistry;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.ui.AbstractProcessingFilter;

public class AuthenticationFilter extends AbstractProcessingFilter{

	private SessionRegistry sessionRegistry;

	public void setSessionRegistry(SessionRegistry sessionRegistry) {
		this.sessionRegistry = sessionRegistry;
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, Authentication authResult)
			throws IOException {
		super.successfulAuthentication(request, response, authResult);
		
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest arg0)
			throws AuthenticationException {
		
		String username = "admin1";
		String password = "balicamp123";
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
		
		
		Authentication authentication = null;
		
		try {
			//FIXME: harusnya di lakukan authenticate dari manager
			authentication = getDefaultAuth(username);
			
			//FIXME: harusnya di lakukan
			//authentication = getAuthenticationManager().authenticate(authRequest);
		} catch (AuthenticationException e) {
			if (e instanceof ConcurrentLoginException){
				throw e;
			}

			//FIXME: tambahan filter untuk user sudah di blok
/*			if (e instanceof UserBlockException)
				throw e;*/

			//FIXME: loging untuk login yang gagal 
/*			User user = userManager.findByUserName(username);
			if (user != null) {
				userManager.logLoginFail(user, new Date(), request.getRemoteHost());
			}
*/
			throw e;
		}
				
		return authentication;
	}

	@Override
	public String getDefaultFilterProcessesUrl() {
		return "/j_acegi_security_check";
	}

	private Authentication getDefaultAuth(final String username){
		return new Authentication() {
			
			@Override
			public String getName() {
				return username;
			}
			
			@Override
			public void setAuthenticated(boolean arg0) throws IllegalArgumentException {
				
			}
			
			@Override
			public boolean isAuthenticated() {
				return true;
			}
			
			@Override
			public Object getPrincipal() {
				return null;
			}
			
			@Override
			public Object getDetails() {
				return null;
			}
			
			@Override
			public Object getCredentials() {
				return null;
			}
			
			@Override
			public GrantedAuthority[] getAuthorities() {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}
}
