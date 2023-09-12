package com.kshrd.asset_tracer_api.model.mapper;

import com.kshrd.asset_tracer_api.model.dto.UserAppApproveDTO;
import com.kshrd.asset_tracer_api.model.entity.UserApp;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserAppApproveMapper {

    UserAppApproveMapper INSTANCE = Mappers.getMapper(UserAppApproveMapper.class);
    UserAppApproveDTO toUserAppApproveDto(UserApp userApp);
}
