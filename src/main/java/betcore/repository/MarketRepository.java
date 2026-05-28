package betcore.repository;

import betcore.entity.MarketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketRepository extends JpaRepository<MarketEntity, Integer> {
    List<MarketEntity> findByEventId(Long eventId);
}
