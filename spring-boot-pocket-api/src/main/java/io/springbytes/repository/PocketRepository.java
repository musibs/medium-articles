package io.springbytes.repository;

import io.springbytes.model.Pocket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PocketRepository extends CrudRepository<Pocket, Long> {
}
