package com.easyevents.event_management_service.repository;

import com.easyevents.event_management_service.domain.model.EventoModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface EventoRepository extends MongoRepository<EventoModel, String> {

    List<EventoModel> findByEmailOrganizador(String email);
    Optional<EventoModel> findByNomeAndEmailOrganizador(String nome, String emailOrganizador);
    Optional<EventoModel> findByIdAndEmailOrganizador(String id, String emailOrganizador);

}
