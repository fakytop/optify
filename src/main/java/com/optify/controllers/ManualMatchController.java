package com.optify.controllers;

import com.optify.domain.DiscardReference;
import com.optify.dto.MatchesPendingDto;
import com.optify.exceptions.DataException;
import com.optify.facade.Facade;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/matching")
public class ManualMatchController {
    @Autowired
    private Facade instance;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<?> getAllPendingMatches() {
        List<MatchesPendingDto> matchesPending = instance.getAllPendingMatches()
                .stream().map(MatchesPendingDto::new).toList();
        if(matchesPending == null) {
            return ResponseEntity.badRequest().body("No se encontraron matches pendientes de realizar.");
        }
        return ResponseEntity.ok(matchesPending);
    }

    @PostMapping("/confirm")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<?> confirmMatch(@RequestParam int id) {
        try {
            instance.confirmMatch(id);
            return ResponseEntity.ok("Productos matcheados con éxito.");
        } catch (DataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/decline")
    public ResponseEntity<?> declineMatch(@RequestParam int id) {
        try {
            instance.declineMatch(id);
            return ResponseEntity.ok("Match eliminado correctamente.");
        } catch (DataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getAllDiscarded")
    public ResponseEntity<?> getAllDiscardedUrls() {
        try {
            List<DiscardReference> references = instance.getAllDiscardedUrls();
            return  ResponseEntity.ok(references);
        } catch (DataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/deleteReferenceDiscarded")
    public ResponseEntity<?> deleteDiscardedReference(int id) {
        try {
            instance.deleteDiscardedReference(id);
            return ResponseEntity.ok("Referencia eliminada con éxito.");
        } catch (DataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
