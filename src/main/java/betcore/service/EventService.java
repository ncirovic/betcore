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

        selectionRepository.findById(winningSelectionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Selection not found: " + winningSelectionId));

        // Find all pending bets for selections in this event's markets
        List<MarketEntity> markets = marketRepository.findByEventId(eventId);
        for (MarketEntity market : markets) {
            for (SelectionEntity selection : market.getSelections()) {
                List<BetEntity> pendingBets = betRepository
                        .findBySelectionIdAndStatus(selection.getId(), BetEntity.BetStatus.PENDING);

                for (BetEntity bet : pendingBets) {
                    if (selection.getId().equals(winningSelectionId)) {
                        // Winner — pay out
                        bet.setStatus(BetEntity.BetStatus.WON);
                        walletService.recordWin(bet.getPlayer(), bet.getPotentialWin(),
                                "Win: " + selection.getName() + " @ " + bet.getOdds());
                        log.info("Bet WON: betId={}, playerId={}, payout={}",
                                bet.getId(), bet.getPlayer().getId(), bet.getPotentialWin());
                    } else {
                        // Loser
                        bet.setStatus(BetEntity.BetStatus.LOST);
                        log.info("Bet LOST: betId={}, playerId={}",
                                bet.getId(), bet.getPlayer().getId());
                    }
                    bet.setSettledAt(LocalDateTime.now());
                    betRepository.save(bet);
                }
            }
        }

        return EventResponse.form(event);
    }

    @Transactional
    public EventResponse updateStatus(Long eventId, EventEntity.EventStatus newStatus) {
        EventEntity event = findById(eventId);
        event.setStatus(newStatus);
        return EventResponse.form(eventRepository.save(event));
    }

}
