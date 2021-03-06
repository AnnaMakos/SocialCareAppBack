package com.annamakos.socialcare.api.repository;

import com.annamakos.socialcare.api.model.RoleName;
import com.annamakos.socialcare.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Set<User> findAllByInstitutionId(int id);

    List<User> findAllByRolesName(RoleName name);


}
