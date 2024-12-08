package kr.co.core.security.component;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import kr.co.core.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenProvider {
	private static final byte[] SIGNING_KEY = "MYKEY"
			.getBytes();

	private static final long TWO_HOURS_IN_MINUTES = 120L;
	private static final String TOKEN_TYPE = "JWT";
	private static final String TOKEN_ISSUER = "tk-api";
	private static final String TOKEN_AUDIENCE = "tk-app";

	public String getToken(User user) {
		return Jwts.builder().header().add("typ", TOKEN_TYPE).and()
				.signWith(Keys.hmacShaKeyFor(SIGNING_KEY), Jwts.SIG.HS512)
				.expiration(Date.from(ZonedDateTime.now().plusMinutes(TWO_HOURS_IN_MINUTES).toInstant()))
				.issuedAt(Date.from(ZonedDateTime.now().toInstant())).id(UUID.randomUUID().toString())
				.issuer(TOKEN_ISSUER).audience().add(TOKEN_AUDIENCE).and().subject(user.getId().toString()).compact();
	}

	public Optional<Jws<Claims>> validateTokenAndGetJws(String token) {
		try {
			Jws<Claims> jws = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(SIGNING_KEY)).build()
					.parseSignedClaims(token);
			return Optional.of(jws);

		} catch (JwtException | IllegalArgumentException e) {
			log.error("## token not invalid - {}", e);
			return Optional.empty();
		}
	}
}
