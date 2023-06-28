package com.example.database;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsertypelinkRepository extends JpaRepository<Usertypelink, String> {
    List<Usertypelink> findByUsername(String username);
}