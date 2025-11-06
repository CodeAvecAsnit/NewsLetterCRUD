package com.news.lettercrud.integration;

import com.news.lettercrud.data.DTOs.CreateORUpdateNewsDTO;
import com.news.lettercrud.data.Enum.Role;
import com.news.lettercrud.data.model.NewsCategory;
import com.news.lettercrud.data.model.NewsLetter;
import com.news.lettercrud.data.model.UserAccount;
import com.news.lettercrud.repository.NewsCategoryRepository;
import com.news.lettercrud.repository.UserAccountRepository;
import com.news.lettercrud.service.model.impl.NewsServiceImpl;
import com.news.lettercrud.service.news.impl.AddNewsServiceImpl;
import com.news.lettercrud.service.news.impl.UpdateNewsServiceImpl;
import com.news.lettercrud.exception.custom.NewsNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class NewsServiceTest {

    @Autowired
    private NewsServiceImpl newsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private NewsCategoryRepository newsCategoryRepository;

    @Autowired
    private AddNewsServiceImpl addNewsService;

    @Autowired
    private UpdateNewsServiceImpl updateNewsServiceImpl;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void clean(){
        entityManager.clear();
        entityManager.flush();
    }


    /**
     * Create and Update
     */
    @Test
    public void createNews(){
        NewsCategory category = new NewsCategory();
        category.setCategoryName("Business");
        NewsCategory category1 = newsCategoryRepository.save(category);

        UserAccount userAccount = new UserAccount();
        userAccount.setRole(Role.ADMIN);
        userAccount.setEmail("testuser@gmail.com");
        String pass = "test123";
        userAccount.setPassword(passwordEncoder.encode(pass));
        userAccount.setRealPass(pass);
        userAccount.setUsername("SomethingWill");
        userAccount.setWritings(new HashSet<>());
        userAccountRepository.save(userAccount);

       CreateORUpdateNewsDTO data = new CreateORUpdateNewsDTO();
       data.setHeadline("Something will happen");
       data.setNewsBody("Something will happen song by Beriloz");
       data.setImageURL("www.spotify.com/Beriloz");
       data.setNewsCategory("Business");

       NewsLetter added = addNewsService.postNews(data,userAccount.getId());
       assertNotNull(added);
       assertEquals(added.getNewsHeadLine(),data.getHeadline());
       assertEquals(category1,added.getNewsCategory());

       String newHeadline = "Headline has been changed";
       data.setHeadline(newHeadline);
       NewsLetter updated = updateNewsServiceImpl.updateNews(added.getNewsId(),data,userAccount.getId());
       assertNotNull(updated);
       assertEquals(updated.getNewsHeadLine(),newHeadline);
       assertEquals(added.getNewsBody(),updated.getNewsBody());

    }

    /**
     * Test for Exception
     */
    @Test
    public void testException(){
        assertThrows(NewsNotFoundException.class,()-> {
                    newsService.findById(9999);
                }
        );
    }
}
