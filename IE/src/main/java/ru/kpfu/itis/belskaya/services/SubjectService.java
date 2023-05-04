package ru.kpfu.itis.belskaya.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.belskaya.models.Subject;
import ru.kpfu.itis.belskaya.repositories.SubjectRepository;

import java.util.List;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    public List<String> findAllTitles() {
        return subjectRepository.findAllTitles();
    }


    public List<Subject> findAllSubjects() {
        return subjectRepository.findAll();
    }


}

