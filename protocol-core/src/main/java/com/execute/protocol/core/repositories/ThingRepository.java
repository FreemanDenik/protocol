package com.execute.protocol.core.repositories;

import com.execute.protocol.core.entities.Thing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ThingRepository extends JpaRepository<Thing, Integer> {
    Set<Thing> findByIdIn(Set<Integer> ids);
}
