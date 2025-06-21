package com.example.bankcards.controller;


import com.example.bankcards.controller.interfaces.CardController;
import com.example.bankcards.dto.BankCardDTO;
import com.example.bankcards.dto.Requests.CreateCardRequest;
import com.example.bankcards.dto.Responses.CardResponse;
import com.example.bankcards.dto.Responses.CardsResponse;
import com.example.bankcards.dto.Responses.Response;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.service.CardService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CardControllerImpl implements CardController {

    private final CardService cardService;

    public CardControllerImpl(CardService cardService) {
        this.cardService = cardService;
    }

    @Override
    public ResponseEntity<Response<List<BankCardDTO>>> getAll() {

        List<BankCardDTO> cards = cardService.getAll();

        if(cards.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CardsResponse.of(cards, "Банковских карт пока нет", HttpStatus.NOT_FOUND));
        }

        return ResponseEntity.ok().body(CardsResponse.of(cards, "Все банковские карты успешно вовзвращены", HttpStatus.OK));
    }

    @Override
    public ResponseEntity<Response<Page<BankCardDTO>>> getAllCurrentUser(int pageNumber, int pageSize) {

        Page<BankCardDTO> bankCards = cardService.getAllCurrentUser(pageNumber, pageSize);

        if(bankCards.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CardsResponse.of(bankCards, "Банковских карт пока нет", HttpStatus.NOT_FOUND));
        }

        return ResponseEntity.ok().body(CardsResponse.of(bankCards, "Ваши банковские карты успешно получены!", HttpStatus.OK));
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
}
