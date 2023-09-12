package com.kshrd.asset_tracer_api.service;

import com.kshrd.asset_tracer_api.model.dto.RoomAllDTO;
import com.kshrd.asset_tracer_api.model.dto.RoomDTO;
import com.kshrd.asset_tracer_api.model.request.RoomRequest;

import java.util.List;
import java.util.UUID;

public interface RoomService {
    List<RoomDTO> getAllRooms(UUID orgId, Integer page, Integer size, String search, String sort);
    RoomDTO getRoomById(UUID roomId);
    UUID addRoom(RoomRequest roomRequest);
    UUID updateRoom(RoomRequest roomRequest, UUID roomId);
    UUID deleteRoom(UUID roomId);
    List<RoomDTO> getAllRoom(UUID orgId, Integer page, Integer size, String search, String prop);
    List<RoomAllDTO> getAllRoomByOrganizationId(UUID orgId);

    Integer getCountAllRooms(UUID orgId);
}



