package spring.myproject.common.filter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import spring.myproject.common.JwtSubject;
import spring.myproject.common.security.CustomUserDetail;
import spring.myproject.common.validator.JwtValidator;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtValidator jwtValidator;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if(token != null){
            if(!token.substring(0,7).equals("Bearer ")){
                throw new AuthenticationServiceException("Not Bearer form!!");
            }
            String jwtToken = token.substring(7);
            Claims claims = jwtValidator.validateToken(jwtToken);
            JwtSubject jwtSubject = extractJwtSubject(claims);
            CustomUserDetail customUserDetail = createUserDetail(jwtSubject);
            SecurityContextHolder.getContext().setAuthentication(createAuthentication(customUserDetail));
        }
        filterChain.doFilter(request, response);
    }

    private JwtSubject extractJwtSubject(Claims claims) {
        String username = claims.getSubject();
        String role = (String) claims.get("role");
        Long userId = ((Integer) claims.get("id")).longValue();
        return JwtSubject.of(userId, role, username);
    }

    private CustomUserDetail createUserDetail(JwtSubject jwtSubject) {
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + jwtSubject.getRole()));
        return new CustomUserDetail(jwtSubject, authorities);
    }

    private Authentication createAuthentication(CustomUserDetail userDetail) {
        return new UsernamePasswordAuthenticationToken(userDetail, "", userDetail.getAuthorities());
    }
}
