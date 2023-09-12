package com.kshrd.asset_tracer_api.model.mapper;

import com.kshrd.asset_tracer_api.model.dto.NotificationByOrgDTO;
import com.kshrd.asset_tracer_api.model.dto.SearchUserAppDTO;
import com.kshrd.asset_tracer_api.model.dto.UserAppDTO;
import com.kshrd.asset_tracer_api.model.entity.UserApp;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserAppMapper {
    UserAppMapper INSTANCE = Mappers.getMapper(UserAppMapper.class);
    UserAppDTO toUserAppDto(UserApp userApp);
    List<UserAppDTO> toUserAppDtos (List<UserApp> list);
    List<SearchUserAppDTO> toSearchUserAppDtos(List<UserApp> list);
    List<NotificationByOrgDTO> tNotificationByOrgDtos(List<UserApp> list);
}
