package betcore.controller;

import betcore.entity.SportEntity;
import betcore.repository.SportRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sports")
public class SportController {
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
    public List<SportEntity> getSportsByName(@RequestParam String name) {
        return sportRepository.findNameByNameContainingIgnoreCase(name.toLowerCase());
    }
}
