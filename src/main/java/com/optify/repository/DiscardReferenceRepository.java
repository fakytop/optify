package com.optify.repository;

import com.optify.domain.DiscardReference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscardReferenceRepository extends JpaRepository<DiscardReference, Integer> {
    Optional<DiscardReference> findByUrl(String url);
}
