package ru.kpfu.itis.belskaya.services;

import org.springframework.beans.factory.annotation.Autowired;
import ru.kpfu.itis.belskaya.models.Account;
import ru.kpfu.itis.belskaya.models.User;
import ru.kpfu.itis.belskaya.repositories.StudentRepository;
import ru.kpfu.itis.belskaya.repositories.TutorRepository;
import ru.kpfu.itis.belskaya.repositories.AccountRepository;

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


    public T findByAccountId(Long id) {
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
