package com.news.lettercrud.service;

import com.news.lettercrud.Data.model.BaseAccount;
import com.news.lettercrud.Repositories.BaseAccountRepository;
import com.news.lettercrud.Services.model.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


public class UserServiceTest {

    @Mock
    private BaseAccountRepository baseAccountRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void testUserFinding() {

        BaseAccount fakeUser = new BaseAccount();
        fakeUser.setId(2L);
        fakeUser.setEmail("testuser@gmail.com");

        when(baseAccountRepository.findById(2L)).thenReturn(java.util.Optional.of(fakeUser));

        BaseAccount user = userServiceImpl.findById(2);

        assertNotNull(user);
        assertEquals(2, user.getId());
        assertEquals("testuser@gmail.com", user.getEmail());
    }

    @Test
    public void testExistMail(){
        String existMail = "testuser@gmail.com";
        String notExist = "notExits@gmail.com";
        BaseAccount fakeUser = new BaseAccount();
        fakeUser.setId(1L);
        fakeUser.setEmail(existMail);

        when(baseAccountRepository.findByEmail(existMail)).thenReturn(fakeUser);
        when(baseAccountRepository.existsByEmail(existMail)).thenReturn(true);
        when(baseAccountRepository.existsByEmail(notExist)).thenReturn(false);
                BaseAccount baseAccount = userServiceImpl.findByEmail(existMail);
        assertNotNull(baseAccount);
        assert(userServiceImpl.existsByEmail(baseAccount.getEmail()));
        assert(!userServiceImpl.existsByEmail(notExist));
    }
}
