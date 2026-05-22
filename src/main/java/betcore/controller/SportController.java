package betcore.controller;

import betcore.entity.SportEntity;
import betcore.model.Sport;
import betcore.repository.SportRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sports")
public class SportController {
    private final List<Sport> sports = List.of(
            new Sport(1L, "Football", "FOOTBALL"),
            new Sport(2L, "Basketball", "BASKETBALL"),
            new Sport(3L, "Tennis", "TENNIS"),
            new Sport(4L, "Ice Hockey", "ICE_HOCKEY")
    );
    private final SportRepository sportRepository;

    public SportController(SportRepository sportRepository) {
        this.sportRepository = sportRepository;
    }

    @GetMapping
    public List<SportEntity> getSports() {
        return sportRepository.findAll();
    }

    @GetMapping("/{id}")
    public SportEntity getSport(@PathVariable Long id) {
        return sportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sport with id " + id + " not found"));
    }

    @GetMapping("/search")
    public List<Sport> getSportsByName(@RequestParam String name) {
        return sportRepository.findNameByNameContainingIgnoreCase(name.toLowerCase());
    }
}
