package com.locus.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by naveen on 09/12/17.
 */

@Service
public class TokenCreationService {

    @Autowired
    private Environment env;

    private static JWSSigner signer;
    private String domainName;

    @PostConstruct
    protected void init() throws InvalidKeySpecException, NoSuchAlgorithmException {
        signer = new MACSigner(env.getRequiredProperty("com.locus.biz.privatekey"));
        domainName = env.getProperty("appdomainname", "localhost");
    }

    public String getSignedToken(User user) throws JOSEException, InvalidKeySpecException, NoSuchAlgorithmException {
        JWTClaimsSet claimsSet = new JWTClaimsSet();
        claimsSet.setSubject(user.getUsername());
        claimsSet.setIssueTime(new Date());
        claimsSet.setExpirationTime(new Date(System.currentTimeMillis() + (1000 * 60 * 10)));
        claimsSet.setIssuer(domainName);
        claimsSet.setCustomClaim("userID", user.getUserID());
        claimsSet.setCustomClaim("userToken", user.getUserToken());
        claimsSet.setCustomClaim("fullName", user.getFullName());
        List<String> userRoles = new ArrayList<String>();
        if(user.getAuthorities() != null && user.getAuthorities().size() > 0) {
            for(GrantedAuthority role: user.getAuthorities()) {
                userRoles.add(role.getAuthority());
            }
        }
        claimsSet.setCustomClaim("access_roles", StringUtils.collectionToDelimitedString(userRoles, ","));

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

        // Apply the RAS Signature
        signedJWT.sign(signer);

        return(signedJWT.serialize());
    }
}
