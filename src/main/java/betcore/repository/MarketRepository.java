package betcore.repository;

import betcore.entity.EventEntity;
import betcore.entity.MarketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketRepository extends JpaRepository<MarketEntity, Long> {
    List<MarketEntity> findByEventId(Long eventId);

    Long event(EventEntity event);
}
