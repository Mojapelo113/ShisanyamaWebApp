package com.shisanyama.shisanyamaWebApp;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MyEntityRepository extends JpaRepository<User, Long> {
   User findUserByEmail(String email);
}