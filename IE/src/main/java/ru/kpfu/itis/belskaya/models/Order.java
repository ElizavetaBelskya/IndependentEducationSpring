package ru.kpfu.itis.belskaya.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
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
@Table(name = "ie_order")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(nullable = false)
    private String subject;

    @ManyToOne(cascade = { CascadeType.DETACH }, fetch = FetchType.LAZY)
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

    @ManyToOne(cascade = { CascadeType.DETACH }, fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id", referencedColumnName = "id")
    @JsonIgnoreProperties("orders")
    private Tutor tutor;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "order_tutor",
            joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tutor_id", referencedColumnName = "id")
    )
    @JsonIgnoreProperties("orders")
    private List<Tutor> candidates;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Format online;

    private Integer price;

    public enum Format {
        ONLINE,
        OFFLINE,
        BOTH
    }

}
