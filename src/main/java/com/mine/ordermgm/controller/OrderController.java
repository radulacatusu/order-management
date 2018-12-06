package com.mine.ordermgm.controller;

import com.mine.ordermgm.model.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @stefanl
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createOrder(@Valid @RequestBody Order order) {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<String> getOrders(@NotNull @RequestParam("start") Date startDate,
                                            @NotNull @RequestParam("end") Date endDate) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/calculate")
    public ResponseEntity<String> calculateAmount(@PathVariable long id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
