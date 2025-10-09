package com.news.lettercrud.Security;

import com.news.lettercrud.Data.model.BaseAccount;
import com.news.lettercrud.Repositories.BaseAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsImplService implements UserDetailsService {

    public final BaseAccountRepository baseAccountRepository;

    @Autowired
    public UserDetailsImplService(BaseAccountRepository baseAccountRepository) {
        this.baseAccountRepository = baseAccountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        BaseAccount account = baseAccountRepository.findByEmail(email);
        if(account==null) {
            throw new UsernameNotFoundException("Not Signed in with this email");
        }
        return UserDetailsImpl.build(account);
    }
}