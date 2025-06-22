package com.example.bankcards.service;

import com.example.bankcards.dto.Requests.TransferUserRequest;
import com.example.bankcards.dto.Responses.TransferResponse;
import com.example.bankcards.dto.Responses.TransfersResponse;
import com.example.bankcards.dto.TransferUserDto;
import com.example.bankcards.entity.bankcard.BankCard;
import com.example.bankcards.entity.transfer.Transfer;
import com.example.bankcards.entity.transfer.TransferStatus;
import com.example.bankcards.entity.user.User;
import com.example.bankcards.exception.CardAccessDeniedException;
import com.example.bankcards.exception.TransferNotFoundException;
import com.example.bankcards.mappers.BankCardMapper;
import com.example.bankcards.mappers.TransferMapper;
import com.example.bankcards.mappers.UserMapper;
import com.example.bankcards.repository.TransferRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferService {

    private final CardService cardService;
    private final UserService userService;
    private final BankCardMapper bankCardMapper;
    private final UserMapper userMapper;
    private final TransferMapper transferMapper;
    private final TransferRepository transferRepository;

    public TransferService(CardService cardService, UserService userService, BankCardMapper bankCardMapper, UserMapper userMapper, TransferMapper transferMapper, TransferRepository transferRepository) {
        this.cardService = cardService;
        this.userService = userService;
        this.bankCardMapper = bankCardMapper;
        this.userMapper = userMapper;
        this.transferMapper = transferMapper;
        this.transferRepository = transferRepository;
    }

    @Transactional
    public TransferUserDto transferFromToCardUser(Long userId, TransferUserRequest request) {

        Long fromCardId = request.getFromCardId();
        Long toCardId = request.getToCardId();
        BigDecimal amount = request.getAmount();

        if (!cardService.isOwnerCard(userId, fromCardId)) {
            throw new CardAccessDeniedException("Карта-отправитель вам не принадлежит");
        }

        if (!cardService.isOwnerCard(userId, toCardId)) {
            throw new CardAccessDeniedException("Карта-получатель вам не принадлежит");
        }

        BankCard fromCard = bankCardMapper.toEntity(cardService.getById(fromCardId));
        BankCard toCard = bankCardMapper.toEntity(cardService.getById(toCardId));
        User initiator = userMapper.toEntity(userService.getUserById(userId));

        Transfer transfer = Transfer.builder()
                .fromCard(fromCard)
                .toCard(toCard)
                .initiator(initiator)
                .amount(amount)
                .build();

        transferRepository.save(transfer);

        try {
            cardService.withdraw(fromCardId, amount);
            cardService.deposit(toCardId, amount);
            transfer.setStatus(TransferStatus.COMPLETED);
        } catch (RuntimeException ex) {
            transfer.setStatus(TransferStatus.CANCELLED);
            throw ex;
        }

        transferRepository.save(transfer);
        return transferMapper.toDto(transfer);
    }

    public Page<TransferUserDto> getAll(int pageNumber, int pageSize) {

        Page<Transfer> transfers = transferRepository.findAll(PageRequest.of(pageNumber, pageSize));

        return transfers.map(transferMapper::toDto);
    }

    public TransferUserDto getById(Long id) {

        Transfer transfer = transferRepository.findById(id)
                .orElseThrow(() -> new TransferNotFoundException(String.format("Перевода с id %d не найден", id)));

        return transferMapper.toDto(transfer);
    }
}
