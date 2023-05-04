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
import ru.kpfu.itis.belskaya.repositories.StudentRepository;
import ru.kpfu.itis.belskaya.repositories.TutorRepository;
import ru.kpfu.itis.belskaya.repositories.AccountRepository;

import java.util.Optional;

/**
 * @author Elizaveta Belskaya
 */

public class UserService<T extends User> {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private StudentRepository studentRepository;


    public T findByAccount_Id(Long id) {
        Account account = accountRepository.getOne(id);
        switch (account.getRole()) {
            case TUTOR:
                return (T) tutorRepository.findByAccount_Id(id);
            case STUDENT:
                return (T) studentRepository.findByAccount_Id(id);
        }
        return null;
    }
}
