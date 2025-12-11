package com.optify.repository;

import com.optify.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUserName(String userName);
    // CORREGIDO: Debe coincidir con el campo 'mail' en la entidad
    Optional<User> findByMail(String mail);
}