package ru.kpfu.itis.belskaya.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.belskaya.models.Tutor;
import ru.kpfu.itis.belskaya.repositories.StudentRepository;

@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    public int getStudentsCountByTutor(Tutor tutor) {
        return studentRepository.getStudentsCountByTutor(tutor);
    }




}
