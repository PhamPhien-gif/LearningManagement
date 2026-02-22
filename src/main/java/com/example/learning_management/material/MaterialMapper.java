package com.example.learning_management.material;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import com.example.learning_management.material.dto.MaterialDetail;
import com.example.learning_management.material.dto.MaterialSummary;
import com.example.learning_management.material.dto.UpdateMaterialRequest;

@Mapper(componentModel = "spring")
public interface MaterialMapper {
    MaterialDetail toDetail(Material material);
    MaterialSummary toSummary(Material material);

    //if request has field null, ignore update to material
    @BeanMapping(nullValuePropertyMappingStrategy =  NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateMaterialFromRequest(UpdateMaterialRequest request, @MappingTarget Material material);
}
