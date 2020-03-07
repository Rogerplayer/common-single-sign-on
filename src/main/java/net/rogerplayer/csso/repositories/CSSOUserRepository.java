package net.rogerplayer.csso.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.rogerplayer.csso.domain.CSSOUser;

@Repository
public interface CSSOUserRepository extends JpaRepository<CSSOUser, Long> {

	@Query("SELECT DISTINCT cssouser FROM CSSOUser cssouser WHERE cssouser.username = :username")
	public Optional<CSSOUser> findByUserName(@Param("username") String username);
}
