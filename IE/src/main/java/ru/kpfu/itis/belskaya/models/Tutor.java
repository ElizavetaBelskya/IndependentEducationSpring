package ru.kpfu.itis.belskaya.models;

import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * @author Elizaveta Belskaya
 */


@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ie_tutor")
public class Tutor extends User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone", unique = true, nullable = true)
    private String phone;
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    private float rating;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @Column(name = "is_working_online")
    private boolean isWorkingOnline;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToMany(cascade = { CascadeType.DETACH }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "tutor_subject",
            joinColumns = { @JoinColumn(name = "subject_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "tutor_id", referencedColumnName = "id") }
    )
    private List<Subject> subjectList;

    @Override
    public void setAccount(Account account) {
        this.account = account;
    }
}
