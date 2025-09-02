package com.news.lettercrud.Services;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import com.news.lettercrud.Data.Enum.Role;
import com.news.lettercrud.Data.model.BaseAccount;
import com.news.lettercrud.Data.model.CompanyAccount;
import com.news.lettercrud.Data.model.UserAccount;
import com.news.lettercrud.Repositories.BaseAccountRepository;
import com.news.lettercrud.Repositories.CompanyRepository;
import com.news.lettercrud.Repositories.UserAccountRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;
import java.util.concurrent.TimeUnit;

@Service
@EnableAsync
public class VerificationService{

    private final Logger logger = LoggerFactory.getLogger(VerificationService.class);
    private Cache<String, LoginAttempt> codes;
    private Cache<String, BaseAccount> accounts;

    @Autowired
    private BaseAccountRepository baseAccountRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsImplService userDetailsImplService;

    @PostConstruct
    public void init(){
        this.codes = Caffeine.newBuilder().
                expireAfterWrite(5, TimeUnit.MINUTES).
                maximumSize(1000).build();
        this.accounts =Caffeine.newBuilder().
                expireAfterWrite(5,TimeUnit.MINUTES).
                maximumSize(1000).build();
    }

    @Async
    public void sendMail(BaseAccount baseAccount) {
        String email = baseAccount.getEmail();
        LoginAttempt attempt = codes.getIfPresent(email);
        if(attempt!=null){
            return;
        }
        int code = sendEmailToWebClient(email);
        if(code != -1){
            codes.put(email,new LoginAttempt(code));
            accounts.put(email,baseAccount);
        }
    }


    public int verify(MailVerificationDTO verify){
        String email = verify.getEmail();
        LoginAttempt validAttempt = codes.getIfPresent(email);
        if(validAttempt==null){
            return 5;
        }
        int returnCode = validAttempt.equals(verify.getCode());
        if(returnCode==1){
            codes.invalidate(email);
            BaseAccount account = accounts.getIfPresent(email);
            if(account==null){
                return 6;
            }
            accounts.invalidate(email);
            account.setPassword(passwordEncoder.encode(account.getPassword()));
            if(account.getRole()== Role.COMPANY_ROLE){
                CompanyAccount companyAccount=(CompanyAccount) account;
                companyRepository.save(companyAccount);
            }else{
                UserAccount userAccount = (UserAccount) account;
                userAccountRepository.save(userAccount);
            }
            return 1;
        }
        else return returnCode;
    }


    private Integer sendEmailToWebClient(String email) {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8081")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        MailRequestDTO request = new MailRequestDTO(email);

        return webClient.post()
                .uri("/mindgame")
                .bodyValue(request)
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return response.bodyToMono(String.class)
                                .map(body -> {
                                    if ("-1".equals(body)) {
                                        System.out.println("Server returned -1");
                                        return -1;
                                    } else if (body.matches("\\d{6}")) {
                                        return Integer.parseInt(body);
                                    } else {
                                        logger.error("Unexpected body format: {} " , body);
                                        return -1;
                                    }
                                });
                    } else {
                        logger.error("HTTP error: " + response.statusCode());
                        return Mono.just(-1);
                    }
                })
                .onErrorResume(e -> {
                    logger.error("Request failed: {} " , e.getMessage());
                    return Mono.just(-1);
                })
                .block();
    }


    public String generateJwt(String email){
        UserDetails userDetails = userDetailsImplService.loadUserByUsername(email);
        return jwtUtils.generateJwtTokens(userDetails);
    }

}