package com.hlh.repo;

import com.hlh.domain.entity.Function;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FunctionRepo extends JpaRepository<Function, Long> {

}
