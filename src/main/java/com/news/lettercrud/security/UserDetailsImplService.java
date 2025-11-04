package com.news.lettercrud.security;

import com.news.lettercrud.data.model.BaseAccount;
import com.news.lettercrud.repository.BaseAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * @author : Asnit Bakhati
 */

@Service
public class UserDetailsImplService implements UserDetailsService {

    public final BaseAccountRepository baseAccountRepository;

    @Autowired
    public UserDetailsImplService(BaseAccountRepository baseAccountRepository) {
        this.baseAccountRepository = baseAccountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<BaseAccount> account = baseAccountRepository.findByEmail(email);
        if(account.isEmpty()) {
            throw new UsernameNotFoundException("Not Signed in with this email");
        }
        return UserDetailsImpl.build(account.get());
    }
}