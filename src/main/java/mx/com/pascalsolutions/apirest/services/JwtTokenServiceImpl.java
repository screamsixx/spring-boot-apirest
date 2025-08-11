package mx.com.pascalsolutions.apirest.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import mx.com.pascalsolutions.apirest.interfaces.JwtTokenServiceI;

@Service
public class JwtTokenServiceImpl implements JwtTokenServiceI {

	@Value("${security.static.secret}")
	private String secretKey;

	 @Async
	 @Override
  public CompletableFuture<String> getJWTToken(String username) {
      // Logic for generating the token (can potentially involve database interactions)
      List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
      String token = Jwts.builder().setId("IdPascalSolutionsJwt").setSubject(username)
              .claim("authorities",
                      grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
              // .setIssuedAt(new Date(System.currentTimeMillis()))
              // .setExpiration(new Date(System.currentTimeMillis() + 600000))
              .signWith(SignatureAlgorithm.HS512, secretKey.getBytes()).compact();
      return CompletableFuture.completedFuture("Bearer " + token);
  }
}