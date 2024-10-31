package com.vasylenkob.pastebin.repo;

import com.vasylenkob.pastebin.entities.MetaData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MetaDataRepo extends JpaRepository<MetaData, Long> {
    List<MetaData> findAllByUserId(Long userId);
}
