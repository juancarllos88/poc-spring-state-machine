package com.statemachine.incubator.example.controller;

import com.statemachine.incubator.example.service.TrafficLightService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/traffic-lights/api")
public class TrafficLightController {

    private final static Logger LOGGER = LoggerFactory.getLogger(TrafficLightController.class);

    @Autowired
    private TrafficLightService trafficLightService;

    @PostMapping
    public ResponseEntity<OutputTrafficLightDto> createTrafficLight(@RequestBody InputTrafficLightDto dto) {
        var trafficLight = trafficLightService.create(dto.toEntity());

        return ResponseEntity.ok(OutputTrafficLightDto.from(trafficLight));
    }

    @PatchMapping("/{trafficLightId}/statuses")
    public ResponseEntity<OutputTrafficLightDto> executeCommand(@PathVariable Long trafficLightId, @RequestBody InputCommandDto dto) {
        var trafficLight = trafficLightService.execute(trafficLightId, dto.command());
        return ResponseEntity.ok(OutputTrafficLightDto.from(trafficLight));
    }

    @GetMapping("/{trafficLightId}")
    public ResponseEntity<OutputTrafficLightDto> findBy(@PathVariable Long trafficLightId) {
        return trafficLightService.findBy(trafficLightId)
                .map(trafficLight -> ResponseEntity.ok(OutputTrafficLightDto.from(trafficLight)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
