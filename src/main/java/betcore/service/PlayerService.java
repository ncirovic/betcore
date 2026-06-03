package betcore.service;

import betcore.dto.PlayerRequest;
import betcore.dto.PlayerResponse;
import betcore.entity.PlayerEntity;
import betcore.exception.DuplicateResourceException;
import betcore.exception.ResourceNotFoundException;
import betcore.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;
    public List<PlayerResponse> findAll() {
        return playerRepository.findAll().stream()
                .map(PlayerResponse::form)
                .toList();
    }

    public PlayerEntity findById(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found with id: " + id));
    }

    public PlayerResponse findResponseById(Long id) {
        return PlayerResponse.form(findById(id));
    }

    public PlayerResponse create(PlayerRequest playerRequest) {
        if (playerRepository.existsByUsername(playerRequest.getUsername())) {
            throw new DuplicateResourceException("Username already taken: " + playerRequest.getUsername());
        }
        if (playerRepository.existsByEmail(playerRequest.getEmail())) {
            throw new DuplicateResourceException("Email already registered: " + playerRequest.getEmail());
        }

        PlayerEntity playerEntity = new PlayerEntity();
        playerEntity.setUsername(playerRequest.getUsername());
        playerEntity.setEmail(playerRequest.getEmail());
        playerEntity.setPassword(playerRequest.getPassword());
        return PlayerResponse.form(playerRepository.save(playerEntity));
    }
}
