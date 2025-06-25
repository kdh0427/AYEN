package com.example.ayen.service;

import com.example.ayen.dto.User;
import com.example.ayen.dto.User.SocialType;
import com.example.ayen.dto.UserProfile;
import com.example.ayen.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.userinfo.*;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.user.*;
import org.springframework.security.oauth2.client.registration.*;
import org.springframework.stereotype.*;
import java.util.Map;
import java.util.Optional;

@Component
public class UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 원래 로직 호출
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        if ("kakao".equals(registrationId)) {
            saveKakaoUser(oAuth2User);
        }
        return oAuth2User; // 👈 원래 객체 그대로 반환
    }

    private void saveKakaoUser(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String email = (String) kakaoAccount.get("email");
        String name = (String) profile.get("nickname");
        String userToken = attributes.get("id").toString();

        // 유저 조회 후 저장
        userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User(
                    email,
                    userToken,
                    name,
                    SocialType.KAKAO,
                    1, 0, 0, 0
            );
            return userRepository.save(newUser);
        });
    }

    public ResponseEntity<?> getUserByEmail(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("User not found for email: " + email);
        }

        return ResponseEntity.ok(userOpt.get());
    }

    public ResponseEntity<?> getUserProfileByEmail(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found for email: " + email);
        }

        User user = userOpt.get();
        UserProfile dto = new UserProfile(user.getName(), user.getLevel());

        return ResponseEntity.ok(dto);
    }
}
