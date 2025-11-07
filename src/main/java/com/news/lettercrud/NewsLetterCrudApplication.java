package com.news.lettercrud;

import com.news.lettercrud.data.enumeration.Role;
import com.news.lettercrud.data.model.BaseAccount;
import com.news.lettercrud.data.model.RoleTable;
import com.news.lettercrud.repository.BaseAccountRepository;
import com.news.lettercrud.repository.RoleTableRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class NewsLetterCrudApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewsLetterCrudApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(RoleTableRepository roleTableRepository, BaseAccountRepository baseAccountRepository){
        return args -> {
            List<RoleTable> roleTableList = new ArrayList<>();

            RoleTable admin = new RoleTable();
            admin.setRole(Role.ADMIN);
            roleTableList.add(admin);


            RoleTable company = new RoleTable();
            company.setRole(Role.COMPANY);
            roleTableList.add(company);

            RoleTable superAdmin = new RoleTable();
            superAdmin.setRole(Role.SUPER_ADMIN);
            roleTableList.add(superAdmin);

            RoleTable user = new RoleTable();
            user.setRole(Role.USER);
            roleTableList.add(user);

            List<RoleTable> savedRoles = roleTableRepository.saveAll(roleTableList);

            List<BaseAccount> baseAccounts = baseAccountRepository.findAll();
            for (BaseAccount baseAccount : baseAccounts) {
                List<RoleTable> accountRoles = new ArrayList<>();
                Optional<RoleTable> userTable = savedRoles.stream().filter(person -> person.getRole().equals(baseAccount.getRole())).findFirst();
                userTable.ifPresent(accountRoles::add);
                baseAccount.setUserRoles(accountRoles);
            }

            baseAccountRepository.saveAll(baseAccounts);
        };
    }
}
