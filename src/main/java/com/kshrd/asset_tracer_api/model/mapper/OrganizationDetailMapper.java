package com.kshrd.asset_tracer_api.model.mapper;

import com.kshrd.asset_tracer_api.model.dto.OrganizationDetailDTO;
import com.kshrd.asset_tracer_api.model.entity.OrganizationDetail;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrganizationDetailMapper {
    OrganizationDetailMapper INSTANCE = Mappers.getMapper(OrganizationDetailMapper.class);

    OrganizationDetailDTO toOrganizationDetailDto(OrganizationDetail organizationDetail);
    List<OrganizationDetailDTO> toOrganizationDetailDtos(List<OrganizationDetail> organizationDetails);
}
