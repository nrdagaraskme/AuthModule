package com.locus.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        try {
            UserData user = userRepository.findByUserid(username);

            boolean enabled = true;
            boolean accountNonExpired = true;
            boolean credentialsNonExpired = true;
            boolean accountNonLocked = true;
            List<GrantedAuthority> roles = new ArrayList<>();
            roles.add(new SimpleGrantedAuthority("USER"));
            return new CustomUserDetails(user.getLogin(),
                    user.getPassword(), enabled, accountNonExpired,
                    credentialsNonExpired, accountNonLocked,
                    roles);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static class SimpleGrantedAuthorityComparator implements
            Comparator<SimpleGrantedAuthority> {

        @Override
        public int compare(SimpleGrantedAuthority o1, SimpleGrantedAuthority o2) {
            return o1.equals(o2) ? 0 : -1;
        }
    }


   /*  * Retrieves a collection of {@link GrantedAuthority} based on a list of
     * roles
     *
     * @param roles
     *            the assigned roles of the user
     * @return a collection of {@link GrantedAuthority}
     */

    public Collection<? extends GrantedAuthority> getAuthorities(Set<Role> roles) {

        Set<SimpleGrantedAuthority> authList = new TreeSet<>(
                new SimpleGrantedAuthorityComparator());

        for (Role role : roles) {
            authList.addAll(getGrantedAuthorities(role));
        }

        return authList;
    }

    /**
     * Wraps a {@link Role} role to {@link SimpleGrantedAuthority} objects
     *
     * @param roles
     *            {@link String} of roles
     * @return list of granted authorities
     */
    public static Set<SimpleGrantedAuthority> getGrantedAuthorities(Role role) {

        Set<SimpleGrantedAuthority> authorities = new HashSet<SimpleGrantedAuthority>();

        Set<Permission> rolePermissions = role.getPermissions();
        for (Permission permission : rolePermissions) {
            authorities.add(new SimpleGrantedAuthority(permission.getName()));
        }

        return authorities;
    }
}
