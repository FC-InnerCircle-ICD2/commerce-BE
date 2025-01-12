package com.emotionalcart.order.infrastructure.configuration;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
@Import(TestContainerConfiguration.class)
@EnableEncryptableProperties
class JasyptConfigTest {

    @Autowired
    private StringEncryptor stringEncryptor;

    @Test
    @DisplayName("jasypt 테스트")
    void test_case_1() {
        // given
        // when
        String test = "jasypt";
        // then
        String encrypt = stringEncryptor.encrypt(test);
        System.out.println("Encrypt :: {} " + encrypt);
        Assertions.assertEquals(test, stringEncryptor.decrypt(encrypt));
    }

}