package betcore.controller;

import betcore.entity.SportEntity;
import betcore.repository.SportRepository;
import betcore.service.SportService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sports")
public class SportController {
    private final SportRepository sportRepository;
    private final SportService sportService;

    public SportController(SportRepository sportRepository, SportService sportService) {
        this.sportRepository = sportRepository;
        this.sportService = sportService;
    }

    @GetMapping
    public List<SportEntity> getSports() {
        return sportService.findAll();
    }

    @GetMapping("/{id}")
    public SportEntity getSport(@PathVariable Long id) {
        return sportService.findById(id);
    }

    @GetMapping("/search")
    public List<SportEntity> getSportsByName(@RequestParam String name) {
        return sportRepository.findNameByNameContainingIgnoreCase(name.toLowerCase());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SportEntity create(@RequestBody SportEntity sportEntity) {
        return sportService.create(sportEntity);
    }

    @PutMapping("/{id}")
    public SportEntity update(@PathVariable Long id, @RequestBody SportEntity sportEntity) {
        return sportService.update(id, sportEntity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        sportService.delete(id);
    }
}
