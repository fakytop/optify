package com.optify.controllers;

import com.optify.domain.Category;
import com.optify.domain.Product;
import com.optify.dto.CategoryDto;
import com.optify.dto.ProductCatalogDto;
import com.optify.dto.ProductImportDto;
import com.optify.exceptions.DataException;
import com.optify.facade.Facade;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private Facade instance;

    @SecurityRequirement(name = "ApiKeyAuth")
    @PostMapping("/import")
    @PreAuthorize("hasRole('SCRIPT')")
    public ResponseEntity<?> importProducts(@RequestBody List<ProductImportDto> dtos) {

        try {
            instance.importProductsBatch(dtos);
            return ResponseEntity.ok("[IMPORT] Productos procesados: {" + dtos.size() + "}");
        } catch (DataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/categories")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<List<CategoryDto>> getCategories() {
        List<Category> categories = instance.getAllCategories();
        List<CategoryDto> categoryDtos = categories.stream()
                .map(CategoryDto::new)
                .toList();

        return ResponseEntity.ok(categoryDtos);
    }

    @GetMapping("/category/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<?> getProductsByCategoryId(@PathVariable int id, @PageableDefault(size = 20) Pageable pageable) {
        try {
            Page<Product> products = instance.getProductsByCategoryId(id,pageable);
            Page<ProductCatalogDto> productDtos = products.map(ProductCatalogDto::new);

            return ResponseEntity.ok(productDtos);
        } catch (DataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<?> searchProductsByName(@RequestParam String term,@PageableDefault(size = 20) Pageable pageable) {
        Page<Product> products = null;
        try {
            products = instance.searchProductsByName(term,pageable);
            Page<ProductCatalogDto> productDtos = products.map(ProductCatalogDto::new);
            return ResponseEntity.ok(productDtos);
        } catch (DataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/allProducts")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<?> getAllProducts(@PageableDefault(size = 20) Pageable pageable) {
        Page<Product> products = instance.getAllProducts(pageable);
        Page<ProductCatalogDto> productDtos = products.map(ProductCatalogDto::new);
        return ResponseEntity.ok(productDtos);
    }

    @PostMapping("/mergeProducts")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<?> mergeProducts(@RequestParam int keepProductId, @RequestParam int suprProductId) {
        try {
            instance.mergeProducts(keepProductId,suprProductId);
        } catch (DataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("Productos vinculados correctamente.");
    }
}
