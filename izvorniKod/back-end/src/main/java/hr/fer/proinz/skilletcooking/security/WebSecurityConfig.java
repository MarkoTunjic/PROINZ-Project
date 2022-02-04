package hr.fer.proinz.skilletcooking.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import hr.fer.proinz.skilletcooking.security.jwt.AuthEntryPoint;
import hr.fer.proinz.skilletcooking.security.jwt.AuthTokenFilter;
import hr.fer.proinz.skilletcooking.security.services.UserDetailsServiceImpl;

//this annotation says that this class is a bean and that it configures other beans too
@Configuration

// this annotation enables web security
@EnableWebSecurity

// this annotation enables filtering page permissions in controllers and not
// directly here
@EnableGlobalMethodSecurity(
        // securedEnabled = true,
        // jsr250Enabled = true,
        prePostEnabled = true)
/**
 * A class that defines how to react on http requests, defines what to do on
 * unauthorized requests and provides necesarry parameters for password encoding
 * and authentication
 */
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    // this annotation says that the attributes value is being injceted from the
    // spring container
    @Autowired
    /**
     * An attribute that contains the reference to the handler of unauthorized
     * requests
     */
    private AuthEntryPoint unauthorizedHandler;

    // this annotation says that the attributes value is being injceted from the
    // spring container
    @Autowired
    /**
     * An attribute that contains the reference to the UserDetailsService which is
     * used for communicating with the databse
     */
    private UserDetailsServiceImpl userDetailsService;

    // this annotation says that the method is a bean method
    @Bean
    /**
     * A method that provides a token filter
     *
     * @return the token filter
     */
    public AuthTokenFilter authenticationJWTTokenFilter() {
        return new AuthTokenFilter();
    }

    // this annotation says that the method is a bean method
    @Bean
    /**
     * A method that provides a password encoder
     *
     * @return a BCryptPasswordEncoder
     */
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * A method that configures the AuthenticationManager by providing user details
     * and a password encoder
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    // this annotation says that the method is a bean method
    @Bean

    /**
     * A method that creates a AuthenticationManagerBuilder and inserts it into the
     * spring cntainer becouse its a bean
     *
     * @return a AuthenticationManagerBuilder
     */
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * A method that defines how to handle http requests
     *
     * @param http a httpsecuirity object that handles http requests
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()// ensure that CORS will be handled before spring security kicks in
                .and().csrf().disable()// disable CSFR
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)// define what to do when we get a
                                                                                  // unauthorized request
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)// define that no
                                                                                                 // sessions are used
                                                                                                 // becouse we use JWT
                .and().authorizeRequests()// allow authentication
                .antMatchers("/api/auth/**")// create a link which we want to handle
                .permitAll()// allow everybody to go to the url
                .antMatchers("/api/recipes/{\\d+}").permitAll()
                .antMatchers("/api/recipes/all/{\\d+}").permitAll()
                .antMatchers("/api/recipes").permitAll()
                .antMatchers("/api/comments/**").permitAll()
                .antMatchers("/api/images/{\\d+}").permitAll()
                .antMatchers("/api/users/{\\d+}").permitAll()
                .antMatchers("/api/users").permitAll()
                .antMatchers("/api/ratings/{\\d+}").permitAll()
                .antMatchers("/api/recipes/author/{\\d+}").permitAll()
                .antMatchers(HttpMethod.POST, "/api/recipes").authenticated()
                .anyRequest().authenticated();// any other request has to be authenicated

        // add the JWT filter before the above defined filter
        http.addFilterBefore(authenticationJWTTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
