package com.kshrd.asset_tracer_api.service.serviceImp;

import com.kshrd.asset_tracer_api.exception.ForbiddenExceptionHandler;
import com.kshrd.asset_tracer_api.exception.NotFoundExceptionHandler;
import com.kshrd.asset_tracer_api.exception.UnauthorizedExceptionHandler;
import com.kshrd.asset_tracer_api.model.dto.RoomAllDTO;
import com.kshrd.asset_tracer_api.model.dto.RoomDTO;
import com.kshrd.asset_tracer_api.model.entity.Room;
import com.kshrd.asset_tracer_api.model.entity.UserApp;
import com.kshrd.asset_tracer_api.model.mapper.RoomMapper;
import com.kshrd.asset_tracer_api.model.request.RoomRequest;
import com.kshrd.asset_tracer_api.repository.OrganizationDetailRepository;
import com.kshrd.asset_tracer_api.repository.RoomRepository;
import com.kshrd.asset_tracer_api.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private Room room = new Room();
    List<Room> rooms;
    private RoomMapper roomMapper;
    private final RoomRepository roomRepository;
    private final OrganizationDetailRepository organizationDetailRepository;

    private UUID getCurrentUser() {

        Object getContext = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (getContext.equals("anonymousUser")) {
            throw new UnauthorizedExceptionHandler("Unauthorized User");
        }

        UserApp user = (UserApp) getContext;
        return user.getId();
    }


    @Override
    public List<RoomDTO> getAllRooms(UUID orgId, Integer page, Integer size, String search, String sort) {

        if(getCurrentUser()==null){
            throw new ForbiddenExceptionHandler("Access Denied, Please log in");
        }

        rooms = roomRepository.getAllRooms(orgId, page, size, search, sort);
        System.out.println(rooms);
        return roomMapper.INSTANCE.toRoomDtos(rooms);
    }

    @Override
    public RoomDTO getRoomById(UUID roomId) {
        if(getCurrentUser()==null){
            throw new ForbiddenExceptionHandler("Access Denied, Please log in");
        }

        room = roomRepository.getRoomById(roomId);
        if (room == null) {
            throw new NotFoundExceptionHandler("Room not found");
        }

        return roomMapper.INSTANCE.toRoomDto(room);
    }

    @Override
    public UUID addRoom(RoomRequest roomRequest) {

        if(getCurrentUser()==null){
            throw new ForbiddenExceptionHandler("Access Denied, Please log in");
        }

        var organizationId = roomRequest.getOrganizationId();

        if(organizationId == null) {
            throw new NotFoundExceptionHandler("Organization not found");
        }

        String roleName = organizationDetailRepository.getExistRoleInOrganization(getCurrentUser(), organizationId);

        if (roleName == null || !roleName.equals("ADMIN")) {
            throw new ForbiddenExceptionHandler("Access Denied Page");
        }

        return roomRepository.addRoom(roomRequest, getCurrentUser());

    }

    @Override
    public UUID updateRoom(RoomRequest roomRequest, UUID roomId) {

        if(getCurrentUser()==null){
            throw new ForbiddenExceptionHandler("Access Denied, Please log in");
        }

        var organizationId = roomRequest.getOrganizationId();

        if(organizationId == null) {
            throw new NotFoundExceptionHandler("Organization not found");
        }

        String roleName = organizationDetailRepository.getExistRoleInOrganization(getCurrentUser(), organizationId);

        if (roleName == null || !roleName.equals("ADMIN")) {
            throw new NotFoundExceptionHandler("Access Denied Page");
        }
        else if (roomRepository.getRoomById(roomId) == null) {
            throw new NotFoundExceptionHandler("Room not found");
        }

        return roomRepository.updateRoom(roomRequest, roomId, getCurrentUser());
    }

    @Override
    public UUID deleteRoom(UUID roomId) {

        if(getCurrentUser()==null){
            throw new ForbiddenExceptionHandler("Access Denied, Please log in");
        }
        else if(roomRepository.getRoomById(roomId) == null){
            throw new NotFoundExceptionHandler("Room not found");
        }

        return roomRepository.deleteRoom(roomId, getCurrentUser());
    }

    @Override
    public List<RoomDTO> getAllRoom(UUID orgId, Integer page, Integer size, String search, String prop) {

        if(getCurrentUser()==null){
            throw new ForbiddenExceptionHandler("Access Denied, Please log in");
        }

        if(prop.equals("NAME")){
            return roomMapper.toRoomDtos(roomRepository.getAllRoomByName(orgId, page, size, search, prop));
        }
        else if(prop.equals("CREATED_DATE")){
            return roomMapper.toRoomDtos(roomRepository.getAllRoomByCreateDate(orgId, page, size, search, prop));
        }
        else {
            return roomMapper.toRoomDtos(roomRepository.getAllRoomByFloor(orgId, page, size, search, prop));
        }
    }

    @Override
    public List<RoomAllDTO> getAllRoomByOrganizationId(UUID orgId) {
        rooms = roomRepository.getAllRoomByOrganizationId(orgId);

        if(rooms.isEmpty()) {
            throw new NotFoundExceptionHandler("Data is empty");
        }

        return roomMapper.INSTANCE.toRoomAllDtos(rooms);
    }

    @Override
    public Integer getCountAllRooms(UUID orgId) {

        if(getCurrentUser()==null){
            throw new ForbiddenExceptionHandler("Access Denied, Please log in");
        }

        return roomRepository.getCountRooms(orgId);
    }
}
