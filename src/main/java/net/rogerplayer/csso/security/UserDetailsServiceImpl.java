package net.rogerplayer.csso.security;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.rogerplayer.csso.domain.CSSOUser;
import net.rogerplayer.csso.services.CSSOUserService;

@Service(value = "userService")
public class UserDetailsServiceImpl implements UserDetailsService {
        
        @Autowired
        private CSSOUserService cssoUserService;
        
        @Transactional
        public UserDetails loadUserByUsername(String username) {
        	CSSOUser user = cssoUserService.findByUserName(username);
            if(user == null){
                throw new UsernameNotFoundException("Invalid username or password.");
            }
            return new org.springframework.security.core.userdetails.User(user.getNome(), user.getPassword(), getAuthority());
        }
        
        private List<SimpleGrantedAuthority> getAuthority() {
    		return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    	}
}	