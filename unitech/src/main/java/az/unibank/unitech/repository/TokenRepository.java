package az.unibank.unitech.repository;

import az.unibank.unitech.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TokenRepository extends JpaRepository<UserToken, UUID> {
}
