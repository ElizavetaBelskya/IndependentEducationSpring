package ru.kpfu.itis.belskaya.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

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
@Table(name = "ie_account", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email", "role"})})
@Schema(description = "Account", hidden = true)
public class Account implements CredentialsContainer, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private City city;

    @Enumerated(value = EnumType.STRING)
    private State state;

    @Enumerated(value = EnumType.STRING)
    private Role role;
    @Override
    public void eraseCredentials() {
        this.passwordHash = null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.toString()));
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return id.toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return (state == State.ACTIVE);
    }

    @Override
    public boolean isAccountNonLocked() {
        return (state != State.BANNED);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return (state == State.ACTIVE);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public enum State {
        ACTIVE,
        BANNED,
        DELETED
    }

    public enum Role {
        USER("User"),
        ADMIN("Admin"),
        STUDENT("Student"),
        TUTOR("Tutor");

        private final String className;

        Role(String className) {
            this.className = className;
        }

        public String getClassName() {
            return className;
        }
    }



}
