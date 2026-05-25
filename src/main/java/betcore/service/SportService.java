package betcore.service;

import betcore.entity.SportEntity;
import betcore.exception.ResourceNotFoundException;
import betcore.repository.SportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SportService {

    private final SportRepository sportRepository;

    public List<SportEntity> findAll() {
        return sportRepository.findAll();
    }

    public SportEntity findById(Long sportId) {
        return sportRepository.findById(sportId)
                .orElseThrow(() -> new ResourceNotFoundException("Sport not found: " + sportId));
    }

    public SportEntity create(SportEntity sportEntity) {
        return sportRepository.save(sportEntity);
    }

    public SportEntity update(Long id, SportEntity updates) {
        SportEntity sport = findById(id);
        sport.setName(updates.getName());
        sport.setCode(updates.getCode());

        return sportRepository.save(sport);
    }

    public void delete(Long id) {
        if (!sportRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sport not found: " + id);
        }

        sportRepository.deleteById(id);
    }
}
