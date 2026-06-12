package betcore.controller;

import betcore.dto.LeaderboardEntry;
import betcore.repository.BetRepository;
import betcore.repository.projection.LeaderboardProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {
    private final BetRepository betRepository;

    @GetMapping
    public List<LeaderboardEntry> getLeaderboard(@RequestParam(defaultValue = "10") int limit) {

        List<LeaderboardProjection> raw = betRepository.getLeaderboardRaw(PageRequest.of(0, limit));
        List<LeaderboardEntry> leaderboard = new ArrayList<>();

        return IntStream.range(0, raw.size())
                .mapToObj( i -> {
                    LeaderboardProjection row = raw.get(i);
                    return LeaderboardEntry.builder()
                            .rank(i + 1)
                            .playerId(row.getPlayerId())
                            .username(row.getUsername())
                            .totalBets(row.getTotalBets())
                            .totalStaked(row.getTotalStaked())
                            .totalWon(row.getTotalWon())
                            .netProfit(row.getNetProfit())
                            .wins(row.getWins())
                            .losses(row.getLosses())
                            .build();
                }).collect(Collectors.toList());
    }
}
