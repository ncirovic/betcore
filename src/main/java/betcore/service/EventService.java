package betcore.service;

import betcore.dto.EventRequest;
import betcore.dto.EventResponse;
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

    public List<EventResponse> findAll() {
        return eventRepository.findAll().stream()
                .map(EventResponse::form)
                .toList();
    }

    public EventEntity findById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + eventId));
    }

    public EventResponse findResponseById(Long eventId) {
        return EventResponse.form(findById(eventId));
    }

    public List<EventResponse> findBySportId(Long sportId) {
        return eventRepository.findBySportId(sportId).stream()
                .map(EventResponse::form)
                .toList();
    }

    public EventResponse create(Long sportId, EventRequest request) {
        SportEntity sportEntity = sportService.findById(sportId);
        EventEntity eventEntity = new EventEntity();

        eventEntity.setName(request.getName());
        eventEntity.setStartTime(request.getStartTime());
        eventEntity.setSport(sportEntity);

        return EventResponse.form(eventRepository.save(eventEntity));
    }
}
