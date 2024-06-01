package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // получить список пользователей из БД
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //показать пользователя по ID

    @Override
    public User showUser(int id) {
        return userRepository.findById(id).get();
    }

    //сохранить пользователя
    @Transactional
    @Override
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    //удалить пользователя по ID
    @Transactional
    @Override
    public void deleteUserById(int id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        }
    }

    // Редактирование пользователя
    @Transactional
    @Override
    public void edit(int id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (user.getPassword().isEmpty()) {
            user.setPassword(existingUser.getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        user.setId(existingUser.getId());
        userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
        return user.get();
    }
}
