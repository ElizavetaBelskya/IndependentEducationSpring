package ru.kpfu.itis.belskaya.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

    private String description;

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

    @ManyToMany(mappedBy = "candidates")
    @JsonIgnoreProperties("candidates")
    private List<Order> orders;

    @Override
    public void setAccount(Account account) {
        this.account = account;
    }
}
