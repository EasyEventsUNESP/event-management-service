package com.easyevents.event_management_service.repository;

import com.easyevents.event_management_service.domain.model.EventoModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EventoRepository extends MongoRepository<EventoModel, String> {

    Optional<EventoModel> findByEmailOrganizador(String email);
}
