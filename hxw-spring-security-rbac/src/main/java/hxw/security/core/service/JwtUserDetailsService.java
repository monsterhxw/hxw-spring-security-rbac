package hxw.security.core.service;

import hxw.security.api.domain.Permission;
import hxw.security.api.domain.Role;
import hxw.security.api.domain.User;
import hxw.security.api.repository.PermissionRepository;
import hxw.security.api.repository.UserRepository;
import hxw.security.common.exception.EntityNotFoundException;
import hxw.security.common.utils.ValidationUtil;
import hxw.security.core.security.JwtUser;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = null;
        if (ValidationUtil.isEmail(username)) {
            user = userRepository.findByEmail(username);
        } else {
            user = userRepository.findByUsername(username);
        }

        if (user == null) {
            throw new EntityNotFoundException(User.class, "name", username);
        }
        return createUserDetails(user);
    }

    private UserDetails createUserDetails(User user) {
        return new JwtUser(
            user.getId(),
            user.getUsername(),
            user.getPassword(),
            user.getAvatar(),
            user.getEmail(),
            user.getEnabled(),
            user.getCreateTime(),
            user.getLastPasswordResetTime(),
            mapToGrantedAuthorities(user.getRoles(), permissionRepository)
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(Set<Role> roles, PermissionRepository permissionRepository) {

        Set<Permission> permissions = new HashSet<>();
        for (Role role : roles) {
            Set<Role> roleSet = new HashSet<>();
            roleSet.add(role);
            permissions.addAll(permissionRepository.findByRoles(roleSet));
        }

        return permissions.stream()
            .map(permission -> new SimpleGrantedAuthority("ROLE" + permission.getName()))
            .collect(Collectors.toList());
    }

}
