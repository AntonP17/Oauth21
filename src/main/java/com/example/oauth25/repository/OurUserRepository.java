package com.example.oauth25.repository;

import com.example.oauth25.model.OurUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OurUserRepository extends JpaRepository<OurUser, Long> {
    OurUser findByEmail(String email);
}
