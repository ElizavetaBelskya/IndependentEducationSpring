package ru.kpfu.itis.belskaya.models;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.kpfu.itis.belskaya.services.UserService;

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
@Table(name = "ie_account")
public class Account implements CredentialsContainer, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(name = "email_and_role", nullable = false, unique = true)
    private String emailAndRole;

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
        return emailAndRole;
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