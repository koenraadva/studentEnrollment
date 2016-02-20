package studentEnrollment.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// see also http://shruubi.com/2014/12/03/spring-boot-hibernate-and-spring-security-a-step-in-the-right-direction-for-java/

@Service
@Qualifier("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
    private StudentRepository studentRepository;

    @Transactional(readOnly=true)
    @Override
    public UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {

    	Student student = studentRepository.findByUserName(username);
        List<GrantedAuthority> authorities = buildUserAuthority(student.getRoles());

        return buildUserForAuthentication(student, authorities);

    }

    private User buildUserForAuthentication(Student student,
                                            List<GrantedAuthority> authorities) {
        return new User(student.getUserName(), student.getPassword(), authorities);
    }

    private List<GrantedAuthority> buildUserAuthority(Set<UserRole> userRoles) {

        Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();

        // Build user's authorities
        for (UserRole userRole : userRoles) {
            setAuths.add(new SimpleGrantedAuthority(userRole.getRoleName()));
        }

        return new ArrayList<GrantedAuthority>(setAuths);
    }

}
