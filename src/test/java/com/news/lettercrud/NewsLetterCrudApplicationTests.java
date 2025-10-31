package com.news.lettercrud;

import com.news.lettercrud.Data.model.BaseAccount;
import com.news.lettercrud.Data.model.CompanyAccount;
import com.news.lettercrud.Data.model.UserAccount;
import org.apache.catalina.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NewsLetterCrudApplicationTests {

    @Test
    void contextLoads() {
        BaseAccount baseAccount = new BaseAccount();
        UserAccount userAccount= new UserAccount();
        CompanyAccount companyAccount = new CompanyAccount();

    }

    BaseAccount transform(UserAccount userAccount){
        return UserAccount;
    }

    @Test
    void  findUserAccount(User user){
        int x = 19;
        Integer m = Integer.valueOf(String.valueOf(x));
        findUserAccount(user);


    }

}
