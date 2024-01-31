package com.wooyeon.yeon.common.fcm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.wooyeon.yeon.common.fcm.dto.FcmDto;
import com.wooyeon.yeon.user.domain.User;
import com.wooyeon.yeon.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FcmService {
    private final ObjectMapper objectMapper;

    public String getAccessToken() throws IOException {
        String firebaseConfigPath = "fcm-secret.json";
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    /**
     * 알림 푸쉬를 보내는 역할을 하는 메서드
     *
     * @Request FcmDto.Request : 푸쉬 알림을 받을 클라이언트 앱의 식별 토큰
     */
    public void sendMessageTo(FcmDto.Request request) throws IOException {

        String message = makeMessage(
                request.getTargetToken(),
                request.getTitle(),
                request.getBody(),
                request.getEmail(),
                request.getDescription());

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));

        Request apiRequest = new Request.Builder()
                .url("https://fcm.googleapis.com/v1/projects/wooyeon-fcm/messages:send")
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();
        try {
            client.newCall(apiRequest).execute();
            log.info("알림을 성공적으로 전송했습니다.");
        } catch (IOException e) {
            log.info("알림 보내기를 실패하였습니다.");

        }
    }

    /**
     * makeMessage : 알림 파라미터들을 FCM이 요구하는 body 형태로 가공한다.
     *
     * @param targetToken : firebase token
     * @param title       : 알림 제목
     * @param body        : 알림 내용
     * @return
     */
    public String makeMessage(
            String targetToken, String title, String body, String email, String description
    ) throws JsonProcessingException {

        FcmDto fcmMessage = FcmDto.builder()
                .message(
                        FcmDto.Message.builder()
                                .token(targetToken)
                                .notification(
                                        FcmDto.Notification.builder()
                                                .title(title)
                                                .body(body)
                                                .build()
                                )
                                .data(
                                        FcmDto.Data.builder()
                                                .name(email)
                                                .description(description)
                                                .build()
                                )
                                .build()
                )
                .validateOnly(false)
                .build();

        return objectMapper.writeValueAsString(fcmMessage);
    }
}
