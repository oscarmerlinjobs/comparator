package com.waes.compare.repositories;

import com.waes.compare.entities.Comparison;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComparisonRepository extends CrudRepository<Comparison, String> {
}
