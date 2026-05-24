package betcore.config;

import betcore.entity.SportEntity;
import betcore.repository.SportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final SportRepository sportRepository;

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

        log.info("Seeder {} sports", sportRepository.count());
    }
}
