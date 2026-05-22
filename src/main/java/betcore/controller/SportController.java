package betcore.controller;

import betcore.model.Sport;
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

    @GetMapping
    public List<Sport> getSports() {
        return sports;
    }

    @GetMapping("/{id}")
    public Sport getSport(@PathVariable Long id) {
        return sports.stream()
                .filter(s -> s.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Sport with id " + id + " not found"));
    }

    @GetMapping("/search")
    public List<Sport> getSportsByName(@RequestParam String name) {
        return sports.stream()
                .filter(s -> s.name().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }
}
