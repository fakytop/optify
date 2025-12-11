package com.optify.api;

import com.optify.domain.Store;
import com.optify.exceptions.DataException;
import com.optify.services.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/stores")
public class StoreController {

    @Autowired
    private StoreService storeService;

    // GET /stores: Listar todas las tiendas
    @GetMapping
    public ResponseEntity<List<Store>> getAllStores() {
        return new ResponseEntity<>(storeService.getAllStores(), HttpStatus.OK);
    }

    // POST /stores: Crear o Actualizar (UPSERT)
    @PostMapping
    public ResponseEntity<?> createOrUpdateStore(@RequestBody Store store) {
        try {
            // Llama al nuevo METODO unificado
            Store resultStore = storeService.createOrUpdateStore(store);

            // Podrías devolver 200 OK si se actualizó o 201 Created si se creó
            // Usamos 201 por simplicidad en el POST.
            return new ResponseEntity<>(resultStore, HttpStatus.CREATED);

        } catch (DataException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.err.println("Error interno al procesar la tienda: " + e.getMessage());
            return new ResponseEntity<>("Error interno al procesar la tienda.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ELIMINAMOS EL METODO PUT DE ESTE CONTROLADOR YA QUE POST HACE LA ACTUALIZACIÓN
}