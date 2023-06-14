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
  value = "SELECT * from users u join user_roles ur on (ur.user_id = u.id) where ur.role_id =3",
  nativeQuery = true)
  Iterable<User> findByRoles();

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);
}
