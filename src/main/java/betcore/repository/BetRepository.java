package betcore.repository;

import betcore.entity.BetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BetRepository extends JpaRepository<BetEntity, Long> {

    List<BetEntity> findByPlayerIdOrderByPlacedAtDesc(Long playerId);

    List<BetEntity> findBySelectionIdAndStatus(Long selectionId, BetEntity.BetStatus status);

    List<BetEntity> findByStatus(BetEntity.BetStatus status);
}
