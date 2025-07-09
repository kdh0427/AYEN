package com.example.ayen.service;

import com.example.ayen.dto.entity.User;
import com.example.ayen.dto.entity.User.SocialType;
import com.example.ayen.dto.response.UserProfile;
import com.example.ayen.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.userinfo.*;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.user.*;
import org.springframework.stereotype.*;

import java.util.List;
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
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        if ("kakao".equals(registrationId)) {
            saveKakaoUser(oAuth2User);
        } else if ("google".equals(registrationId)) {
            saveGoogleUser(oAuth2User);
        }

        return oAuth2User;
    }

    private void saveGoogleUser(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 구글은 이메일, 이름이 보통 아래 키에 있음
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String userToken = (String) attributes.get("sub"); // 구글 고유 ID

        // 유저가 없으면 저장
        userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User(
                    email,
                    userToken,
                    name,
                    SocialType.GOOGLE,  // SocialType enum에 GOOGLE 추가 필요
                    1, 0, 0, 0
            );
            return userRepository.save(newUser);
        });
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

    public List<User> getTop10UsersByAchievementCount() {
        return userRepository.findTop10ByOrderByAchievementCountDesc();
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Long findIdByEmail(String email) {return userRepository.findIdByEmail(email);}

    public void settlementOfExperiencePoints(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found for email: " + email));

        int uexp = user.getExp();
        int level = uexp / 1500 + 1;
        int exp =  uexp % 1500;

        user.setExp(exp);
        user.setLevel(level);
        userRepository.save(user);
    }
}
