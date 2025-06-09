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
@RequiredArgsConstructor
public class EventController {

    @Autowired
    private final EventService eventService;

    @GetMapping
    public String endpointTest() {

        return "ERALDO VS MARIO!";
    }

    @GetMapping("/listar")
    public ResponseEntity<List<EventoModel>> listar() {

        return eventService.listar();
    }


    @GetMapping("/buscar/{email}")
    public ResponseEntity<EventoModel> buscarPorEmail(@PathVariable String email) {
        return eventService.buscarPorEmail(email);
    }

    @PostMapping("/criar")
    public ResponseEntity<EventoResponse> criar(@RequestBody CriarEventoRequest criarEventoRequest) {
        return eventService.criarEvento(criarEventoRequest);
    }

    @PutMapping("/atualizar")
    public ResponseEntity<EventoResponse> atualizar(@RequestBody AtualizarEventoRequest atualizarUsuarioRequest) {
        return eventService.atualizarEvento(atualizarUsuarioRequest);
    }

    @DeleteMapping("/deletar/{email}")
    public ResponseEntity<EventoResponse> deletar(@PathVariable String email) {
        return eventService.deletarEvento(email);
    }
}
