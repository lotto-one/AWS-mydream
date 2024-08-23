package kr.co.ict.yourdream.login.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



import java.util.Collections;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private ConsultLoginService consultLoginService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ConsultVO99 consult = consultLoginService.findByEmail(email);
        if (consult == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return new org.springframework.security.core.userdetails.User(
            consult.getEmail(),
            consult.getPassword(),
                getAuthorities(consult.getRolecd())
        );
    }

    private List<SimpleGrantedAuthority> getAuthorities(String rolecd) {
        try {
            Role role = Role.fromCode(rolecd);
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
        } catch (IllegalArgumentException e) {
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")); 
        }
    }
}
