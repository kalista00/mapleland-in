package kr.co.core.security.dto;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import kr.co.core.model.User;
import lombok.Data;

@Data
public class OAuthUserDetails implements OAuth2User {

	private User user;
	private String provider;
	private Map<String, Object> attributes;
	private Collection<? extends GrantedAuthority> authorities;

	public OAuthUserDetails(String provider, Map<String, Object> attributes) {
		this.provider = provider;
		this.attributes = attributes;
	}

	public OAuthUserDetails(User user) {
		this.user = user;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getName() {
		return provider;
	}

}
