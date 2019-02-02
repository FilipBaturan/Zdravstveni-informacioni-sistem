package zis.rs.zis.service.security;

import org.springframework.security.core.GrantedAuthority;
import zis.rs.zis.domain.entities.Korisnik;
import zis.rs.zis.domain.security.UserDetailsImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Factory for creating instance of {@link UserDetailsImpl}.
 */
public class UserDetailsFactory {

    private UserDetailsFactory() {
    }

    /**
     * Creates UserDetailsImpl from a user.
     *
     * @param user user model
     * @return UserDetailsImpl
     */
    public static UserDetailsImpl create(Korisnik user) {
        Collection<? extends GrantedAuthority> authorities;
        List<GrantedAuthority> auth = new ArrayList<GrantedAuthority>();
        //auth.add(user.getAuthorityType());
        try {
            //auth.add(new SimpleGrantedAuthority(user.getAuthorityType().toString()));
            //authorities = auth; //.map(a -> new SimpleGrantedAuthority(a.toString())).collect(Collectors.toList());
            //authorities = user.getAuthorityType().stream().map(a -> new SimpleGrantedAuthority(a.toString())).collect(Collectors.toList());
            authorities = null;
        } catch (Exception e) {
            authorities = null;
        }

        //AuthorityType auth = AuthorityType.REGISTERED_USER;

        return new UserDetailsImpl(
//                user.getId(),
//                user.getUsername(),
//                user.getPassword(),
//                user.getEmail(),
//                user.getTelephone(),
//                //user.getLastPasswordReset(),
//                auth
        );
    }
}

