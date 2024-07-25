package ru.siebel.kitchen.service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.siebel.kitchen.service.kafka.StockMessageProducer;
import ru.siebel.order.dto.Order;
import ru.siebel.stock.api.StockApi;
import ru.siebel.stock.dto.Stock;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KitchenService {

    private final StockApi stockApi;

    private final StockMessageProducer stockMessageProducer;
    Map<String, Integer> orderIngredients;
    public void cookOrder(Order dto) {
        orderIngredients = stockApi.getOrderIngredients(dto).getBody();
        if (orderIngredients != null) {
            for (String ingredient : orderIngredients.keySet()) {
                Stock currentStock = stockApi.getStockByValue(ingredient).getBody();
                if (currentStock != null) {
                if (currentStock.getStock() >= orderIngredients.get(ingredient)) {
                    currentStock.setStock(currentStock.getStock() - orderIngredients.get(ingredient));
                    currentStock.setReserved(currentStock.getReserved() - orderIngredients.get(ingredient));
                    sendStockMessage(currentStock);
                }
                }
            }
        }
    }

    public void reserveIngredients(Order dto){
        orderIngredients = stockApi.getOrderIngredients(dto).getBody();
        if (orderIngredients != null) {
            for (String ingredient : orderIngredients.keySet()) {
                Stock currentStock = stockApi.getStockByValue(ingredient).getBody();
                if (currentStock != null) {
                    if (currentStock.getStock() >= currentStock.getReserved() + orderIngredients.get(ingredient)) {
                        currentStock.setReserved(currentStock.getReserved() + orderIngredients.get(ingredient));
                        sendStockMessage(currentStock);
                    }
                }
            }
        }
    }

    private void sendStockMessage(Stock stock) {
        this.stockMessageProducer.sendRequest(stock);
    }
}
