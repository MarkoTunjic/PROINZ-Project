package hr.fer.proinz.skilletcooking.repository;

import hr.fer.proinz.skilletcooking.models.ERole;
import hr.fer.proinz.skilletcooking.models.Role;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//this annotation says that this class is a bean repository
@Repository
/**
 * A interface that extends the JpaRepository and it is used to access data from
 * the database. In the JpaRepository most of the methods are defined so the
 * only methods that should be implemented are the Role specific methods for
 * example existsById
 */
public interface RoleRepository extends JpaRepository<Role, Integer> {
    /**
     * A method that returns a optional role with the given role name
     *
     * @param name the role name to be found in the database
     * @return the optional role with the given name
     */
    Optional<Role> findByRoleName(ERole name);
}
