package betcore.service;

import betcore.entity.EventEntity;
import betcore.entity.SportEntity;
import betcore.exception.ResourceNotFoundException;
import betcore.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final SportService sportService;

    public List<EventEntity> findAll(Long sportId) {
        return eventRepository.findAll();
    }

    public EventEntity findById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + eventId));
    }

    public List<EventEntity> findBySportId(Long sportId) {
        return eventRepository.findBySportId(sportId);
    }

    public EventEntity create(Long sportId, EventEntity event) {
        SportEntity sport = sportService.findById(sportId);
        event.setSport(sport);
        return eventRepository.save(event);
    }
}
