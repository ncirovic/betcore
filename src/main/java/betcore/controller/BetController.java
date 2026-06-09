package betcore.controller;

import betcore.dto.BetRequest;
import betcore.dto.BetResponse;
import betcore.service.BetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bets")
@RequiredArgsConstructor
public class BetController {

    private final BetService betService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BetResponse createBet(@Valid @RequestBody BetRequest betRequest) {
        return betService.placeBet(betRequest);
    }

    @GetMapping("/{id}")
    public BetResponse getById(@PathVariable Long id) {
        return betService.findResponseById(id);
    }

    @GetMapping("/player/{playerId}")
    public List<BetResponse> getByPlayer(@PathVariable Long playerId) {
        return betService.findByPlayerId(playerId);
    }
}
