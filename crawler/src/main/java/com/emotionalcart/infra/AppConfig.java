package com.emotionalcart.infra;

import com.emotionalcart.CoupangCrawler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppConfig implements ApplicationRunner {

    private final CoupangCrawler coupangCrawler;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        coupangCrawler.crawl();
    }

}
