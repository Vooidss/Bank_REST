package com.example.bankcards.service;

import com.example.bankcards.dto.BankCardDTO;
import com.example.bankcards.dto.Requests.CreateCardRequest;
import com.example.bankcards.entity.BankCard;
import com.example.bankcards.entity.Status;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.mappers.BankCardMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CardEncryptionUtil;
import com.example.bankcards.util.CardNumberGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final BankCardMapper bankCardMapper;
    private final CardEncryptionUtil cardEncryptionUtil;
    private final UserService userService;

    public CardService(CardRepository cardRepository, UserRepository userRepository, BankCardMapper bankCardMapper, CardEncryptionUtil cardEncryptionUtil, UserService userService) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.bankCardMapper = bankCardMapper;
        this.cardEncryptionUtil = cardEncryptionUtil;
        this.userService = userService;
    }


    public List<BankCardDTO> getAll() {
        List<BankCard> cards = cardRepository.findAll();

        if(cards.isEmpty()){
            return null;
        }

        return cards.stream().map(bankCardMapper::toDto).toList();
    }

    @Transactional
    public BankCardDTO create(CreateCardRequest request) {

        String cardNumber = CardNumberGenerator.generateCardNumber();
        String encrypt = cardEncryptionUtil.encrypt(cardNumber);
        String maskCardNumber = cardEncryptionUtil.maskEncrypted(encrypt);
        System.out.println(maskCardNumber);

        BankCard card = new BankCard();

        try {
            User user = userRepository.findById(request.getOwnerId()).orElseThrow(() -> new CardNotFoundException("Пользователь с таким ID не найден"));
            card.setOwner(user);
            card.setCardNumber(maskCardNumber);
        }catch (CardNotFoundException e){
            throw new CardNotFoundException(e.getMessage());
        }

        cardRepository.save(card);

        return bankCardMapper.toDto(card);
    }


    public BankCardDTO blocked(Long id) {
            return changeStatusCard(Status.BLOCKED, id);
    }


    public BankCardDTO activate(Long id) {
            return changeStatusCard(Status.ACTIVE, id);
    }


    public void delete(Long id) {
        if (!cardRepository.existsById(id)) {
            throw new CardNotFoundException("Банковская карточка не найдена");
        }

        cardRepository.deleteById(id);
    }

    @Transactional
    protected BankCardDTO changeStatusCard(Status status, Long id){

            BankCard card = cardRepository.findById(id)
                    .orElseThrow(() -> new CardNotFoundException("Карта с таким ID не найдена"));

            card.setStatus(status);
            cardRepository.save(card);

        return bankCardMapper.toDto(card);
    }

    public Page<BankCardDTO> getAllCurrentUser(int pageNumber, int pageSize) {
        Long userId = userService.getCurrentUser().getId();
        Page<BankCard> bankCards = cardRepository.findAllByOwnerId(PageRequest.of(pageNumber, pageSize), userId);

        return bankCards.map(bankCardMapper::toDto);
    }
}
