package ru.siebel.kitchen.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.siebel.kitchen.api.KitchenApi;
import ru.siebel.kitchen.service.service.KitchenService;
import ru.siebel.order.dto.Order;

@RestController
@RequiredArgsConstructor
public class KitchenController implements KitchenApi {

    private final KitchenService kitchenService;

    @Override
    public void cookOrder(Order dto) {
        kitchenService.cookOrder(dto);
    }

    @Override
    public void reserveIngredients(Order dto) {
        kitchenService.reserveIngredients(dto);
    }
}
