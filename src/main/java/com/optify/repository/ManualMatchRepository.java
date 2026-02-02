package com.optify.repository;

import com.optify.domain.ManualMatchPending;
import com.optify.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManualMatchRepository extends JpaRepository<ManualMatchPending,Integer> {
    boolean existsByStoreAndIdWeb(Store store, long idWeb);
}
