package com.poly.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.poly.bean.User;
import com.poly.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository udao;

    @Autowired
    BCryptPasswordEncoder pe;

    public List<User> findActiveUsers() {
        return udao.findAll();
    }

    public Page<User> findActiveUsers(Pageable pageable) {
        return udao.findAll(pageable);
    }

    public User findById(String id) {
        return udao.findById(id).get();
    }

    public User update(User entity) {
        return udao.saveAndFlush(entity);
    }

    public User delete(User entity) {
        return udao.saveAndFlush(entity);
    }

    public List<String> getAllEmails() {
        return udao.getAllEmails();
    }

    public List<String> getAllPhones() {
        return udao.getAllPhones();
    }

    public Page<User> findUserByNamePage(String name, Pageable pageable) {
        return udao.findUserByNamePage(name, pageable);
    }

    public User create(User entity) {
        entity.setPassword(pe.encode(entity.getPassword()));
        return udao.saveAndFlush(entity);
    }

    public Boolean existsById(String id) {
        return udao.existsById(id);
    }

    public List<User> findAll() {
        return udao.findAll();
    }

    public Boolean existsByEmail(String email) {
        List<String> emails = udao.getAllEmails();
        for (String emailC : emails) {
            if (email.equalsIgnoreCase(emailC)) {
                return true;
            }

        }
        return false;
    }

    public Boolean existsByPhone(String phone) {
        List<String> phones = udao.getAllPhones();
        for (String phoneC : phones) {
            if (phone.equalsIgnoreCase(phoneC)) {
                return true;
            }

        }
        return false;
    }
    
    public long countTotalUsers() {
        return udao.count(); 
    }
}
