package com.hlh.repo;

import com.hlh.domain.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepo extends JpaRepository<Type, Long> {

    Type findFirstByNameAndParentIdIsNullAndParentIdsIsNull(String name);

    Type findFirstByNameAndParentId(String name, Long parentId);

}
