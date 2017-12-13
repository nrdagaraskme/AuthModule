package com.locus.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by naveen on 09/12/17.
 */

@Service
public class TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    private static JWSVerifier verifier;

    @Autowired
    private Environment environment;

    @PostConstruct
    protected void init() throws InvalidKeySpecException, NoSuchAlgorithmException {
        verifier = new MACVerifier(environment.getRequiredProperty("com.locus.biz.privatekey"));
    }

    public User extractAndValidateUsernameFromToken(String token) throws JOSEException, InvalidKeySpecException, NoSuchAlgorithmException, ParseException {
        SignedJWT signedJWT;
        try {
            signedJWT = SignedJWT.parse(token);
        } catch (ParseException e) {
            return null;
        }

        if(signedJWT.verify(verifier)) {
            final ReadOnlyJWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
            if(jwtClaimsSet.getExpirationTime().getTime() < System.currentTimeMillis()) {
                logger.debug("Token expired");
                return null;
            }
            String userGuid;
            String userRolesCsv;
            String userID;
            String fullName;
            String userToken = "";
            List<GrantedAuthority> userRoles = new ArrayList<GrantedAuthority>();
            try {
                userGuid = (String) signedJWT.getJWTClaimsSet().getClaim("sub");
                userRolesCsv = (String) signedJWT.getJWTClaimsSet().getClaim("access_roles");
                userID = signedJWT.getJWTClaimsSet().getStringClaim("userID");
                userToken = signedJWT.getJWTClaimsSet().getStringClaim("userToken");
                fullName = signedJWT.getJWTClaimsSet().getStringClaim("fullName");
                String[] userRolesList = StringUtils.commaDelimitedListToStringArray(userRolesCsv);
                for(int i=0; i<userRolesList.length; i++) {
                    userRoles.add(new SimpleGrantedAuthority(userRolesList[i]));
                }
                User user = new User(userGuid, "", userID, userRoles, fullName , userToken);
                return user;
            } catch (ParseException e) {
                logger.error("Error parsing claim set for authentication token {}. Stacktrace: {}", token, e);
                return null;
            }
        } else {
            return null;
        }
    }


}
