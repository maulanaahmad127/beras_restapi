package com.bezkoder.spring.login.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bezkoder.spring.login.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);

  @Query(
  value = "SELECT *  from user_roles ur join sppb_muser u on (ur.user_id = u.id) join roles r on (r.id = ur.role_id) where r.name ='ROLE_PETANI'",
  nativeQuery = true)
  Iterable<User> findByRoles();

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);

  boolean existsById(Long id);
}
