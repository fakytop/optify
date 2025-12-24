package com.optify.controllers;

import com.optify.domain.Category;
import com.optify.domain.Product;
import com.optify.dto.CategoryDto;
import com.optify.dto.ProductDto;
import com.optify.exceptions.DataException;
import com.optify.facade.Facade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            return ResponseEntity.ok("[IMPORT] Productos procesados: {" + dtos.size() + "}");
        } catch (DataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getCategories() {
        List<Category> categories = instance.getAllCategories();
        List<CategoryDto> categoryDtos = categories.stream()
                .map(CategoryDto::new)
                .toList();

        return ResponseEntity.ok(categoryDtos);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<?> getProductsByCategoryId(@PathVariable int id) {
        try {
            List<Product> products = instance.getProductsByCategoryId(id);
            List<ProductDto> productDtos = products.stream()
                    .map(ProductDto::new)
                    .toList();

            return ResponseEntity.ok(productDtos);
        } catch (DataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchProductsByName(@RequestParam String term) {
        List<Product> products = instance.searchProductsByName(term);
        List<ProductDto> productDtos = products.stream()
                .map(ProductDto::new)
                .toList();
        return ResponseEntity.ok(productDtos);
    }
}
