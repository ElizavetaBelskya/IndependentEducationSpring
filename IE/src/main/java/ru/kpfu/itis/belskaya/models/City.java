package ru.kpfu.itis.belskaya.models;

import lombok.*;

import javax.persistence.*;

/**
 * @author Elizaveta Belskaya
 */
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "city")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;


}
