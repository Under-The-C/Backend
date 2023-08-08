package LikeLion.UnderTheCBackend.config;

import LikeLion.UnderTheCBackend.Entity.Buyer;
import LikeLion.UnderTheCBackend.Repository.BuyerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class OAuth2BuyerCustomService extends DefaultOAuth2UserService {

    private final BuyerRepository buyerRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest); // ❶ 요청을 바탕으로 유저 정보를 담은 객체 반환
        saveOrUpdate(user);

        return user;
    }

    // ❷ 유저가 있으면 업데이트, 없으면 유저 생성
    private Buyer saveOrUpdate(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        Buyer buyer = buyerRepository.findByEmail(email)
                .map(entity -> entity.update(name))
                .orElse(Buyer.builder()
                        .email(email)
                        .nickname(name)
                        .build());

        return buyerRepository.save(buyer);
    }
}
