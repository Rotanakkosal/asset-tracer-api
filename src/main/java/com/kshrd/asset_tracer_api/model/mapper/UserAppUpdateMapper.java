package com.kshrd.asset_tracer_api.model.mapper;

import com.kshrd.asset_tracer_api.model.dto.UserAppUpdateDTO;
import com.kshrd.asset_tracer_api.model.entity.UserApp;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserAppUpdateMapper {
    UserAppUpdateMapper INSTANCE = Mappers.getMapper(UserAppUpdateMapper.class);
    UserAppUpdateDTO toUserAppUpdateDto(UserApp userApp);
}
