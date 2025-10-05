package com.news.lettercrud.Security;


import com.news.lettercrud.Data.model.BaseAccount;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;


public class UserDetailsImpl implements UserDetails {

    private Long id ;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl() {
    }

    public UserDetailsImpl(Long id, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(BaseAccount baseAccount){
        String roleName = baseAccount.getRole().name();
        GrantedAuthority authority = new SimpleGrantedAuthority(roleName);
        return new UserDetailsImpl(
                baseAccount.getUserId(),
                baseAccount.getEmail(),
                baseAccount.getPassword(),
                Collections.singleton(authority));
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
}