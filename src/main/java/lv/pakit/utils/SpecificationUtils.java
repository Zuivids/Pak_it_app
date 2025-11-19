package lv.pakit.utils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class SpecificationUtils {

    public static <T> void addValueEqualTo(List<Predicate> predicates, Root<T> root, CriteriaBuilder cb, String field, Object value) {
        if (value != null) {
            predicates.add(cb.equal(root.get(field), value));
        }
    }

    public static <T> void addValueLike(List<Predicate> predicates, Root<T> root, CriteriaBuilder cb, String field, String value) {
        if (value != null && !value.isEmpty()) {
            predicates.add(cb.like(root.get(field), "%" + value + "%"));
        }
    }

    public static <T> void addDatetimeInDate(List<Predicate> predicates, Root<T> root, CriteriaBuilder cb, String field, LocalDate value) {
        if (value != null) {
            predicates.add(cb.greaterThan(root.get(field), value.atStartOfDay()));
            predicates.add(cb.lessThan(root.get(field), value.atTime(LocalTime.MAX)));
        }
    }
}
