package com.vasylenkob.pastebin.repo;

import com.vasylenkob.pastebin.entities.MetaData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MetaDataRepo extends JpaRepository<MetaData, Long> {
    List<MetaData> findAllByUserId(Long userId);
    Optional<MetaData> findByUserIdAndMetaId(Long userId, long metaId);
    void deleteAllByUserIdAndMetaId (Long userId, long metaId);
}
