package com.news.lettercrud.integration;

import com.news.lettercrud.data.Enum.Role;
import com.news.lettercrud.data.model.BaseAccount;
import com.news.lettercrud.data.model.NewsCategory;
import com.news.lettercrud.data.model.NewsLetter;
import com.news.lettercrud.repository.NewsCategoryRepository;
import com.news.lettercrud.repository.NewsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * @author : Asnit Bakhati
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Use actual DB config
class NewsRepositoryIntegrationTest {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private NewsCategoryRepository newsCategoryRepository;

    @Autowired
    private TestEntityManager entityManager;


    /**
    Data Creation
     */
    @Test
    public void testSaveAndFindNewsLetter() {
        NewsCategory category = new NewsCategory();
        category.setCategoryName("Technology");
        category = newsCategoryRepository.save(category);

        BaseAccount author = new BaseAccount();
        author.setEmail("author@test.com");
        author.setPassword("password123");
        author.setRole(Role.ADMIN);
        entityManager.persist(author);

        NewsLetter news = new NewsLetter();
        news.setNewsHeadLine("Spring Boot Testing");
        news.setNewsBody("Learn integration testing...");
        news.setImageUrl("test.jpg");
        news.setAuthor(author);
        news.setNewsCategory(category);

        NewsLetter saved = newsRepository.save(news);
        entityManager.flush();
        entityManager.clear();

        Optional<NewsLetter> found = newsRepository.findById(saved.getNewsId());

        assertThat(found).isPresent();
        assertThat(found.get().getNewsHeadLine()).isEqualTo("Spring Boot Testing");
        assertThat(found.get().getAuthor().getEmail()).isEqualTo("author@test.com");
        assertThat(found.get().getNewsCategory().getCategoryName()).isEqualTo("Technology");
    }

    /**
     * Search by category testing
     */
    @Test
    public void testFindByCategory() {
        NewsCategory techCategory = new NewsCategory();
        techCategory.setCategoryName("Tech");
        techCategory = newsCategoryRepository.save(techCategory);



        BaseAccount author = new BaseAccount();
        author.setEmail("test@test.com");
        author.setPassword("pass");
        author.setRole(Role.ADMIN);
        entityManager.persist(author);

        // Create multiple news items
        NewsLetter news1 = new NewsLetter();
        news1.setNewsHeadLine("News 1");
        news1.setNewsBody("Body 1");
        news1.setAuthor(author);
        news1.setNewsCategory(techCategory);
        newsRepository.save(news1);

        NewsLetter news2 = new NewsLetter();
        news2.setNewsHeadLine("News 2");
        news2.setNewsBody("Body 2");
        news2.setAuthor(author);
        news2.setNewsCategory(techCategory);
        newsRepository.save(news2);

        entityManager.flush();

        List<NewsLetter> techNews = newsRepository.findByNewsCategory(techCategory);

        assert(techNews.size()==2);
    }

    /**
     * Deletion Testing
     */

    @Test
    public void testDeleteNews() {
        NewsCategory category = new NewsCategory();
        category.setCategoryName("Sports");
        category = newsCategoryRepository.save(category);

        BaseAccount author = new BaseAccount();
        author.setEmail("delete@test.com");
        author.setPassword("pass");
        author.setRole(Role.ADMIN);
        entityManager.persist(author);

        NewsLetter news = new NewsLetter();
        news.setNewsHeadLine("To be deleted");
        news.setNewsBody("This will be deleted");
        news.setAuthor(author);
        news.setNewsCategory(category);
        NewsLetter saved = newsRepository.save(news);

        int newsId = saved.getNewsId();
        entityManager.flush();


        newsRepository.deleteById(newsId);
        entityManager.flush();


        Optional<NewsLetter> deleted = newsRepository.findById(newsId);
        assertThat(deleted).isEmpty();
    }
}
