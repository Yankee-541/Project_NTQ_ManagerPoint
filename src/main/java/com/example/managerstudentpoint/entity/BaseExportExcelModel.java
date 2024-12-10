package com.example.managerstudentpoint.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class BaseExportExcelModel {

    public List<String> getHeaders(){
        List<String> headers = new ArrayList<>();
        headers.add("#");
        List<MetadataExcelModel> listMetadata = getListMetadata();
        for (MetadataExcelModel metadataExcelModel : listMetadata) {
            headers.add(metadataExcelModel.getHeader());
        }
        return headers;
    }

    public List<String> getHeaderForScoreByRollnumber(){
        List<String> headers = new ArrayList<>();
        headers.add("#");
        List<MetadataExcelModel> listMetadata = getListMetadataExcelModels();
        for (MetadataExcelModel metadataExcelModel : listMetadata) {
            headers.add(metadataExcelModel.getHeader());
        }
        return headers;
    }

    public abstract List<MetadataExcelModel> getListMetadata();

    public abstract List<MetadataExcelModel> getListMetadataExcelModels();
}
