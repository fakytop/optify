package com.optify.controllers;

import com.optify.dto.ProductDto;
import com.optify.exceptions.DataException;
import com.optify.facade.Facade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private Facade instance;

    @PostMapping("/import")
    public ResponseEntity<?> importProducts(@RequestBody List<ProductDto> dtos) {

        try {
            instance.importProductsBatch(dtos);
            return ResponseEntity.ok("Productos procesados [" + dtos.size() + "]");
        } catch (DataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
