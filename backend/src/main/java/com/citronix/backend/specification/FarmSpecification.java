package com.citronix.backend.specification;

import com.citronix.backend.dto.farm.request.FarmSearchCriteria;
import com.citronix.backend.entity.Farm;
import com.citronix.backend.entity.Field;
import com.citronix.backend.entity.Tree;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class FarmSpecification {

    public static Specification<Farm> withCriteria(FarmSearchCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Basic criteria
            if (criteria.getName() != null && !criteria.getName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")),
                        "%" + criteria.getName().toLowerCase() + "%"));
            }

            if (criteria.getLocation() != null && !criteria.getLocation().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("location")),
                        "%" + criteria.getLocation().toLowerCase() + "%"));
            }

            if (criteria.getMinTotalArea() != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        root.get("totalAreaInSquareMeters"), criteria.getMinTotalArea()));
            }

            if (criteria.getMaxTotalArea() != null) {
                predicates.add(cb.lessThanOrEqualTo(
                        root.get("totalAreaInSquareMeters"), criteria.getMaxTotalArea()));
            }

            // Fields count criteria using subquery
            if (criteria.getMinFields() != null || criteria.getMaxFields() != null) {
                Subquery<Long> fieldCountSubquery = query.subquery(Long.class);
                Root<Field> fieldRoot = fieldCountSubquery.from(Field.class);
                fieldCountSubquery.select(cb.count(fieldRoot));
                fieldCountSubquery.where(cb.equal(fieldRoot.get("farm"), root));

                if (criteria.getMinFields() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(fieldCountSubquery,
                            criteria.getMinFields().longValue()));
                }

                if (criteria.getMaxFields() != null) {
                    predicates.add(cb.lessThanOrEqualTo(fieldCountSubquery,
                            criteria.getMaxFields().longValue()));
                }
            }

            // Trees count criteria using subquery
            if (criteria.getMinTrees() != null || criteria.getMaxTrees() != null) {
                Subquery<Long> treeCountSubquery = query.subquery(Long.class);
                Root<Tree> treeRoot = treeCountSubquery.from(Tree.class);
                Join<Tree, Field> fieldJoin = treeRoot.join("field");
                treeCountSubquery.select(cb.count(treeRoot));
                treeCountSubquery.where(cb.equal(fieldJoin.get("farm"), root));

                if (criteria.getMinTrees() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(treeCountSubquery,
                            criteria.getMinTrees().longValue()));
                }

                if (criteria.getMaxTrees() != null) {
                    predicates.add(cb.lessThanOrEqualTo(treeCountSubquery,
                            criteria.getMaxTrees().longValue()));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}