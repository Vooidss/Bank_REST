package com.example.bankcards.controller;

import com.example.bankcards.controller.interfaces.UserController;
import com.example.bankcards.dto.Responses.Response;
import com.example.bankcards.dto.Responses.TotalBalanceResponse;
import com.example.bankcards.dto.Responses.UsersResponse;
import com.example.bankcards.dto.TotalCardBalanceDTO;
import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserControllerImpl implements UserController {

    private final UserService userService;

    public UserControllerImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<UsersResponse> getAll(int pageNumber, int pageSize) {
        Page<UserDTO> userDTO = userService.getUserByUsername(pageNumber, pageSize);
        return ResponseEntity.ok().body(new UsersResponse(userDTO,"Пользователи успешно вовзращены", HttpStatus.OK));
    }

    @Override
    public ResponseEntity<Response<Void>> deleteUser(Long id) {
        userService.delete(id);
        return ResponseEntity.ok().body(Response.of("Пользователь успешно удалён", HttpStatus.OK));
    }

    @Override
    public ResponseEntity<TotalBalanceResponse> getTotalBalance(Long userId) {
        TotalCardBalanceDTO totalCardBalanceDTO = userService.getTotalBalance(userId);
        return ResponseEntity.ok().body(new TotalBalanceResponse(totalCardBalanceDTO,"Общий баланс пользователя получен", HttpStatus.OK));
    }
}
