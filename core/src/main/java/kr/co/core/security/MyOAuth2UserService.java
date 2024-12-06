package kr.co.core.security;

import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.core.security.dto.OAuthUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	@Transactional(readOnly = true)
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) {
		String provider = userRequest.getClientRegistration().getRegistrationId();
		Map<String, Object> attributes = new DefaultOAuth2UserService().loadUser(userRequest).getAttributes();
		return new OAuthUserDetails(provider, attributes);
	}
}
