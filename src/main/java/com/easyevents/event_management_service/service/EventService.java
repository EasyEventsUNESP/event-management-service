package com.easyevents.event_management_service.service;

import com.easyevents.event_management_service.domain.dto.Request.AtualizarEventoRequest;
import com.easyevents.event_management_service.domain.dto.Request.CriarEventoRequest;
import com.easyevents.event_management_service.domain.dto.Response.EventoResponse;
import com.easyevents.event_management_service.domain.model.EventoModel;
import com.easyevents.event_management_service.repository.EventoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventService.class);
    @Autowired
    private EventoRepository eventoRepository;

    /**
     * Método para listar todos os eventos.
     *
     * @return ResponseEntity contendo uma lista de todos os eventos.
     */
    public ResponseEntity<List<EventoModel>> listar() {
        return ResponseEntity.status(HttpStatus.FOUND).body(eventoRepository.findAll());
    }

    /**
     * Método para buscar um evento pelo email do organizador.
     *
     * @param email Email do organizador para buscar o evento.
     * @throws IllegalArgumentException Se o evento não for encontrado.
     * @return ResponseEntity contendo os detalhes do evento.
     */
    public ResponseEntity<EventoModel> buscarPorEmail(String email) {
        return ResponseEntity.status(HttpStatus.FOUND).body(eventoRepository.findByEmailOrganizador(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado")));
    }

    /**
     * Método para criar um evento.
     *
     * @param criarEventoRequest Objeto de requisição contendo os detalhes do evento.
     * @throws IllegalArgumentException Se a data de início for posterior à data de fim ou se já existir um evento com o mesmo nome.
     * @return ResponseEntity contendo a resposta do serviço.
     */
    public ResponseEntity<EventoResponse> criarEvento(CriarEventoRequest criarEventoRequest) {
        if (criarEventoRequest.getDataInicio().isAfter(criarEventoRequest.getDataFim())) {
            throw new IllegalArgumentException("A data de início deve ser anterior à data de fim.");
        }

        if (eventoRepository.findByEmailOrganizador(criarEventoRequest.getNome()).isPresent()) {
            throw new IllegalArgumentException("Você já criou um evento com esse mesmo nome! Escolha outro.");
        }

        EventoModel novoEvento = EventoModel.builder()
                .nome(criarEventoRequest.getNome())
                .descricao(criarEventoRequest.getDescricao())
                .local(criarEventoRequest.getLocalizacao())
                .dataInicio(criarEventoRequest.getDataInicio())
                .dataFim(criarEventoRequest.getDataFim())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .emailOrganizador(criarEventoRequest.getEmailOrganizador())
                .funcionariosId(criarEventoRequest.getFuncionariosId())
                .convidadosId(criarEventoRequest.getConvidadosId())
                .build();

        eventoRepository.insert(novoEvento);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                EventoResponse.builder()
                        .nomeEvento(novoEvento.getNome())
                        .emailOrganizador(novoEvento.getEmailOrganizador())
                        .responseMessage("Evento criado com sucesso!")
                        .build()
        );
    }

    /**
     * Método para atualizar um evento existente.
     *
     * @param atualizarEventoRequest Objeto de requisição contendo os detalhes atualizados do evento.
     * @throws IllegalArgumentException Se o evento não for encontrado ou se a data de início for posterior à data de fim.
     * @return ResponseEntity contendo a resposta do serviço.
     */
    public ResponseEntity<EventoResponse> atualizarEvento(AtualizarEventoRequest atualizarEventoRequest) {
        EventoModel eventoModel = eventoRepository.findByEmailOrganizador(atualizarEventoRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Evento não encontrado com o e-mail: " + atualizarEventoRequest.getEmail()));

        boolean modificado = false;

        if (atualizarEventoRequest.getDescricao() != null && !atualizarEventoRequest.getDescricao().trim().isEmpty()) {
            if (!atualizarEventoRequest.getDescricao().equals(eventoModel.getDescricao())) {
                eventoModel.setDescricao(atualizarEventoRequest.getDescricao().trim());
                modificado = true;
            }
        }

        if (atualizarEventoRequest.getDataInicio() != null && atualizarEventoRequest.getDataFim() != null) {
            LocalDateTime dataInicio = LocalDateTime.parse(atualizarEventoRequest.getDataInicio());
            LocalDateTime dataFim = LocalDateTime.parse(atualizarEventoRequest.getDataFim());

            if (dataInicio.isAfter(dataFim)) {
                throw new IllegalArgumentException("A data de início deve ser anterior à data de fim.");
            }

            eventoModel.setDataInicio(dataInicio);
            eventoModel.setDataFim(dataFim);
            modificado = true;
        }

        if (atualizarEventoRequest.getLocalizacao() != null && !atualizarEventoRequest.getLocalizacao().trim().isEmpty()) {
            if (!atualizarEventoRequest.getLocalizacao().equals(eventoModel.getLocal())) {
                eventoModel.setLocal(atualizarEventoRequest.getLocalizacao().trim());
                modificado = true;
            }
        }

        if (modificado) {
            eventoModel.setUpdatedAt(LocalDateTime.now());
            eventoRepository.save(eventoModel);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(EventoResponse.builder()
                        .emailOrganizador(eventoModel.getEmailOrganizador())
                        .nomeEvento(eventoModel.getNome())
                        .responseMessage(modificado ? "Evento atualizado com sucesso!" : "Nenhuma alteração fornecida ou dados são os mesmos.")
                        .build()
                );
    }

    /**
     * Método para deletar um evento com base no email do organizador.
     *
     * @param email Email do organizador do evento a ser deletado.
     * @throws IllegalArgumentException Se o evento não for encontrado.
     * @return ResponseEntity contendo a resposta do serviço.
     */

    public ResponseEntity<EventoResponse> deletarEvento(String email) {

        EventoModel eventoModel = eventoRepository.findByEmailOrganizador(email)
                .orElseThrow(() -> new IllegalArgumentException("Evento não encontrado"));

        eventoRepository.delete(eventoModel);

        return ResponseEntity.status(HttpStatus.OK).body(
                EventoResponse.builder()
                        .nomeEvento(eventoModel.getNome())
                        .emailOrganizador(eventoModel.getEmailOrganizador())
                        .responseMessage("Evento deletado com sucesso.")
                        .build()
        );
    }
}
