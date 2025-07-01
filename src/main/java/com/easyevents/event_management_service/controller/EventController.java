package com.easyevents.event_management_service.controller;

import com.easyevents.event_management_service.domain.dto.Request.AtualizarEventoRequest;
import com.easyevents.event_management_service.domain.dto.Request.CriarEventoRequest;
import com.easyevents.event_management_service.domain.dto.Response.EventoResponse;
import com.easyevents.event_management_service.domain.model.EventoModel;
import com.easyevents.event_management_service.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/listar")
    public ResponseEntity<List<EventoModel>> listar() {

        return eventService.listar();
    }

    @GetMapping("/buscarPorEmail/{email}")
    public ResponseEntity<List<EventoModel>> buscarEventosPorEmail(@PathVariable String email) {
        List<EventoModel> eventos = eventService.buscarEventosPorEmail(email);
        if (eventos.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 No Content if no events are found
        }
        return ResponseEntity.ok(eventos); // Return 200 OK with the list of events
    }

    @PostMapping("/criar")
    public ResponseEntity<EventoResponse> criar(@RequestBody CriarEventoRequest criarEventoRequest) {
        return eventService.criarEvento(criarEventoRequest);
    }

    @PutMapping("/atualizar")
    public ResponseEntity<EventoResponse> atualizar(@RequestBody AtualizarEventoRequest atualizarEventoRequest) {
        return eventService.atualizarEvento(atualizarEventoRequest);
    }

    @DeleteMapping("/deletar/{eventoId}/{email}")
    public ResponseEntity<EventoResponse> deletarEvento(
            @PathVariable String eventoId,
            @PathVariable String email) {

        return eventService.deletarEvento(eventoId, email);
    }
}
