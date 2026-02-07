package com.optify.controllers;

import com.optify.domain.Category;
import com.optify.domain.Product;
import com.optify.domain.StoreProduct;
import com.optify.domain.StoreProductPk;
import com.optify.dto.*;
import com.optify.exceptions.DataException;
import com.optify.facade.Facade;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @PostMapping("/manualImport")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<?> manualImportProducts(@RequestBody ProductImportDto dto) {
        try {
            instance.importNewProduct(dto);
            return ResponseEntity.ok("Producto creado correctamente.");
        } catch (DataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/manualImportWithId")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<?> manualImportWithId(@RequestBody ProductManualImportDto dto) {
        try {
            instance.importNewProductWithId(dto);
            return ResponseEntity.ok("Producto creado correctamente.");
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

    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "BearerAuth")
    @PostMapping("/changeProductReference")
    public ResponseEntity<?> changeProductReference(@RequestParam int newProductId, @RequestParam int oldProductid, @RequestParam long storeRut) {
        StoreProductPk spId = new StoreProductPk(storeRut,oldProductid);
        try {
            instance.changeStoreProductReference(spId,newProductId);
            return ResponseEntity.ok("Productos vinculados correctamente.");
        } catch (DataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "BearerAuth")
    @DeleteMapping("/deleteProductReference")
    public ResponseEntity<?> deleteProductReference(@RequestParam int productId, @RequestParam long storeRut) {
        StoreProductPk spId = new StoreProductPk(storeRut,productId);
        try {
            instance.deleteStoreProduct(spId);
            return ResponseEntity.ok("El producto id: {" + productId +
                    "} ha sido desvinculado del supermercado con RUT: {" + storeRut +
                            "} correctamente.");
        } catch (DataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "BearerAuth")
    @GetMapping("/getStoreProductsByProduct")
    public ResponseEntity<?> getStoreProductsByProductId(@RequestParam int productId) {
        List<StoreProduct> storeProducts = instance.getStoreProductByProductId(productId);
        if(storeProducts == null || storeProducts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<StoreProductDto> dtos = storeProducts.stream().map(StoreProductDto::new).toList();
        return ResponseEntity.ok(dtos);
    }
}
