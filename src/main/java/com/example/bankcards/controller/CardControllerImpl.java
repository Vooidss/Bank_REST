package com.example.bankcards.controller;


import com.example.bankcards.controller.interfaces.CardController;
import com.example.bankcards.dto.BankCardDTO;
import com.example.bankcards.dto.CardBalanceDto;
import com.example.bankcards.dto.Requests.CreateCardRequest;
import com.example.bankcards.dto.Requests.ReplenishRequest;
import com.example.bankcards.dto.Responses.*;
import com.example.bankcards.service.CardService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CardControllerImpl implements CardController {

    private final CardService cardService;

    public CardControllerImpl(CardService cardService) {
        this.cardService = cardService;
    }

    @Override
    public ResponseEntity<CardsResponse> getAll(int pageNumber, int pageSize) {
        Page<BankCardDTO> cards = cardService.getAll(pageNumber, pageSize);
        return ResponseEntity.ok().body(new CardsResponse(cards, "Все банковские карты успешно вовзвращены", HttpStatus.OK));
    }

    @Override
    public ResponseEntity<CardsResponse> getAllCurrentUser(Long id,int pageNumber, int pageSize) {
        Page<BankCardDTO> bankCards = cardService.getAllCurrentUser(pageNumber, pageSize, id);
        return ResponseEntity.ok().body(new CardsResponse(bankCards, "Ваши банковские карты успешно получены!", HttpStatus.OK));
    }

    @Override
    public ResponseEntity<CardResponse> getById(Long id) {
        BankCardDTO bankCardDTO = cardService.getById(id);

        return ResponseEntity.ok().body(new CardResponse(bankCardDTO, "Банковская карточка получена!", HttpStatus.OK));
    }

    @Override
    public ResponseEntity<CardResponse> create(CreateCardRequest request) {
            BankCardDTO bankCardDTO = cardService.create(request);
        return ResponseEntity.ok().body(new CardResponse(bankCardDTO, "Банковская кароточка успещно создана!", HttpStatus.OK));
    }


    @Override
    public ResponseEntity<CardResponse> blocked(Long id) {
            BankCardDTO bankCardDTO = cardService.blocked(id);
            return ResponseEntity.ok().body(new CardResponse(bankCardDTO, "Банковская кароточка успешно заблокирована!", HttpStatus.OK));
    }

    @Override
    public ResponseEntity<CardResponse> activate(Long id) {
            BankCardDTO bankCardDTO = cardService.activate(id);
            return ResponseEntity.ok().body(new CardResponse(bankCardDTO, "Банковская кароточка успещно активирована!", HttpStatus.OK));

    }

    @Override
    public ResponseEntity<Response<Void>> delete(Long id) {
            cardService.delete(id);
            return ResponseEntity.ok().body(Response.of( "Банковская кароточка успещно удалена!", HttpStatus.OK));
    }

    @Override
    public ResponseEntity<BalanceResponse> getBalance(Long userId, Long cardId) {
        CardBalanceDto balance = cardService.getBalance(userId, cardId);
        return ResponseEntity.ok().body(new BalanceResponse( balance, "Баланс карточки успешно получен!", HttpStatus.OK));
    }

    @Override
    public ResponseEntity<BalanceResponse> replenish(Long id, ReplenishRequest request) {
        CardBalanceDto balance = cardService.deposit(id ,request.getAmount());

        return ResponseEntity.ok().body(new BalanceResponse( balance, "Банковская карта пополнена!", HttpStatus.OK));
    }
}
