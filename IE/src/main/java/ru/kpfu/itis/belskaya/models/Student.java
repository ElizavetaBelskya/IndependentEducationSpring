package ru.kpfu.itis.belskaya.models;

import lombok.*;

import javax.persistence.*;

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
@Table(name = "ie_student")
public class Student extends User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone", unique = true, nullable = true)
    private String phone;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Override
    public void setAccount(Account account) {
        this.account = account;
    }

}
