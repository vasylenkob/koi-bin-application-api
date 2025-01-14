package com.vasylenkob.pastebin.services;

import com.vasylenkob.pastebin.entities.MetaData;
import com.vasylenkob.pastebin.repo.MetaDataRepo;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.Meta;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MetaDataService {

    private final MetaDataRepo metaDataRepo;

    public MetaData saveMetaData(MetaData metaData){
        return metaDataRepo.save(metaData);
    }


    public void deleteMetaData(Long metaId){
        metaDataRepo.deleteById(metaId);
    }
    public void deleteMetaData(Long userId, Long metaId){
        metaDataRepo.deleteAllByUserIdAndMetaId(userId, metaId);
    }


    public List<MetaData> getAllMetaDataByUserId(Long userId){
        return metaDataRepo.findAllByUserId(userId);
    }
    public List<MetaData> getAllMetaData(){
        return metaDataRepo.findAll();
    }


    public Optional<MetaData> getMetaData(Long metaId){
        return metaDataRepo.findById(metaId);
    }
    public Optional<MetaData> getMetaData(Long userId, Long metaId) throws IllegalArgumentException{
        return metaDataRepo.findByUserIdAndMetaId(userId, metaId);
    }
}
