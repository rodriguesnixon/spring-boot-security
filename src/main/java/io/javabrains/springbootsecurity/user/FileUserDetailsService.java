package io.javabrains.springbootsecurity.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class FileUserDetailsService implements UserDetailsService {

    @Autowired
    UserProperties userProperties;

    @Override
    public UserDetails loadUserByUsername(String username) {

        String userValue = userProperties.getConfigValue(username);
        String   password = "";
        String   role     = "";

        if(userValue == null) {
            return null;
        }

        String[] dataArr  = userValue.split("::");

        if (dataArr != null && dataArr.length == 2) {
            role     = dataArr[0];
            password = dataArr[1];
        } else {
            return null;
        }

        List<GrantedAuthority> grantedAuths = new ArrayList<>();

        if (StringUtils.hasText(role)) {
            grantedAuths.add(new SimpleGrantedAuthority(role));
        } else {
            return null;
        }

        User userDetails = new User(username, password, grantedAuths);

        return userDetails;

    }

}
