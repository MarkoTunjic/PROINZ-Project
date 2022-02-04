package hr.fer.proinz.skilletcooking.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hr.fer.proinz.skilletcooking.models.User;
import hr.fer.proinz.skilletcooking.repository.UserRepository;

//this annotation says that this is a service bean
@Service

/**
 * A class that provides sevices for user details for example getting user
 * details based on one unique parameter
 */
public class UserDetailsServiceImpl implements UserDetailsService {

    // this annotation says that the attributes value is being injceted from the
    // spring container
    @Autowired

    /**
     * An attribute that contains the reference to the user repository used for
     * communicating with the database
     */
    private UserRepository repository;

    /**
     * A method that returns the userDetails for the given username by finding the
     * user in the database and createing user details for that user and throws a
     * {@link UsernameNotFoundException} if the username did not exist in the
     * database
     *
     * @param username the username of the userDetails
     *
     * @return the user details bound to the given username
     *
     * @throws UsernameNotFoundException if the username did not exist in the
     *                                   database
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return UserDetailsImpl.build(user);
    }

}
