package com.shisanyama.shisanyamaWebApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyService {
    @Autowired
    private MyEntityRepository myEntityRepository;

    public List<User> getAllEntities() {
        return myEntityRepository.findAll();
    }
}
