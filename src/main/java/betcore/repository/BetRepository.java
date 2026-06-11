package betcore.repository;

import betcore.entity.BetEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BetRepository extends JpaRepository<BetEntity, Long> {

    List<BetEntity> findByPlayerIdOrderByPlacedAtDesc(Long playerId);

    List<BetEntity> findBySelectionIdAndStatus(Long selectionId, BetEntity.BetStatus status);

    List<BetEntity> findByStatus(BetEntity.BetStatus status);

    @Modifying
    @Query("UPDATE BetEntity b SET b.status = BetStatus.LOST, b.settledAt = :settledAt " +
            "WHERE b.status = BetStatus.PENDING " +
            "AND b.selection.id != :winningSelectionId " +
            "AND b.selection.market.event.id = :eventId")
    Integer bulkUpdatePendingBetsToLost(
            @Param("eventId") Long eventId,
            @Param("winningSelectionId") Long winningSelectionId,
            @Param("settledAt") LocalDateTime settledAt
    );
}
