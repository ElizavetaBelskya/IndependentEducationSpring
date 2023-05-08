package ru.kpfu.itis.belskaya.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.belskaya.models.Rate;
import ru.kpfu.itis.belskaya.models.forms.RateDto;
import ru.kpfu.itis.belskaya.models.Tutor;
import ru.kpfu.itis.belskaya.repositories.RatesRepository;
import ru.kpfu.itis.belskaya.repositories.TutorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TutorService {

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private RatesRepository ratesRepository;


    public void changeRating(RateDto rateDto) {
        Tutor tutor = tutorRepository.findById(rateDto.getTutorId()).get();
        Optional<Rate> rate = ratesRepository.findByTutorAndStudent(tutor, rateDto.getStudent());
        if (rate.isPresent()) {
            rate.get().setRating(rateDto.getRating());
            rate.get().setComment(rateDto.getComment());
            ratesRepository.save(rate.get());
        } else {
            Rate newRate = Rate.builder()
                    .student(rateDto.getStudent())
                    .tutor(tutor)
                    .rating(rateDto.getRating())
                    .comment(rateDto.getComment()).build();
            ratesRepository.save(newRate);
        }
        tutor.setRating(ratesRepository.changeRating(rateDto.getTutorId()));
        tutorRepository.save(tutor);
    }

    public Optional<List<Rate>> getRatesOfTutor(Tutor tutor) {
        return ratesRepository.findAllByTutor(tutor);
    }


    public Optional<Tutor> getTutorById(Long id) {
        return tutorRepository.findById(id);
    }
}
