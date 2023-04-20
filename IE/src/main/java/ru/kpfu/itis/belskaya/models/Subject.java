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
@Table(name = "subject")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;


}
