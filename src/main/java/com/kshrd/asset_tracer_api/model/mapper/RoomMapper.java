package com.kshrd.asset_tracer_api.model.mapper;

import com.kshrd.asset_tracer_api.model.dto.RoomAllDTO;
import com.kshrd.asset_tracer_api.model.dto.RoomDTO;
import com.kshrd.asset_tracer_api.model.entity.Room;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    RoomMapper INSTANCE = Mappers.getMapper(RoomMapper.class);
    RoomDTO toRoomDto(Room room);
    List<RoomDTO> toRoomDtos(List<Room> rooms);
    List<RoomAllDTO> toRoomAllDtos(List<Room> rooms);
}
