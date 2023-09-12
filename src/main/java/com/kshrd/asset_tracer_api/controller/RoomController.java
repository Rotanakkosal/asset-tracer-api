package com.kshrd.asset_tracer_api.controller;

import com.kshrd.asset_tracer_api.model.request.RoomRequest;
import com.kshrd.asset_tracer_api.model.response.BodyResponse;
import com.kshrd.asset_tracer_api.repository.RoomRepository;
import com.kshrd.asset_tracer_api.service.RoomService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rooms")
@SecurityRequirement(name = "bearerAuth")
public class RoomController {
    private final RoomService roomService;
    private final RoomRepository roomRepository;
    @GetMapping("/by-organization/{orgId}")
    public ResponseEntity<?> getAllRooms(@PathVariable("orgId") UUID orgId,
                                         @RequestParam (required = false)Integer page,
                                         @RequestParam (required = false) Integer size,
                                         @RequestParam (required = false) String search,
                                         @RequestParam (required = false) String sort){

        Integer countData = roomRepository.getCountAllRooms(orgId, search);
        return BodyResponse.getBodyResponse(roomService.getAllRooms(orgId, page, size, search, sort), countData);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomById(@PathVariable("id")UUID roomId){
        return BodyResponse.getBodyResponse(roomService.getRoomById(roomId));
    }

    @PostMapping
    public ResponseEntity<?> addRoom(@RequestBody RoomRequest roomRequest){
        UUID getId = roomService.addRoom(roomRequest);
        return BodyResponse.getBodyResponse(roomService.getRoomById(getId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoom(@RequestBody RoomRequest roomRequest,@PathVariable("id") UUID roomId){
        UUID id = roomService.updateRoom(roomRequest, roomId);
        return BodyResponse.getBodyResponse(roomService.getRoomById(id));
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable UUID roomId){
        return BodyResponse.getBodyResponse(roomService.deleteRoom(roomId));
    }

    enum SortProperty {NAME, CREATED_DATE, FLOOR}
    @GetMapping("/filter/{orgId}")
    public ResponseEntity<?> getAllRoom(@PathVariable UUID orgId,
                                        @RequestParam (required = false, defaultValue = "1") Integer page,
                                        @RequestParam (required = false, defaultValue = "5") Integer size,
                                        @RequestParam (required = false) String search,
                                        @RequestParam (required = false) SortProperty prop){
        return BodyResponse.getBodyResponse(roomService.getAllRoom(orgId, page, size, search, prop.name()), roomRepository.getCountData(orgId));
    }

    @GetMapping("/get-all-name/{orgId}")
    public ResponseEntity<?> getAllRoomByOrganizationId(@PathVariable("orgId") UUID orgId) {
        return BodyResponse.getBodyResponse(roomService.getAllRoomByOrganizationId(orgId));
    }
}