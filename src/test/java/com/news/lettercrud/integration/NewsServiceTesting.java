package com.news.lettercrud.integration;

import com.news.lettercrud.Data.DTOs.CreateORUpdateNewsDTO;
import com.news.lettercrud.Data.Enum.Role;
import com.news.lettercrud.Data.model.NewsCategory;
import com.news.lettercrud.Data.model.NewsLetter;
import com.news.lettercrud.Data.model.UserAccount;
import com.news.lettercrud.Repositories.NewsCategoryRepository;
import com.news.lettercrud.Repositories.NewsRepository;
import com.news.lettercrud.Repositories.UserAccountRepository;
import com.news.lettercrud.Services.model.impl.NewsServiceImpl;
import com.news.lettercrud.Services.news.impl.AddNewsServiceImpl;
import com.news.lettercrud.Services.news.impl.UpdateNewsServiceImpl;
import com.news.lettercrud.exceptions.custom.NewsNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class NewsServiceTesting {

    @Autowired
    private NewsServiceImpl newsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private NewsCategoryRepository newsCategoryRepository;

    @Autowired
    private AddNewsServiceImpl addNewsService;

    @Autowired
    private UpdateNewsServiceImpl updateNewsServiceImpl;
    @Autowired
    private UserAccountRepository userAccountRepository;

    @BeforeEach
    public void clean(){
        newsRepository.deleteAll();
        newsCategoryRepository.deleteAll();
        userAccountRepository.deleteAll();
    }

    /**
     * Delete and Update
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
        assertThrows(NewsNotFoundException.class,()->
                {
                    newsService.findById(9999);
                }
        );
    }
}
