package com.vasylenkob.pastebin.repo;

import com.vasylenkob.pastebin.models.entities.MetaData;
import org.springframework.data.repository.CrudRepository;

public interface MetaDataRepo extends CrudRepository <MetaData, Long> {
}
