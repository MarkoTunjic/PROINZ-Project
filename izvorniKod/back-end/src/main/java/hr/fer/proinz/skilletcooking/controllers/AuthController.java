package hr.fer.proinz.skilletcooking.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hr.fer.proinz.skilletcooking.models.ERole;
import hr.fer.proinz.skilletcooking.models.User;
import hr.fer.proinz.skilletcooking.payload.Request.LoginRequest;
import hr.fer.proinz.skilletcooking.payload.Request.RegisterRequest;
import hr.fer.proinz.skilletcooking.payload.Response.JWTResponse;
import hr.fer.proinz.skilletcooking.payload.Response.MessageResponse;
import hr.fer.proinz.skilletcooking.repository.RoleRepository;
import hr.fer.proinz.skilletcooking.repository.UserRepository;
import hr.fer.proinz.skilletcooking.security.jwt.JWTUtils;
import hr.fer.proinz.skilletcooking.security.services.UserDetailsImpl;

//this annotation says that this class will be a rest controller and creates a Bean object that will
//be saved in the spring container
@RestController

// this annotation defines that we recevie requests from all sources
@CrossOrigin(origins = "*", maxAge = 3600)

// this annotation says that this controller controlls only requests with the
// /api/auth destination
@RequestMapping("/api/auth")

/**
 * A class that contains methods that define what to do when a HTTP request
 * comes to the server
 */
public class AuthController {
    // this annotation says that the user repository will be automatically injected
    // from the spring container
    @Autowired

    /**
     * An attribute that is a bean authentication manager and holds the userdetails
     * and authenticates the creditentials
     */
    AuthenticationManager authenticationManager;

    // this annotation says that the user repository will be automatically injected
    // from the spring container
    @Autowired
    /** An attribute that we use to excecute querys for the table users */
    UserRepository userRepository;

    // this annotation says that the user repository will be automatically injected
    // from the spring container
    @Autowired
    /** An attribute that we use to excetue querys for the roles */
    RoleRepository roleRepository;

    // this annotation says that the user repository will be automatically injected
    // from the spring container
    @Autowired
    /**
     * An attribute that holds the password encoder that we use for encoding the
     * password
     */
    PasswordEncoder passwordEncoder;

    // this annotation says that the user repository will be automatically injected
    // from the spring container
    @Autowired
    /**
     * An attribute that holds the jwt utils aka the functions for handling JWT
     * tokens
     */
    JWTUtils jwtUtils;

    // this annotation says that this function handles HTTP POST requests on
    // /api/auth/login
    @PostMapping("/login")
    /**
     * A method that retuns a HTTP response entity based on the given loginRequest.
     * A bad credidentials exception is thrown in case of bad login request
     *
     * @param loginRequest the login creditentials
     *
     * @return the http response entity
     */
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = null;
        // check if email was given
        if (loginRequest.getUsername().contains("@")) {

            // check if user exists
            boolean exists = userRepository.existsByEmail(loginRequest.getUsername());
            if (exists) {
                // get user
                User user = userRepository.findByEmail(loginRequest.getUsername()).get();

                // send username and password for authentication
                authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(user.getUsername(),
                                loginRequest.getPassword()));
            }
            // send it anyway
            else
                authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                                loginRequest.getPassword()));
        }
        // if username was given not email
        else {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                            loginRequest.getPassword()));
        }
        // authenticate the token and throw exception if not valid credidentials

        // set the currently authenticated user
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // create new token
        String jwt = jwtUtils.generateJWTToken(authentication);

        // get user details from currently authenticated user so we can get the rest of
        // the users information
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // get the users roles
        List<String> roles = userDetails.getAuthorities().stream().map(authority -> authority.getAuthority())
                .collect(Collectors.toList());

        // send the http response with the token inside it
        return ResponseEntity.ok(
                new JWTResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
    }

    // this annotation says that this function handles HTTP POST requests on
    // /api/auth/register
    @PostMapping("/register")
    /**
     * A method that sends a HTTP response entity based on the given register
     * request
     *
     * @param registerRequest the register data
     *
     * @return username already taken message if the username is taken or email is
     *         already in use if email is in use or registered successfully if
     *         everything is ok
     */
    public ResponseEntity<?> signUserUp(@Valid @RequestBody RegisterRequest registerRequest) {
        // check if username already exists
        if (userRepository.existsByUsername(registerRequest.getUsername()))
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));

        // check if email already exists
        if (userRepository.existsByEmail(registerRequest.getEmail()))
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));

        // create new user
        User user = new User(registerRequest.getFirstName(), registerRequest.getLastName(), registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword()), registerRequest.getDateOfBirth(),
                registerRequest.getUsername());

        // set the role of the user
        user.setUserRole(roleRepository.findByRoleName(ERole.USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found.")));

        // save the user to the database
        userRepository.save(user);

        // send HTTP ok message
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
