package com.example.bankcards.controller.interfaces;

import com.example.bankcards.dto.Requests.CreateCardRequest;

import com.example.bankcards.dto.Responses.BalanceResponse;
import com.example.bankcards.dto.Responses.CardResponse;
import com.example.bankcards.dto.Responses.CardsResponse;
import com.example.bankcards.dto.Responses.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/v1/cards")
@Tag(name = "Card", description = "Операции с картами банка")
public interface CardController {

    @Operation(
            summary     = "Получить все карты",
            description = "Возвращает список всех банковских карт",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description  = "Список карт получен успешно",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = CardsResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description  = "Доступ запрещён: недостаточно прав",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    )
            }
    )
    @GetMapping("/all")
    @Secured("ADMIN")
    ResponseEntity<CardsResponse> getAll(
            @RequestParam(name = "page", defaultValue = "0") int pageNumber,
            @RequestParam(name = "size", defaultValue = "5") int pageSize
    );

    @Operation(
            summary     = "Получить все карты текущего пользователя",
            description = "Возвращает список банковских карт, принадлежащих указанному пользователю",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description  = "Список карт получен успешно",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = CardsResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description  = "Доступ запрещён: недостаточно прав",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    )
            }
    )
    @GetMapping("/all/{userId}")
    @PreAuthorize("#userId == authentication.principal.id")
    ResponseEntity<CardsResponse> getAllCurrentUser(
            @RequestParam(name = "page", defaultValue = "0") int pageNumber,
            @RequestParam(name = "size", defaultValue = "5") int pageSize,
            @PathVariable Long userId
    );

    @Operation(
            summary     = "Создать новую карту",
            description = "Принимает параметры карты и создаёт новую запись",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description  = "Карта создана успешно",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = CardsResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description  = "Неверные или неполные параметры запроса",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description  = "Доступ запрещён: недостаточно прав",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    )
            }
    )
    @PostMapping("/create")
    @Secured("ADMIN")
    ResponseEntity<CardResponse> create(@RequestBody CreateCardRequest request);

    @Operation(
            summary     = "Заблокировать карту",
            description = "Блокирует карту по её идентификатору",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description  = "Карта успешно заблокирована",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = CardResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description  = "Карта с указанным ID не найдена",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description  = "Доступ запрещён: недостаточно прав",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    )
            }
    )
    @PatchMapping("/blocked/{id}")
    @Secured("ADMIN")
    ResponseEntity<CardResponse> blocked(@PathVariable Long id);

    @Operation(
            summary     = "Активировать карту",
            description = "Активирует ранее созданную или заблокированную карту",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description  = "Карта успешно активирована",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = CardResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description  = "Карта с указанным ID не найдена",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description  = "Доступ запрещён: недостаточно прав",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    )
            }
    )
    @PatchMapping("/activate/{id}")
    @Secured("ADMIN")
    ResponseEntity<CardResponse> activate(@PathVariable Long id);

    @Operation(
            summary     = "Удалить карту",
            description = "Удаляет карту по её идентификатору",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description  = "Карта успешно удалена",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = CardResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description  = "Карта с указанным ID не найдена",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description  = "Доступ запрещён: недостаточно прав",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    )
            }
    )
    @DeleteMapping("/{id}")
    @Secured("ADMIN")
    ResponseEntity<Response<Void>> delete(@PathVariable Long id);

    @Operation(
            summary     = "Получить баланс карты",
            description = "Возвращает текущий баланс указанной карты пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description  = "Баланс карты получен",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = BalanceResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description  = "Доступ запрещён: недостаточно прав",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description  = "Карта не найдена",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = Response.class)
                            )
                    )
            }
    )
    @GetMapping("/balance/{cardId}/user/{userId}")
    @PreAuthorize("#userId == authentication.principal.id and @cardService.isOwnerCard(#userId, #cardId)")
    ResponseEntity<BalanceResponse> getBalance(
            @PathVariable Long userId,
            @PathVariable Long cardId
    );
}