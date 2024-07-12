package com.project.JewelryMS.config;

import com.project.JewelryMS.entity.Account;
import com.project.JewelryMS.service.JWTservice;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;



@Component
public class Filter extends OncePerRequestFilter {
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Autowired
    JWTservice jwTservice;
    private final List<String> AUTH_PERMISSION = List.of(
            "/swagger-ui/**",
            "/api-docs/**",
            "/favicon",
            "/swagger-resources/**",
            "/api/account/register",
            "/api/account/login",
            "/api/account/reset",
            "/Info/GoldPrice",
            "/vnpay/**",
            "/code/**",
            "/api/category/**",
            "/productsell/**",
            "/api/account/loginGG",
            "/api/productSell/readall",
            "/api/productSell/read/{id}",
            "/api/productSell/**",
            "/images/uploadByPath",
            "/api/productSell/create",
            "/api/productBuy/**",
            "/api/order/**",
            "/api/Dashboard/**",
            "/ws/**"


    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String uri = request.getRequestURI();
        if (isPermitted(uri)) {
            // yêu cầu truy cập 1 api => ai cũng truy cập đc

            filterChain.doFilter(request, response); // cho phép truy cập dô controller
        } else {

            String token = getToken(request);
            if (token == null) {
                resolver.resolveException(request, response, null, new AuthException("Empty token!"));
                return;
            }

            Account account;
            try {
                // từ token tìm ra thằng đó là ai
                account = jwTservice.extractAccount(token);

            } catch (ExpiredJwtException expiredJwtException) {
                // token het han
                resolver.resolveException(request, response, null, new AuthException("Expired Token!"));
                return;
            } catch (MalformedJwtException malformedJwtException) {
                resolver.resolveException(request, response, null, new AuthException("Invalid Token!"));
                return;
            }
            // token dung

            UsernamePasswordAuthenticationToken
                    authenToken =
                    new UsernamePasswordAuthenticationToken(account, token, account.getAuthorities());
            authenToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            System.out.println(authenToken.getAuthorities().toString());
            SecurityContextHolder.getContext().setAuthentication(authenToken);
            // token ok, cho vao`
            filterChain.doFilter(request, response);
        }

    }
    public boolean isPermitted(String uri){
        AntPathMatcher pathMatcher = new AntPathMatcher();
        return AUTH_PERMISSION.stream().anyMatch(pattern ->pathMatcher.match(pattern,uri));
    }
    public String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.substring(7);
    }
}
