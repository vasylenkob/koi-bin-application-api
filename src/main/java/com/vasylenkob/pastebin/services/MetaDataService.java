package com.vasylenkob.pastebin.services;

import com.vasylenkob.pastebin.entities.MetaData;
import com.vasylenkob.pastebin.repo.MetaDataRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class MetaDataService {

    private final MetaDataRepo metaDataRepo;

    public MetaData saveMetaData(MetaData metaData){
        return metaDataRepo.save(metaData);
    }

    public Optional<MetaData> getMetaData(Long postId){
        return metaDataRepo.findById(postId);
    }

    public void deleteMetaDataById(Long metaId){
        metaDataRepo.deleteById(metaId);
    }
}
