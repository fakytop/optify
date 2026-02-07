package com.optify.services;

import com.optify.domain.DiscardReference;
import com.optify.exceptions.DataException;
import com.optify.repository.DiscardReferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiscardReferenceService {
    @Autowired
    private DiscardReferenceRepository discardReferenceRepository;

    public List<DiscardReference> getAllDiscardadedReferences() throws DataException {
        List<DiscardReference> discradedReferences = discardReferenceRepository.findAll();
        if (discradedReferences.isEmpty()) {
            throw new DataException("No se encontraron referencias descartadas.");
        }
        return discradedReferences;
    }

    public void deleteDiscardedReference(int id) throws DataException {
        Optional<DiscardReference> reference = discardReferenceRepository.findById(id);
        if(!reference.isPresent()) {
            throw new DataException("No se encontraron referencias descartadas con id {" + id + "}");
        }
        discardReferenceRepository.delete(reference.get());
    }

    public void addDiscardedReference(String url) {
        Optional<DiscardReference> reference = discardReferenceRepository.findByUrl(url);
        DiscardReference discardReference = new DiscardReference(url);
        if(!reference.isPresent()) {
            discardReferenceRepository.save(discardReference);
        }
    }
}
