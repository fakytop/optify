package com.optify.repository;

import com.optify.domain.ManualMatchPending;
import com.optify.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ManualMatchRepository extends JpaRepository<ManualMatchPending,Integer> {
    boolean existsByStoreAndIdWeb(Store store, long idWeb);
    Optional<ManualMatchPending> findById(int id);
    List<ManualMatchPending> findByProductId(long id);
}
