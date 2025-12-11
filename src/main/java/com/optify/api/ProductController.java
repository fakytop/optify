package com.optify.api;

import com.optify.domain.Product;
import com.optify.dto.ProductPriceRequest;
import com.optify.exceptions.DataException;
import com.optify.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // --- NUEVO MÉTODO: GET /products ---
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        try {
            List<Product> products = productService.findAllProducts();

            if (products.isEmpty()) {
                // Devuelve 204 No Content si no hay productos
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            // Devuelve 200 OK con la lista de productos
            return new ResponseEntity<>(products, HttpStatus.OK);

        } catch (Exception e) {
            System.err.println("Error al obtener productos: " + e.getMessage());
            // Devuelve 500 Internal Server Error
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/capture")
    public ResponseEntity<String> receivePriceCapture(@RequestBody List<ProductPriceRequest> captureRequests) {
        if (captureRequests == null || captureRequests.isEmpty()) {
            return new ResponseEntity<>("La lista de productos no puede estar vacía.", HttpStatus.BAD_REQUEST);
        }

        int successfulCaptures = 0;

        for (ProductPriceRequest captureRequest : captureRequests) {
            try {
                productService.processPriceCapture(captureRequest);
                successfulCaptures++;
            } catch (DataException e) {
                System.err.println("Error de datos (400) al procesar GTIN: " + captureRequest.getGtin() + ". Mensaje: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error interno (500) al procesar GTIN: " + captureRequest.getGtin() + ". Mensaje: " + e.getMessage());
            }
        }

        String responseMessage = String.format("Lote procesado. Éxitos: %d. Fallos: %d.",
                successfulCaptures,
                captureRequests.size() - successfulCaptures);

        if (successfulCaptures == 0 && !captureRequests.isEmpty()) {
            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(responseMessage, HttpStatus.CREATED);
    }
}