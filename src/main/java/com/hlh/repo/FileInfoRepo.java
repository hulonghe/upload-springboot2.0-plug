package com.hlh.repo;

import com.hlh.domain.entity.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileInfoRepo extends JpaRepository<FileInfo, Long> {
    List<FileInfo> findAllByParentAndParentIdAndFlagAndIsDel(String parent, String pId, boolean flag, boolean del);

    List<FileInfo> findAllByIdIn(List<String> ids);

    List<FileInfo> findAllByParentAndParentIdInAndFlagAndIsDel(String parent, List<String> parentIds, boolean flag, boolean del);
}
