package ru.kpfu.itis.belskaya.converters;


import lombok.AllArgsConstructor;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import ru.kpfu.itis.belskaya.models.Account;
import ru.kpfu.itis.belskaya.models.Gender;
import ru.kpfu.itis.belskaya.models.Student;
import ru.kpfu.itis.belskaya.models.Tutor;
import ru.kpfu.itis.belskaya.models.forms.StudentForm;
import ru.kpfu.itis.belskaya.models.forms.TutorForm;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class StudentFormToAccountAndStudentConverter implements GenericConverter {

    private final String STUDENT = "_____Student";

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        HashSet<ConvertiblePair> pairSet = new HashSet<>();
        pairSet.add(new ConvertiblePair(TutorForm.class, Account.class));
        pairSet.add(new ConvertiblePair(TutorForm.class, Student.class));
        return pairSet;
    }

    @Override
    public Object convert(Object o, TypeDescriptor typeDescriptor, TypeDescriptor typeDescriptor1) {
        if (StudentForm.class.equals(typeDescriptor.getType()) && Account.class.equals(typeDescriptor1.getType())) {
            StudentForm studentForm = (StudentForm) o;
            Account account = Account.builder()
                    .emailAndRole(studentForm.getEmail() + STUDENT)
                    .name(studentForm.getName())
                    .passwordHash(studentForm.getPassword())
                    .role(Account.Role.STUDENT)
                    .state(Account.State.ACTIVE)
                    .city(studentForm.getCity())
                    .build();
            return account;
        } else if (StudentForm.class.equals(typeDescriptor.getType()) && Tutor.class.equals(typeDescriptor1.getType())) {
            StudentForm studentForm = (StudentForm) o;
            Student student = Student.builder()
                    .email(studentForm.getEmail())
                    .phone(studentForm.getPhone())
                    .build();
            return student;
        }
        throw new IllegalArgumentException();
    }



}
