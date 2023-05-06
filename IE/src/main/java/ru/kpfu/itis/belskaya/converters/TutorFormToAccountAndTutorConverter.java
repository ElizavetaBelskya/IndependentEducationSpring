package ru.kpfu.itis.belskaya.converters;


import lombok.AllArgsConstructor;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import ru.kpfu.itis.belskaya.models.Account;
import ru.kpfu.itis.belskaya.models.Gender;
import ru.kpfu.itis.belskaya.models.Tutor;
import ru.kpfu.itis.belskaya.models.forms.TutorForm;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class TutorFormToAccountAndTutorConverter implements GenericConverter {

    private final String TUTOR = "_____Tutor";

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        HashSet<ConvertiblePair> pairSet = new HashSet<>();
        pairSet.add(new ConvertiblePair(TutorForm.class, Account.class));
        pairSet.add(new ConvertiblePair(TutorForm.class, Tutor.class));
        return pairSet;
    }

    @Override
    public Object convert(Object o, TypeDescriptor typeDescriptor, TypeDescriptor typeDescriptor1) {
        if (TutorForm.class.equals(typeDescriptor.getType()) && Account.class.equals(typeDescriptor1.getType())) {
            TutorForm tutorForm = (TutorForm) o;
            Account account = Account.builder()
                    .emailAndRole(tutorForm.getEmail() + TUTOR)
                    .name(tutorForm.getName())
                    .passwordHash(tutorForm.getPassword())
                    .role(Account.Role.TUTOR)
                    .state(Account.State.ACTIVE)
                    .city(tutorForm.getCity())
                    .build();
            return account;
        } else if (TutorForm.class.equals(typeDescriptor.getType()) && Tutor.class.equals(typeDescriptor1.getType())) {
            TutorForm tutorForm = (TutorForm) o;
            Tutor tutor = Tutor.builder()
                    .email(tutorForm.getEmail())
                    .phone(tutorForm.getPhone())
                    .gender(tutorForm.isGender()? Gender.MALE : Gender.FEMALE)
                    .subjectList(tutorForm.getSubjects())
                    .isWorkingOnline(tutorForm.getIsWorkingOnline())
                    .build();
            return tutor;
        }
        throw new IllegalArgumentException();
    }


}
