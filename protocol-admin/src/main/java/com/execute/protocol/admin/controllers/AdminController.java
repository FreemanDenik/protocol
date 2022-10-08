package com.execute.protocol.admin.controllers;

import com.execute.protocol.core.dto.EventDto;
import com.execute.protocol.core.entities.Event;
import com.execute.protocol.core.repositories.AdminRepository;
import com.execute.protocol.core.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminRepository userRepository;
    private final EventService eventService;
    @PostMapping("/events")
    public ResponseEntity index(){

        return ResponseEntity.ok(eventService.getAllEventDto());
    }

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody Event event){

        eventService.saveEvent(event);
        return ResponseEntity.ok(true);
    }


}
