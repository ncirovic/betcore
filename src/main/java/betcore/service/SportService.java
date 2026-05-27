package betcore.service;

import betcore.dto.SportRequest;
import betcore.dto.SportResponse;
import betcore.entity.SportEntity;
import betcore.exception.ResourceNotFoundException;
import betcore.repository.SportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SportService {

    private final SportRepository sportRepository;

    public List<SportResponse> findAll() {
        return sportRepository.findAll().stream()
                .map(SportResponse::form)
                .toList();
    }

    public SportEntity findById(Long sportId) {
        return sportRepository.findById(sportId)
                .orElseThrow(() -> new ResourceNotFoundException("Sport not found: " + sportId));
    }

    public SportResponse findResponseById(Long id) {
        return SportResponse.form(findById(id));
    }

    public SportResponse create(SportRequest request) {
        SportEntity sportEntity = new SportEntity();
        sportEntity.setName(request.getName());
        sportEntity.setCode(request.getCode());

        return SportResponse.form(sportRepository.save(sportEntity));
    }

    public SportResponse update(Long id, SportRequest request) {
        SportEntity sportEntity = findById(id);
        sportEntity.setName(request.getName());
        sportEntity.setCode(request.getCode());
        return SportResponse.form(sportRepository.save(sportEntity));
    }

    public void delete(Long id) {
        if (!sportRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sport not found: " + id);
        }

        sportRepository.deleteById(id);
    }
}
