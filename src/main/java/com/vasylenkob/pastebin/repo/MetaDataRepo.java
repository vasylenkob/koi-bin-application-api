package com.vasylenkob.pastebin.repo;

import com.vasylenkob.pastebin.models.entities.MetaData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetaDataRepo extends JpaRepository<MetaData, Long> {
}
