package ru.kpfu.itis.belskaya.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

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
@Table(name = "ie_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String subject;
    @ManyToOne
    @JoinColumn(name = "author_id",  referencedColumnName = "id")
    private Student author;
    private String description;
    @Column(name = "tutor_gender", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Gender tutorGender;
    @Column(name = "min_rating")
    private float minRating = 0;
    @Column(name = "created_at")
    private LocalDateTime creationDate;

    @PrePersist
    private void updateCreationDate() {
        creationDate = LocalDateTime.now();
    }

    @ManyToOne
    @JoinColumn(name = "tutor_id", referencedColumnName = "id")
    private Tutor tutor;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Format online;

    private Integer price;


    public enum Gender {
        MALE,
        FEMALE,
        BOTH
    }

    public enum Format {
        ONLINE,
        OFFLINE,
        BOTH
    }

}
