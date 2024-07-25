package ru.siebel.kitchen.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.siebel.order.dto.Order;

import java.util.Map;

@FeignClient(
        name = "KitchenApi",
        url = "localhost:7666"
)
public interface KitchenApi {

    @PostMapping("/cookOrder")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cookOrder(@RequestBody Order dto);

    @PostMapping("/reserveIngredients")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reserveIngredients(@RequestBody Order dto);
}