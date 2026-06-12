package betcore.repository;

import betcore.entity.BetEntity;
import betcore.repository.projection.LeaderboardProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
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

    // JPQL — object-oriented query language (references entities, not tables)
    @Query("SELECT COUNT(b) FROM BetEntity b WHERE b.player.id = :playerId AND b.status = :status")
    Long countByPlayerAndStatus(@Param("playerId") Long playerId,
                                @Param("status") BetEntity.BetStatus status);

    // Native SQL for complex aggregation
    @Query(value = """
        SELECT
            p.id as player_id,
            p.username,
            COUNT(b.id) as total_bets,
            COALESCE(SUM(b.stake), 0) as total_staked,
            COALESCE(SUM(CASE WHEN b.status = 'WON' THEN b.potential_win ELSE 0 END), 0) as total_won,
            COALESCE(SUM(CASE WHEN b.status = 'WON' THEN b.potential_win ELSE 0 END), 0)
                - COALESCE(SUM(b.stake), 0) as net_profit,
            COALESCE(SUM(CASE WHEN b.status = 'WON' THEN 1 ELSE 0 END), 0) as wins,
            COALESCE(SUM(CASE WHEN b.status = 'LOST' THEN 1 ELSE 0 END), 0) as losses
        FROM bets b
        JOIN players p ON b.player_id = p.id
        WHERE b.status IN ('WON', 'LOST')
        GROUP BY p.id, p.username
        ORDER BY net_profit DESC
        """,
            nativeQuery = true)
    List<LeaderboardProjection> getLeaderboardRaw(Pageable pageable);
}
