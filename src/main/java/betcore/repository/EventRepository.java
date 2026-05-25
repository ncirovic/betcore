package betcore.repository;

import betcore.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {

    List<EventEntity> findBySportId(Long sportId);

    List<EventEntity> findByStatus(EventEntity.EventStatus status);
}
