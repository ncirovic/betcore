package betcore.service;

import betcore.dto.EventRequest;
import betcore.dto.EventResponse;
import betcore.entity.*;
import betcore.exception.ResourceNotFoundException;
import betcore.repository.BetRepository;
import betcore.repository.EventRepository;
import betcore.repository.MarketRepository;
import betcore.repository.SelectionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final EventRepository eventRepository;
    private final SportService sportService;
    private final SelectionRepository selectionRepository;
    private final MarketRepository marketRepository;
    private final BetRepository betRepository;
    private final WalletService walletService;

    public List<EventResponse> findAll() {
        return eventRepository.findAll().stream()
                .map(EventResponse::form)
                .toList();
    }

    public EventEntity findById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + eventId));
    }

    public EventResponse findResponseById(Long eventId) {
        return EventResponse.form(findById(eventId));
    }

    public List<EventResponse> findBySportId(Long sportId) {
        return eventRepository.findBySportId(sportId).stream()
                .map(EventResponse::form)
                .toList();
    }

    public EventResponse create(Long sportId, EventRequest request) {
        SportEntity sportEntity = sportService.findById(sportId);
        EventEntity eventEntity = new EventEntity();

        eventEntity.setName(request.getName());
        eventEntity.setStartTime(request.getStartTime());
        eventEntity.setSport(sportEntity);

        return EventResponse.form(eventRepository.save(eventEntity));
    }

    @Transactional
    public EventResponse settleEvent(Long eventId, Long winningSelectionId) {
        EventEntity event = findById(eventId);

        if (event.getStatus() != EventEntity.EventStatus.FINISHED) {
            throw new IllegalArgumentException(
                    "Event must be FINISHED before settlement. Current status: " + event.getStatus());
        }

        // 1. Process the WINNERS first
        List<BetEntity> winningBets = betRepository
                .findBySelectionIdAndStatus(winningSelectionId, BetEntity.BetStatus.PENDING);

        for (BetEntity bet : winningBets) {
            bet.setStatus(BetEntity.BetStatus.WON);
            bet.setSettledAt(LocalDateTime.now());

            // Payout logic
            walletService.recordWin(bet.getPlayer(), bet.getPotentialWin(),
                    "Win: " + bet.getSelection().getName() + " @ " + bet.getOdds());

            log.info("Bet WON: betId={}, playerId={}, payout={}",
                    bet.getId(), bet.getPlayer().getId(), bet.getPotentialWin());
        }
        betRepository.saveAll(winningBets);

        // 2. Bulk update all remaining pending bets for this event to LOST in one query
        int updatedLosersCount = betRepository.bulkUpdatePendingBetsToLost(eventId, winningSelectionId, LocalDateTime.now());
        log.info("Bulk updated {} losing bets to LOST for eventId={}", updatedLosersCount, eventId);

        return EventResponse.form(event);
    }

    @Transactional
    public EventResponse updateStatus(Long eventId, EventEntity.EventStatus newStatus) {
        EventEntity event = findById(eventId);
        event.setStatus(newStatus);
        return EventResponse.form(eventRepository.save(event));
    }

}
