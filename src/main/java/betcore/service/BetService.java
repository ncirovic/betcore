package betcore.service;

import betcore.dto.BetRequest;
import betcore.dto.BetResponse;
import betcore.entity.BetEntity;
import betcore.entity.PlayerEntity;
import betcore.entity.SelectionEntity;
import betcore.exception.ResourceNotFoundException;
import betcore.repository.BetRepository;
import betcore.repository.SelectionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BetService {
    private final BetRepository betRepository;
    private final SelectionRepository selectionRepository;
    private final PlayerService playerService;
    private final WalletService walletService;

    @Transactional
    public BetResponse placeBet(BetRequest betRequest) {
        PlayerEntity player = playerService.findById(betRequest.getPlayerId());

        SelectionEntity selection = selectionRepository.findById(betRequest.getSelectionId())
                .orElseThrow(() -> new ResourceNotFoundException("Selection not found: "+betRequest.getSelectionId()));

        BigDecimal potentialWin = betRequest.getStake()
                .multiply(selection.getOdds())
                .setScale(2, RoundingMode.HALF_UP);

        walletService.recordBet(
                player,
                betRequest.getStake(),
                "Bet on " + selection.getName() + " @ " + selection.getOdds()
        );

        BetEntity bet = new BetEntity();
        bet.setPlayer(player);
        bet.setSelection(selection);
        bet.setStake(betRequest.getStake());
        bet.setOdds(selection.getOdds());
        bet.setPotentialWin(potentialWin);

        BetEntity savedBet = betRepository.save(bet);

        log.info("Bet placed: betId={}, playerId={}, selectionId={}, stake={}, odds={}, potentialWin={}",
                savedBet.getId(), player.getId(), selection.getId(),
                betRequest.getStake(), selection.getOdds(), potentialWin);

        return BetResponse.form(savedBet);
    }

    public List<BetResponse> findByPlayerId(Long playerId) {
        return betRepository.findByPlayerIdOrderByPlacedAtDesc(playerId).stream()
                .map(BetResponse::form)
                .toList();
    }

    public BetResponse findResponseById(Long id) {
        BetEntity bet = betRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Bet not found: " + id));
        return BetResponse.form(bet);
    }
}
