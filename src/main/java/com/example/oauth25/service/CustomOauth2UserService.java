package com.example.oauth25.service;

import com.example.oauth25.model.OurUser;
import com.example.oauth25.repository.OurUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
public class CustomOauth2UserService extends DefaultOAuth2UserService {


    private final OurUserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(CustomOauth2UserService.class);

    @Autowired
    public CustomOauth2UserService(OurUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        Optional<Map<String, Object>> attributes = Optional.ofNullable(oAuth2User.getAttributes());

        String name = attributes
                .flatMap(attrs -> Optional.ofNullable(attrs.get("name")))
                .map(Object::toString)
                .orElse(null);
        String email = attributes
                .flatMap(attrs -> Optional.ofNullable(attrs.get("email")))
                .map(Object::toString)
                .orElse(null);
        String id = attributes
                .flatMap(attrs -> Optional.ofNullable(attrs.get("id")))
                .map(Object::toString)
                .orElse(null);

        OurUser user = userRepository.findByEmail(email);

        if (user == null) {
            user = new OurUser();
            user.setProviderId(id);
            user.setName(name);
            user.setEmail(email);
            if (email == null) {
                user.setRole("USER");
            } else {
                user.setRole(email.endsWith("@example.com") ? "ADMIN" : "USER");
            }

            userRepository.save(user);
        }


        return oAuth2User;
    }

}
