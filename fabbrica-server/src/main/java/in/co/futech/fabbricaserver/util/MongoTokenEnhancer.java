package in.co.futech.fabbricaserver.util;

import in.co.futech.fabbricaserver.service.UserDetail;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

public class MongoTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        final Map<String, Object> additionalInfo = new HashMap<>();

        additionalInfo.put("id", userDetail.getUser().getId());
        additionalInfo.put("name", userDetail.getUser().getName());
        additionalInfo.put("username", userDetail.getUser().getUsername());
        additionalInfo.put("roles", userDetail.getUser().getRoles());

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

        return accessToken;
    }

}
