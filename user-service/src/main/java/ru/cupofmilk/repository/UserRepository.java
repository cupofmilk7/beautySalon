package ru.cupofmilk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.cupofmilk.model.User;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE YEAR(u.birthday) >= :yearOfBirthMin AND YEAR(u.birthday) <= :yearOfBirthMax")
    List<User> findUsers(@Param("yearOfBirthMin") int yearOfBirthMin,
                         @Param("yearOfBirthMax") int yearOfBirthMax);
}
