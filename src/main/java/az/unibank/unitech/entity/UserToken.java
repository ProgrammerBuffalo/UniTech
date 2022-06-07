package az.unibank.unitech.entity;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users_tokens")
public class UserToken {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @ColumnDefault("random_uuid()")
    @Type(type = "uuid-char")
    private UUID id;

    @Column
    private LocalDateTime expireAt = LocalDateTime.now().plusMinutes(15);

    @Column
    private Boolean isRevoked = false;

    @JoinColumn(nullable = false)
    @ManyToOne
    private User user;

    public UserToken() {
    }

    public UUID getId() {
        return id;
    }

    public UserToken setId(UUID id) {
        this.id = id;
        return this;
    }

    public LocalDateTime getExpireAt() {
        return expireAt;
    }

    public UserToken setExpireAt(LocalDateTime expireAt) {
        this.expireAt = expireAt;
        return this;
    }

    public Boolean getRevoked() {
        return isRevoked;
    }

    public UserToken setRevoked(Boolean revoked) {
        isRevoked = revoked;
        return this;
    }

    public User getUser() {
        return user;
    }

    public UserToken setUser(User user) {
        this.user = user;
        return this;
    }
}
