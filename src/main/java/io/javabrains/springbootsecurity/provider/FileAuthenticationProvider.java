package io.javabrains.springbootsecurity.provider;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class FileAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsService userDetailsService;

    @Autowired
    public FileAuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        if (username == null || username.isEmpty()) {
            throw new BadCredentialsException("Username can't be null or empty.");
        }

        if (password == null || password.isEmpty()) {
            throw new BadCredentialsException("Password can't be null or empty.");
        }

        UserDetails user    = userDetailsService.loadUserByUsername(username);

//        boolean     isValidPassword = UserDao.checkEncrypted(password, user.getPassword(), username);

        if(user == null){
            throw new BadCredentialsException("No valida User Present");
        }

        if (!user.getPassword().equals(password)) {
            throw new BadCredentialsException("Wrong password");
        }

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        authentication = new UsernamePasswordAuthenticationToken(username, password, authorities);

        return authentication;

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
