package betcore.repository;

import betcore.entity.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {

    Optional<PlayerEntity> findByUsername(String username);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
