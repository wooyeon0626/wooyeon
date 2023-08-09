package com.wooyeon.yeon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wooyeon.yeon.user.service.SmsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@SpringBootTest
class YeonApplicationTests {
    @Autowired
    private SmsService smsService;

    @Test
    void contextLoads() {
    }

    @Test
    void sendSms() throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        smsService.sendSms("01036418450");
    }

}
