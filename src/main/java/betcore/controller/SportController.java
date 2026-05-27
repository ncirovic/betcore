package betcore.controller;

import betcore.dto.SportRequest;
import betcore.dto.SportResponse;
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
    public List<SportResponse> getAll() {
        return sportService.findAll();
    }

    @GetMapping("/{id}")
    public SportResponse getSport(@PathVariable Long id) {
        return sportService.findResponseById(id);
    }

    @GetMapping("/search")
    public List<SportEntity> getSportsByName(@RequestParam String name) {
        return sportRepository.findNameByNameContainingIgnoreCase(name.toLowerCase());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SportResponse create(@RequestBody SportRequest request) {
        return sportService.create(request);
    }

    @PutMapping("/{id}")
    public SportResponse update(@PathVariable Long id, @RequestBody SportRequest request) {
        return sportService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        sportService.delete(id);
    }
}
