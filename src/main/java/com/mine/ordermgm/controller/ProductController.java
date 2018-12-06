package com.mine.ordermgm.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mine.ordermgm.api.UpdateProductRequest;
import com.mine.ordermgm.model.Product;
import com.mine.ordermgm.service.ProductService;
import com.mine.ordermgm.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @stefanl
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private JsonUtil jsonUtil;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createProduct(@Valid @RequestBody Product product) throws JsonProcessingException {
        long id = productService.createProduct(product);
        if (id == -1) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(id));
        return new ResponseEntity<>(jsonUtil.writeValueAsString(map), null, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProducts() {
        return new ResponseEntity<>(productService.findAll(), null, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable long id,
                                                 @RequestBody UpdateProductRequest request) {
        Optional<Product> result = productService.updateProduct(id, request);
        if (!result.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result.get(), null, HttpStatus.OK);
    }
}
