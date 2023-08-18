package account.repository;

import account.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findUserByEmailIgnoreCase(String email);

    @Query("UPDATE User u SET u.failedAttempts = :failedAttempts WHERE u.email = :username")
    @Modifying
    public void setFailedAttempts(@Param("failedAttempts") int failedAttempts, @Param("username") String username);

    @Query("SELECT u.failedAttempts from User u where u.email = :username")
    public int getFailedAttempts(@Param("username") String username);
}
