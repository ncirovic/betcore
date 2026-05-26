package betcore.controller;

import betcore.entity.EventEntity;
import betcore.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public List<EventEntity> getAll(@RequestParam(required = false) Long sportId) {
        if (sportId != null) {
            return eventService.findBySportId(sportId);
        }
        return eventService.findAll();
    }

    @GetMapping("/{id}")
    public EventEntity getById(@PathVariable Long id) {
        return eventService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventEntity create(@RequestParam Long sportId, @RequestBody EventEntity event) {
        return eventService.create(sportId, event);
    }
}
