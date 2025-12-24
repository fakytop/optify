package com.optify.specifications;

import com.optify.domain.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecifications {
    public static Specification<Product> searchByNameMultiWord(String searchTerm) {
        return (root,query,criteriaBuilder) -> {
            if(searchTerm == null || searchTerm.isBlank()) {
                return criteriaBuilder.conjunction(); //siempre verdadero. TODO: Modificar para no traer nada.
            }
            String[] words = searchTerm.toLowerCase().split("\\s");
            List<Predicate> predicates = new ArrayList<>();

            for(String word : words) {
                Predicate nameLike = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + word + "%"
                );
                predicates.add(nameLike);
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
