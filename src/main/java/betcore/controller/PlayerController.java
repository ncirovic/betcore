package betcore.controller;

import betcore.dto.PlayerRequest;
import betcore.dto.PlayerResponse;
import betcore.entity.BetEntity;
import betcore.repository.BetRepository;
import betcore.service.PlayerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;
    private final BetRepository betRepository;

    @GetMapping
    public List<PlayerResponse> getAll() {
        return playerService.findAll();
    }

    @GetMapping("/{id}")
    public PlayerResponse getById(@PathVariable Long id){
        return playerService.findResponseById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlayerResponse create(@Valid @RequestBody PlayerRequest playerRequest){
        return playerService.create(playerRequest);
    }

    @GetMapping("/{id}/stats")
    public Map<String, Object> getPlayerStats(@PathVariable Long id) {
        PlayerResponse player = playerService.findResponseById(id);
        return Map.of(
                "player", player,
                "totalBets", betRepository.countByPlayerAndStatus(id, BetEntity.BetStatus.WON)
                        + betRepository.countByPlayerAndStatus(id, BetEntity.BetStatus.LOST)
                        + betRepository.countByPlayerAndStatus(id, BetEntity.BetStatus.PENDING),
                "wins", betRepository.countByPlayerAndStatus(id, BetEntity.BetStatus.WON),
                "losses", betRepository.countByPlayerAndStatus(id, BetEntity.BetStatus.LOST),
                "pending", betRepository.countByPlayerAndStatus(id, BetEntity.BetStatus.PENDING)
        );
    }
}
