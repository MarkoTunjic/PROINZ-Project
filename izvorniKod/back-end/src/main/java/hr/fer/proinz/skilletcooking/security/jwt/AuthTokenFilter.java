package hr.fer.proinz.skilletcooking.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import hr.fer.proinz.skilletcooking.security.services.UserDetailsServiceImpl;

/** A class that filters all invalid tokens */
public class AuthTokenFilter extends OncePerRequestFilter {

    // this annotation says that the attributes value is being injceted from the
    // spring container
    @Autowired
    private JWTUtils jwtUtils;

    // this annotation says that the attributes value is being injceted from the
    // spring container
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     * A method that parses a jwt token aka removes the header
     *
     * @param request the HTTP request
     *
     * @return the JWT token in string format or null if not a real JWT token
     */
    private String parseJwt(HttpServletRequest request) {
        // gets the header specified for authorization
        String headerAuth = request.getHeader("Authorization");

        // check if the header is really a JWT token
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer"))
            // return the JWT token
            return headerAuth.substring(7, headerAuth.length());

        // if it was nt a real JWT token return null
        return null;
    }

    /**
     * A method that filters all invalid JWT tokens aka doesn't create a
     * authentication request
     *
     * @param request     the HTTP request
     * @param response    the HTTP response
     * @param filterChain a collection of all filters that have to be excecuted
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // try to parse the JWT token
            String jwt = parseJwt(request);

            // if the parsing went well and the token is valid build the authentication
            // request
            if (jwt != null && jwtUtils.validateJWTToken(jwt)) {
                // get the username from the token
                String username = jwtUtils.getUsernameFromJWTToken(jwt);

                // get the userDetails bound to the username
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // create a authentication request token
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                // set the deatils of the authentication
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // bind the authentication request token to the security context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            System.out.println("Cannot set user authentication: {" + e.getMessage() + "}");
        }
        filterChain.doFilter(request, response);
    }

}
