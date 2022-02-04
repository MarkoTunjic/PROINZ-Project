package hr.fer.proinz.skilletcooking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hr.fer.proinz.skilletcooking.models.User;

//this annotation says that this class is a bean repository
@Repository
/**
 * A interface that extends the JpaRepository and it is used to access data from
 * the database. In the JpaRepository most of the methods are defined so the
 * only methods that should be implemented are the user specific methods for
 * example findUserByEmail
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * A method that returns a optional user with the given username from the
     * database
     *
     * @param username the username to be found
     * @return the optional user with the given username
     */
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
