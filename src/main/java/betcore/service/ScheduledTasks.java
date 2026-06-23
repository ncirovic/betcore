package betcore.service;

import betcore.entity.BetEntity;
import betcore.entity.EventEntity;
import betcore.repository.BetRepository;
import betcore.repository.EventRepository;
import betcore.repository.PlayerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasks {
    private final BetRepository betRepository;
    private final EventRepository eventRepository;
    private final PlayerRepository playerRepository;

    /**
     * Every 5 minutes: void pending bets on events that started more than 24 hours ago
     * and were never settled. This prevents orphaned bets.
     */
    @Scheduled(fixedRate = 300000)
    @Transactional
    public void voidStaleBets() {
        List<BetEntity> bets = betRepository.findByStatus(BetEntity.BetStatus.PENDING);
        LocalDateTime cutoff = LocalDateTime.now().minusHours(24);

        int voided = 0;
        for (BetEntity bet : bets) {
            if (bet.getSelection().getMarket().getEvent().getStartTime().isBefore(cutoff)) {
                bet.setStatus(BetEntity.BetStatus.VOID);
                bet.setSettledAt(LocalDateTime.now());
                betRepository.save(bet);
                voided++;
            }
        }

        if (voided > 0) {
            log.info("Voided {} stale pending bets", voided);
        }
    }

    /**
     * Daily at midnight: log a summary of platform activity.
     * In production, this would send an email or write to an analytics table.
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void dailySummary() {
        long totalPlayers = playerRepository.count();
        long totalBets = betRepository.count();
        long pendingBets = betRepository.findByStatus(BetEntity.BetStatus.PENDING).size();
        long upcomingEvents = eventRepository.findByStatus(EventEntity.EventStatus.UPCOMING).size();

        log.info("=== DAILY SUMMARY ===");
        log.info("Total players: {}", totalPlayers);
        log.info("Total bets: {} (pending: {})", totalBets, pendingBets);
        log.info("Upcoming events: {}", upcomingEvents);
        log.info("=====================");
    }

}
