package com.news.lettercrud.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.news.lettercrud.Data.DTOs.CreateORUpdateNewsDTO;
import com.news.lettercrud.Data.Enum.Role;
import com.news.lettercrud.Data.model.BaseAccount;
import com.news.lettercrud.Data.model.NewsCategory;
import com.news.lettercrud.Data.model.NewsLetter;
import com.news.lettercrud.Data.model.UserAccount;
import com.news.lettercrud.Repositories.BaseAccountRepository;
import com.news.lettercrud.Repositories.NewsCategoryRepository;
import com.news.lettercrud.Repositories.NewsRepository;
import com.news.lettercrud.Repositories.UserAccountRepository;
import com.news.lettercrud.Security.JwtUtils;
import com.news.lettercrud.Security.UserDetailsImpl;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

// Correct imports
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class NewsControllerIntegrationTesting {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private NewsCategoryRepository newsCategoryRepository;

    @Autowired
    private EntityManager entityManager;

    private String authToken;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private BaseAccount classAccount;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @BeforeEach
    void setUp() throws Exception {
        newsRepository.deleteAll();
        newsCategoryRepository.deleteAll();
        classAccount = createUser();
        authToken = "Bearer " + getAuthToken(classAccount);
    }

    @Test
    public void testCreateNews_Success() throws Exception {
        NewsCategory category = new NewsCategory();
        category.setCategoryName("Tech");
        newsCategoryRepository.save(category);

        CreateORUpdateNewsDTO dto = new CreateORUpdateNewsDTO();
        dto.setHeadline("API Test News");
        dto.setNewsBody("Testing REST API");
        dto.setImageURL("api.jpg");
        dto.setNewsCategory("Tech");

        mockMvc.perform(post("/api/v1/add/news")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.newsHeadLine").value("API Test News"))
                .andExpect(jsonPath("$.newsBody").value("Testing REST API"))
                .andExpect(jsonPath("$.newsCategory.categoryName").value("Tech"));
    }

    @Test
    public void testGetNewsById_Success() throws Exception {
        NewsCategory category = new NewsCategory();
        category.setCategoryName("Sports");
        category = newsCategoryRepository.save(category);


        NewsLetter news = new NewsLetter();
        news.setNewsHeadLine("Get Test");
        news.setNewsBody("Get Body");
        news.setAuthor(classAccount);
        news.setNewsCategory(category);
        news = newsRepository.save(news);
        entityManager.flush();

        mockMvc.perform(get("/api/v1/news/" + news.getNewsId())
                        .header("Authorization", authToken))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateNews_Success() throws Exception {
        // Setup
        NewsCategory category = new NewsCategory();
        category.setCategoryName("Tech");
        category = newsCategoryRepository.save(category);

        BaseAccount author = new BaseAccount();
        author.setId(1L);
        author.setEmail("update@test.com");
        author.setPassword("pass");
        entityManager.persist(author);

        NewsLetter news = new NewsLetter();
        news.setNewsHeadLine("Old Headline");
        news.setNewsBody("Old Body");
        news.setAuthor(author);
        news.setNewsCategory(category);
        news = newsRepository.save(news);
        entityManager.flush();

        // Update DTO
        CreateORUpdateNewsDTO updateDto = new CreateORUpdateNewsDTO();
        updateDto.setHeadline("Updated Headline");
        updateDto.setNewsBody("Updated Body");
        updateDto.setImageURL("updated.jpg");
        updateDto.setNewsCategory("Tech");

        // Test PUT request
        mockMvc.perform(put("/api/news/" + news.getNewsId())
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.newsHeadLine").value("Updated Headline"));
    }

    @Test
    public void testDeleteNews_Unauthorized() throws Exception {
        mockMvc.perform(delete("/api/news/1"))
                .andExpect(status().isUnauthorized());
    }

    private String getAuthToken(BaseAccount saved) {
        UserDetailsImpl userDetails = UserDetailsImpl.build(saved);
        return jwtUtils.generateJwtTokens(userDetails);
    }

    private BaseAccount createUser(){
        UserAccount baseAccount = new UserAccount();
        baseAccount.setEmail("radomuser@gmail.com");
        baseAccount.setPassword(passwordEncoder.encode("test123"));
        baseAccount.setRealPass("test123");
        baseAccount.setRole(Role.ADMIN);
        baseAccount.setUsername("Author");
        return (BaseAccount)userAccountRepository.save(baseAccount);
    }

}