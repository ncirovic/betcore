package betcore.service;

import betcore.dto.MarketRequest;
import betcore.dto.MarketResponse;
import betcore.entity.EventEntity;
import betcore.entity.MarketEntity;
import betcore.entity.SelectionEntity;
import betcore.exception.ResourceNotFoundException;
import betcore.repository.MarketRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarketService {

    private final MarketRepository marketRepository;
    private final EventService eventService;

    public List<MarketResponse> findByEventId(Long eventId){
        return marketRepository.findByEventId(eventId).stream()
                .map(MarketResponse::form)
                .toList();
    }

    public MarketEntity findById(Long id) {
        return marketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Market not found: " + id));
    }

    public MarketResponse findResponseById(Long id){
        return MarketResponse.form(findById(id));
    }

    @Transactional
    public MarketResponse create(MarketRequest request){
        EventEntity event = eventService.findById(request.getEventId());

        MarketEntity market = new MarketEntity();
        market.setName(request.getName());
        market.setEvent(event);

        List<SelectionEntity> selections = request.getSelections().stream()
                .map(sr -> {
                    SelectionEntity selection = new SelectionEntity();
                    selection.setName(sr.getName());
                    selection.setOdds(sr.getOdds());
                    selection.setMarket(market);
                    return selection;
                }).toList();

        market.setSelections(selections);

        return MarketResponse.form(marketRepository.save(market));
    }
}
