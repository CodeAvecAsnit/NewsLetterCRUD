package com.news.lettercrud.unit;


import com.news.lettercrud.data.DTOs.CreateORUpdateNewsDTO;
import com.news.lettercrud.data.model.BaseAccount;
import com.news.lettercrud.data.model.NewsCategory;
import com.news.lettercrud.data.model.NewsLetter;
import com.news.lettercrud.repository.NewsCategoryRepository;
import com.news.lettercrud.repository.NewsRepository;
import com.news.lettercrud.service.model.CategoryService;
import com.news.lettercrud.service.model.NewsService;
import com.news.lettercrud.service.news.impl.UpdateNewsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


import java.util.Optional;




@ExtendWith(MockitoExtension.class)
public class UpdateNewsTest {

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private NewsCategoryRepository newsCategoryRepository;

    @Mock
    private NewsService newsService;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private UpdateNewsServiceImpl updateNewsService;

    @Test
    public void testUpdate() {
        BaseAccount testUser = new BaseAccount();
        testUser.setId(1L);
        testUser.setEmail("testuser@gmail.com");

        NewsCategory category = new NewsCategory();
        category.setCategoryId(1);
        category.setCategoryName("Tech");

        NewsLetter existing = new NewsLetter();
        existing.setNewsId(1);
        existing.setNewsHeadLine("Old Headline");
        existing.setNewsBody("Old body");
        existing.setImageUrl("oldurl.com");
        existing.setAuthor(testUser);
        existing.setNewsCategory(category);

        CreateORUpdateNewsDTO newsDTO = new CreateORUpdateNewsDTO();
        newsDTO.setHeadline("New Application leaving");
        newsDTO.setImageURL("www.ImageURL.com");
        newsDTO.setNewsBody("The body has been changed...");
        newsDTO.setNewsCategory("Tech");


        when(newsService.findById(1)).thenReturn(existing);
        when(newsCategoryRepository.findByCategoryName("Tech")).thenReturn(Optional.of(category));


        Optional<NewsCategory> newsCategory = newsCategoryRepository.findByCategoryName("Tech");
        NewsLetter update = CreateORUpdateNewsDTO.buildNewsLetter(newsDTO,testUser,newsCategory.get());

        when(newsService.postNews(any(NewsLetter.class))).thenReturn(update);
        NewsLetter updated = updateNewsService.updateNews(1, newsDTO, 1L);

        assertNotNull(updated);
        assertEquals("New Application leaving", updated.getNewsHeadLine());
        assertEquals("www.ImageURL.com", updated.getImageUrl());
        assertEquals("The body has been changed...", updated.getNewsBody());
        assertEquals("Tech", updated.getNewsCategory().getCategoryName());

        verify(newsService, times(1)).findById(1);
    }
}