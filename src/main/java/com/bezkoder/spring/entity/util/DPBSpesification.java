package com.bezkoder.spring.entity.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.bezkoder.spring.entity.model.DataProduksiBeras;
import com.bezkoder.spring.entity.model.JenisBeras;
import com.bezkoder.spring.login.models.User;

public class DPBSpesification {
    public static Specification<User
    > containsTextInAttributes(String text, List<String> attributes) {
        if (!text.contains("%")) {
            text = "%" + text + "%";
        }
        String finalText = text;
        return (root, query, builder) -> builder.or(root.getModel().getDeclaredSingularAttributes().stream()
                .filter(a -> attributes.contains(a.getName()))
                .map(a -> builder.like(root.get(a.getName()), finalText))
                .toArray(Predicate[]::new)
        );
    }

    public static Specification<DataProduksiBeras> containsNested(String text) {
        return new Specification<DataProduksiBeras>() {
            @Override
            public Predicate toPredicate(Root<DataProduksiBeras> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                final List<Predicate> predicates = new ArrayList<>();
                Path<JenisBeras> u = root.get("jenisBeras");
                predicates.add(criteriaBuilder.like(u.get("nama"), "%"+text+"%"));
                Path<User> p = root.get("petani");
                predicates.add(criteriaBuilder.like(p.get("nama"), "%"+text+"%"));
                return criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

    public static Specification<DataProduksiBeras> containsNestedIsTerjualTrue(String text) {
        return new Specification<DataProduksiBeras>() {
            @Override
            public Predicate toPredicate(Root<DataProduksiBeras> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                // final List<Predicate> predicates = new ArrayList<>();
                Path<JenisBeras> u = root.get("jenisBeras");
                // predicates.add(criteriaBuilder.equal(root.get("isTerjual"), true));
                // predicates.add(criteriaBuilder.like(u.get("nama"), "%"+text+"%"));
                Path<User> p = root.get("petani");
                // predicates.add(criteriaBuilder.like(p.get("nama"), "%"+text+"%"));
                Predicate[] predicates = new Predicate[3];
                predicates[0] = criteriaBuilder.equal(root.get("isTerjual"), false);
                predicates[1] = criteriaBuilder.like(u.get("nama"), "%"+text+"%");
                predicates[2] = criteriaBuilder.like(p.get("nama"), "%"+text+"%");
                return criteriaBuilder.and(predicates[0] ,criteriaBuilder.or(predicates[1], predicates[2]));
            }
        };
    }
    
    public static Specification<User> containsTextInName(String text) {
        return containsTextInAttributes(text, Arrays.asList("roles", "jenis_kelamin"));
    }
}
