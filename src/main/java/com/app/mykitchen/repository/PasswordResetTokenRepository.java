package com.app.mykitchen.repository;

import java.util.Date;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.mykitchen.domain.PasswordResetToken;
import com.app.mykitchen.domain.User;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
	
	PasswordResetToken findByToken(String token);
	
	PasswordResetToken findByUser(User user);
	
	Stream<PasswordResetToken> findAllByExpiryDateLessThan(Date currentDate);
	
	@Modifying
	@Query("delete from PasswordResetToken where expiryDate <= ?1")
	void deleteAllExpiredSince(Date currentDate);
}
