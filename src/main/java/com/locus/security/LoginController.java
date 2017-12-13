package com.locus.security;

import com.nimbusds.jose.JOSEException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final UserService userService;
    private final TokenCreationService tokenCreationService;

    @Autowired
    public LoginController(UserService userService, TokenCreationService tokenCreationService) {
        this.userService = userService;
        this.tokenCreationService = tokenCreationService;
    }

    @CrossOrigin(exposedHeaders = "auth-token")
    @RequestMapping(value="login", method= RequestMethod.POST )
    public ResponseEntity<String> hello(@RequestParam String username, @RequestParam String password) {
        logger.info("In the login");
        HttpHeaders headers = new HttpHeaders();
        String authToken;
        try {

            final User user = userService.loginuser(username, password);

            if(user!= null) {
                authToken = tokenCreationService.getSignedToken(user);
                headers.set("auth-token", authToken);
                ResponseEntity<String> response = new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
                return response;
            } else {
                return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
            }
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | JOSEException e) {
            logger.error("Error while login: ", e);
            return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}