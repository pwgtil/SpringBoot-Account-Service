package account.repository;

import account.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByEmailIgnoreCase(String email);

    @Query("UPDATE User u SET u.failedAttempts = :failedAttempts WHERE u.email = :username")
    @Modifying
    int setFailedAttempts(@Param("failedAttempts") int failedAttempts, @Param("username") String username);

    @Query("SELECT u.failedAttempts from User u where u.email = :username")
    int getFailedAttempts(@Param("username") String username);
}
