package com.execute.protocol.admin.controllers;

import com.execute.protocol.core.entities.Event;
import com.execute.protocol.core.repositories.AdminRepository;
import com.execute.protocol.core.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminRepository userRepository;
    private final EventService eventService;

    @PostMapping("/events")
    public ResponseEntity index(
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "size", defaultValue = "1") int pageSize) {
        List<Event> sortEvents = eventService.getEventsOrderByUpdateTimeDesc(pageNumber, pageSize).toList();
        return ResponseEntity.ok(sortEvents);
    }

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody Event event) {


        eventService.saveEvent(event);

        return ResponseEntity.ok(true);
    }


}
