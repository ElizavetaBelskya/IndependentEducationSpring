package ru.kpfu.itis.belskaya.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.belskaya.models.Account;
import ru.kpfu.itis.belskaya.models.Student;
import ru.kpfu.itis.belskaya.models.Tutor;
import ru.kpfu.itis.belskaya.models.User;
import ru.kpfu.itis.belskaya.repositories.AccountRepository;
import ru.kpfu.itis.belskaya.repositories.StudentRepository;
import ru.kpfu.itis.belskaya.repositories.TutorRepository;

import java.util.Optional;

public class AccountService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(final String emailAndRole) throws UsernameNotFoundException {
        Optional<Account> user = accountRepository.findAccountByEmailAndRole(emailAndRole);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UsernameNotFoundException("User with such email doesn't exist");
        }
    }

    public boolean registerUser(Account account, User user) throws DuplicateKeyException {
        // Я мучилась с абстракциями долго, все, хватит, просто switch case! тем более ролей, даже потенциально, немного
        switch (account.getRole()) {
            case TUTOR: {
                if (tutorRepository.findAccountByEmailOrPhone(user.getEmail(), user.getPhone()).isPresent()) {
                    throw new DuplicateKeyException("User already registered");
                } else {
                    account.setPasswordHash(passwordEncoder.encode(account.getPassword()));
                    Account addedAccount = accountRepository.save(account);
                    user.setAccount(addedAccount);
                    Tutor tutor = (Tutor) user;
                    Tutor createdUser = tutorRepository.save(tutor);
                    if (createdUser != null) {
                        return true;
                    }
                }
                break;
            }
            case STUDENT: {
                if (studentRepository.findAccountByEmailOrPhone(user.getEmail(), user.getPhone()).isPresent()) {
                    throw new DuplicateKeyException("User already registered");
                } else {
                    account.setPasswordHash(passwordEncoder.encode(account.getPassword()));
                    Account addedAccount = accountRepository.save(account);
                    user.setAccount(addedAccount);
                    Student student = (Student) user;
                    Student createdUser = studentRepository.save(student);
                    if (createdUser != null) {
                        return true;
                    }
                }
                break;
            }
        }

        return false;

    }



}
