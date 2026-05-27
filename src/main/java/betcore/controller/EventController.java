package betcore.controller;

import betcore.dto.EventRequest;
import betcore.dto.EventResponse;
import betcore.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public List<EventResponse> getAll(@RequestParam(required = false) Long sportId) {
        if (sportId != null) {
            return eventService.findBySportId(sportId);
        }
        return eventService.findAll();
    }

    @GetMapping("/{id}")
    public EventResponse getById(@PathVariable Long id) {
        return eventService.findResponseById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponse create(@RequestParam Long sportId, @RequestBody EventRequest request) {
        return eventService.create(sportId, request);
    }
}
