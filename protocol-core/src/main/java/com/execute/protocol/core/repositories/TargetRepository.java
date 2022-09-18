package com.execute.protocol.core.repositories;

import com.execute.protocol.core.entities.Target;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TargetRepository extends JpaRepository<Target, Long> {
}
