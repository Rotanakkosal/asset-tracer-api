package com.kshrd.asset_tracer_api.model.mapper;

import com.kshrd.asset_tracer_api.model.dto.NormalCategoryDTO;
import com.kshrd.asset_tracer_api.model.dto.OrganizationDTO;
import com.kshrd.asset_tracer_api.model.dto.OrganizationDTO2;
import com.kshrd.asset_tracer_api.model.dto.OrganizationInvitedDTO;
import com.kshrd.asset_tracer_api.model.entity.NormalCategory;
import com.kshrd.asset_tracer_api.model.entity.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {
    OrganizationMapper INSTANCE = Mappers.getMapper(OrganizationMapper.class);
    OrganizationDTO toOrganizationDto (Organization organization);
    OrganizationDTO2 toOrganizationDto2 (Organization organization);
    List<OrganizationDTO> toOrganizationDto (List<Organization> organizations);
    List<OrganizationInvitedDTO> tOrganizationInvitedDtos(List<Organization> organizations);
    OrganizationInvitedDTO toOrganizationInvitedDto(Organization organization);
}
