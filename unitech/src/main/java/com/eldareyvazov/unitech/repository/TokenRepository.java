package com.eldareyvazov.unitech.repository;

import com.eldareyvazov.unitech.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TokenRepository extends JpaRepository<UserToken, UUID> {
}
