package betcore.controller;

import betcore.dto.MarketRequest;
import betcore.dto.MarketResponse;
import betcore.service.MarketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/markets")
@RequiredArgsConstructor
public class MarketController {

    private final MarketService marketService;

    @GetMapping
    public List<MarketResponse> getByEvent(@RequestParam Long eventId){
        return marketService.findByEventId(eventId);
    }

    @GetMapping("/{id}")
    public MarketResponse getById(@PathVariable Long id){
        return marketService.findResponseById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MarketResponse create(@Valid @RequestBody MarketRequest request){
        return marketService.create(request);
    }
}
