package kr.ict.mydream.member.login;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kr.ict.mydream.vo.MemberVO;

import java.util.Collections;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberLoginService memberLoginService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        MemberVO member = memberLoginService.findByEmail(email);
        if (member == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return new org.springframework.security.core.userdetails.User(
                member.getEmail(),
                member.getPassword(),
                getAuthorities(member.getRolecd())
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
