package com.example.bankcards.service;

import com.example.bankcards.dto.BankCardDTO;
import com.example.bankcards.dto.CardBalanceDto;
import com.example.bankcards.dto.TotalCardBalanceDTO;
import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.bankcard.BankCard;
import com.example.bankcards.entity.user.User;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.CustomUserNotFoundException;
import com.example.bankcards.mappers.BankCardBalanceMapper;
import com.example.bankcards.mappers.UserMapper;
import com.example.bankcards.repository.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BankCardBalanceMapper bankCardBalanceMapper;
    private final CardService cardService;

    public UserService(UserRepository userRepository, UserMapper userMapper, BankCardBalanceMapper bankCardBalanceMapper, @Lazy CardService cardService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.bankCardBalanceMapper = bankCardBalanceMapper;
        this.cardService = cardService;
    }

    @Cacheable(cacheNames = "users")
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    @Cacheable(cacheNames = "currentUser")
    public User getCurrentUser(){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        return getUserByUsername(userName);
    }

    public Page<UserDTO> getUserByUsername(int pageNumber, int pageSize) {

        Page<User> users = userRepository.findAll(PageRequest.of(pageNumber, pageSize));

        if(users.isEmpty()){
            throw new CustomUserNotFoundException("Пользователей пока нет.");
        }

        return users.map(userMapper::toDto);
    }

    public void delete(Long id) {
        try{
            userRepository.deleteById(id);
        }catch (CustomUserNotFoundException e){
            throw new CustomUserNotFoundException(String.format("Пользователя с id %d не существует", id));
        }
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomUserNotFoundException(String.format("Пользователя с id %d не существует", id)));

        return userMapper.toDto(user);
    }

    public TotalCardBalanceDTO getTotalBalance(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomUserNotFoundException(String.format("Пользователя с id %d не существует", userId)));

        List<BankCardDTO> cards = cardService.getAllCurrentUser();
        List<CardBalanceDto> cardsBalance = cards.stream()
                .map(bankCardBalanceMapper::toDto)
                .toList();

        if(!cardsBalance.isEmpty()) {
            BigDecimal totalBalance = cardsBalance.stream()
                    .map(CardBalanceDto::getBalance)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            return TotalCardBalanceDTO
                    .builder()
                    .userId(user.getId())
                    .cardBalances(cardsBalance)
                    .totalBalance(totalBalance)
                    .build();
        }else{
            throw new CardNotFoundException("У пользователя нет карт, поэтому баланс не может быть рассчитан.");
        }
    }
}
