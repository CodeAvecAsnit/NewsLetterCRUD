package com.news.lettercrud;

import com.news.lettercrud.Data.Enum.Role;
import com.news.lettercrud.Data.model.UserAccount;
import com.news.lettercrud.Repositories.UserAccountRepository;
import io.swagger.v3.oas.models.links.Link;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@SpringBootApplication
public class NewsLetterCrudApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewsLetterCrudApplication.class, args);
    }

}
