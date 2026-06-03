package betcore.service;

import betcore.dto.TransactionResponse;
import betcore.dto.WalletRequest;
import betcore.entity.PlayerEntity;
import betcore.entity.TransactionEntity;
import betcore.exception.InsufficientBalanceException;
import betcore.repository.PlayerRepository;
import betcore.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletService {

    private final PlayerRepository playerRepository;
    private final TransactionRepository transactionRepository;
    private final PlayerService playerService;

    @Transactional
    public TransactionResponse deposit(Long playerId, WalletRequest walletRequest) {
        PlayerEntity player = playerService.findById(playerId);
        BigDecimal before = player.getBalance();
        player.setBalance(before.add(walletRequest.getAmount()));
        playerRepository.save(player);

        TransactionEntity transactionEntity = createTransaction(
                player,
                TransactionEntity.TransactionType.DEPOSIT,
                walletRequest.getAmount(),
                before,
                player.getBalance(),
                walletRequest.getDescription() != null ? walletRequest.getDescription() : "Deposit"
        );

        log.info("Depoist: playerId={}, amount={}, newBalance={}", playerId, walletRequest.getAmount(), player.getBalance());

        return TransactionResponse.form(transactionEntity);
    }

    @Transactional
    public TransactionResponse withdraw(Long playerId, WalletRequest walletRequest) {
        PlayerEntity player = playerService.findById(playerId);

        if (player.getBalance().compareTo(walletRequest.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance, Current: " + player.getBalance() + ", Requested: " + walletRequest.getAmount());
        }

        BigDecimal before = player.getBalance();
        player.setBalance(before.subtract(walletRequest.getAmount()));
        playerRepository.save(player);

        TransactionEntity transactionEntity = createTransaction(
                player,
                TransactionEntity.TransactionType.WITHDRAWAL,
                walletRequest.getAmount(),
                before,
                player.getBalance(),
                walletRequest.getDescription() != null ? walletRequest.getDescription() : "Withdraw"
        );

        log.info("Withdrawal: playerId={}, amount={}, newBalance={}", playerId, walletRequest.getAmount(), player.getBalance());

        return TransactionResponse.form(transactionEntity);
    }

    private TransactionEntity createTransaction(
            PlayerEntity player,
            TransactionEntity.TransactionType type,
            BigDecimal amount,
            BigDecimal before,
            BigDecimal after,
            String description
    ) {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setPlayer(player);
        transactionEntity.setAmount(amount);
        transactionEntity.setType(type);
        transactionEntity.setBalanceBefore(before);
        transactionEntity.setBalanceAfter(after);
        transactionEntity.setDescription(description);
        return transactionRepository.save(transactionEntity);
    }
}
