package betcore.controller;

import betcore.dto.TransactionResponse;
import betcore.dto.WalletRequest;
import betcore.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players/{playerId}/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/deposit")
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse deposit(@PathVariable Long playerId, @Valid @RequestBody WalletRequest walletRequest){
        return walletService.deposit(playerId, walletRequest);
    }

    @PostMapping("/withdraw")
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse withdraw(@PathVariable Long playerId, @Valid @RequestBody WalletRequest walletRequest){
        return walletService.withdraw(playerId, walletRequest);
    }

    @GetMapping("/transactions")
    public List<TransactionResponse> getTransactions(@PathVariable Long playerId){
        return walletService.getPlayerTransactions(playerId);
    }
}
