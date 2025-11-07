package com.news.lettercrud.security;

import com.news.lettercrud.data.model.BaseAccount;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author : Asnit Bakhati
 */

@Setter
public class UserDetailsImpl implements UserDetails {

    @Getter
    private Long id ;
    @Getter
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(long id, String username, String password, List<String> roles) {
    }

    public UserDetailsImpl(Long id, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(BaseAccount baseAccount){

        List<SimpleGrantedAuthority> authorities = baseAccount.getUserRoles().stream().
                map(roleTable -> new SimpleGrantedAuthority(roleTable.getRole().name())).collect(Collectors.toList());

        return new UserDetailsImpl(
                baseAccount.getId(),
                baseAccount.getEmail(),
                baseAccount.getPassword(),
                authorities);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

}