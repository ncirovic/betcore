package betcore.repository;

import betcore.entity.SportEntity;
import betcore.model.Sport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SportRepository extends JpaRepository<SportEntity, Long> {

    Optional<SportEntity> findByCode(String code);

    boolean existsByCode(String code);

    List<Sport> findNameByNameContainingIgnoreCase(String name);
}
