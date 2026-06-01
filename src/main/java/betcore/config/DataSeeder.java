package betcore.config;

import betcore.entity.EventEntity;
import betcore.entity.MarketEntity;
import betcore.entity.SelectionEntity;
import betcore.entity.SportEntity;
import betcore.repository.EventRepository;
import betcore.repository.MarketRepository;
import betcore.repository.SportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final SportRepository sportRepository;
    private final EventRepository eventRepository;
    private final MarketRepository marketRepository;

    @Override
    public void run(String ...args) {
        if (sportRepository.count() > 0) {
            return;
        }

        sportRepository.saveAll(List.of(
                new SportEntity(null, "Football", "FOOTBALL"),
                new SportEntity(null, "Basketball", "BASKETBALL"),
                new SportEntity(null, "Tennis", "TENNIS"),
                new SportEntity(null, "Ice Hockey", "ICE_HOCKEY")
        ));

        log.info("Seeded {} sports", sportRepository.count());

        SportEntity football = sportRepository.findByCode("FOOTBALL").orElseThrow();
        SportEntity basketball = sportRepository.findByCode("BASKETBALL").orElseThrow();

        EventEntity event1 = new EventEntity();
        event1.setSport(football);
        event1.setName("Man City vs Arsenal");
        event1.setStartTime(LocalDateTime.now().plusDays(2));

        EventEntity event2 = new EventEntity();
        event2.setName("Real Madrid vs Barcelona");
        event2.setStartTime(LocalDateTime.now().plusDays(2));
        event2.setSport(football);

        EventEntity event3 = new EventEntity();
        event3.setName("Lakers vs Celtics");
        event3.setStartTime(LocalDateTime.now().plusDays(2));
        event3.setSport(basketball);

        eventRepository.saveAll(List.of(event1, event2, event3));
        log.info("Seeded {} events", eventRepository.count());

        MarketEntity matchWinner = new MarketEntity();
        matchWinner.setName("Match Winner");
        matchWinner.setEvent(event1);

        SelectionEntity homeWin = new SelectionEntity();
        homeWin.setName("Man City");
        homeWin.setOdds(new BigDecimal("1.85"));
        homeWin.setMarket(matchWinner);

        SelectionEntity draw = new SelectionEntity();
        draw.setName("Draw");
        draw.setOdds(new BigDecimal("3.40"));
        draw.setMarket(matchWinner);

        SelectionEntity awayWin = new SelectionEntity();
        awayWin.setName("Arsenal");
        awayWin.setOdds(new BigDecimal("4.20"));
        awayWin.setMarket(matchWinner);

        matchWinner.setSelections(List.of(homeWin, draw, awayWin));
        marketRepository.save(matchWinner);

        log.info("Seeded {} markets", marketRepository.count());

    }
}
