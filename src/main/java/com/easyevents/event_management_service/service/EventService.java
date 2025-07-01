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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
     * Método para buscar todos os eventos pelo email do organizador.
     *
     * @param email Email do organizador para buscar os eventos.
     * @return List de eventos.
     */
    public List<EventoModel> buscarEventosPorEmail(String email) {
        List<EventoModel> eventos = eventoRepository.findByEmailOrganizador(email);
        if (eventos.isEmpty()) {
            return Collections.emptyList();
        }
        return eventos;
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


        if (eventoRepository.findByNomeAndEmailOrganizador(criarEventoRequest.getNome(), criarEventoRequest.getEmailOrganizador()).isPresent()) {
            throw new IllegalArgumentException("Você já criou um evento com esse mesmo nome para este organizador! Escolha outro nome para o evento.");
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
                .build();

        eventoRepository.save(novoEvento); // Use .save() for both insert and update operations in JpaRepository

        return ResponseEntity.status(HttpStatus.CREATED).body(
                EventoResponse.builder()
                        .nomeEvento(novoEvento.getNome())
                        .emailOrganizador(novoEvento.getEmailOrganizador())
                        .responseMessage("Evento criado com sucesso!")
                        .build()
        );
    }

    public ResponseEntity<EventoResponse> atualizarEvento(AtualizarEventoRequest atualizarEventoRequest) {
        // 1. Encontrar o evento específico a ser atualizado.
        // Usamos 'novoNome' da requisição como o nome do evento a ser encontrado,
        // e 'email' como o e-mail do organizador.
        Optional<EventoModel> eventoOptional = eventoRepository.findByNomeAndEmailOrganizador(
                atualizarEventoRequest.getNovoNome(), // Nome do evento a ser atualizado
                atualizarEventoRequest.getEmail()     // E-mail do organizador do evento
        );

        // Se o evento não for encontrado, lança uma exceção
        EventoModel eventoModel = eventoOptional.orElseThrow(() ->
                new IllegalArgumentException("Evento não encontrado com o nome '" + atualizarEventoRequest.getNovoNome() +
                        "' e e-mail do organizador: " + atualizarEventoRequest.getEmail()));

        boolean modificado = false;

        // 2. Atualizar a descrição, se fornecida e diferente
        // Apenas atualiza se o campo não for nulo e não estiver vazio (após trim)
        if (atualizarEventoRequest.getDescricao() != null && !atualizarEventoRequest.getDescricao().trim().isEmpty()) {
            if (!atualizarEventoRequest.getDescricao().equals(eventoModel.getDescricao())) {
                eventoModel.setDescricao(atualizarEventoRequest.getDescricao().trim());
                modificado = true;
            }
        }

        // 3. Atualizar datas de início e fim, se fornecidas e válidas
        // Ambos os campos (dataInicio e dataFim) devem estar presentes para que a atualização ocorra
        if (atualizarEventoRequest.getDataInicio() != null && atualizarEventoRequest.getDataFim() != null) {
            LocalDateTime dataInicioRequest = LocalDateTime.parse(atualizarEventoRequest.getDataInicio());
            LocalDateTime dataFimRequest = LocalDateTime.parse(atualizarEventoRequest.getDataFim());

            // Valida se a data de início é anterior à data de fim
            if (dataInicioRequest.isAfter(dataFimRequest)) {
                throw new IllegalArgumentException("A data de início deve ser anterior à data de fim.");
            }

            // Verifica se as datas realmente mudaram antes de setar
            if (!dataInicioRequest.equals(eventoModel.getDataInicio()) || !dataFimRequest.equals(eventoModel.getDataFim())) {
                eventoModel.setDataInicio(dataInicioRequest);
                eventoModel.setDataFim(dataFimRequest);
                modificado = true;
            }
        }

        // 4. Atualizar a localização, se fornecida e diferente
        // Apenas atualiza se o campo não for nulo e não estiver vazio (após trim)
        if (atualizarEventoRequest.getLocalizacao() != null && !atualizarEventoRequest.getLocalizacao().trim().isEmpty()) {
            if (!atualizarEventoRequest.getLocalizacao().equals(eventoModel.getLocal())) {
                eventoModel.setLocal(atualizarEventoRequest.getLocalizacao().trim());
                modificado = true;
            }
        }



        // 5. Se alguma modificação foi feita, atualiza a data de atualização e salva no repositório
        if (modificado) {
            eventoModel.setUpdatedAt(LocalDateTime.now());
            eventoRepository.save(eventoModel); // Usa .save() para persistir as alterações
        }

        // 6. Retorna a resposta de sucesso
        return ResponseEntity.status(HttpStatus.OK)
                .body(EventoResponse.builder()
                        .emailOrganizador(eventoModel.getEmailOrganizador())
                        .nomeEvento(eventoModel.getNome())
                        .responseMessage(modificado ? "Evento atualizado com sucesso!" : "Nenhuma alteração fornecida ou dados são os mesmos.")
                        .build()
                );
    }

    /**
     * Método para deletar um evento específico com base no ID do evento (MongoDB ID) e no email do organizador.
     *
     * @param eventoId O ID único do evento (String, MongoDB ID) a ser deletado.
     * @param emailOrganizador O email do organizador do evento.
     * @throws IllegalArgumentException Se o evento não for encontrado ou se o organizador não tiver permissão.
     * @return ResponseEntity contendo a resposta do serviço.
     */
    public ResponseEntity<EventoResponse> deletarEvento(String eventoId, String emailOrganizador) {

        EventoModel eventoParaDeletar = eventoRepository.findByIdAndEmailOrganizador(eventoId, emailOrganizador)
                .orElseThrow(() -> new IllegalArgumentException("Evento não encontrado ou você não tem permissão para deletá-lo."));

        // Deleta o evento encontrado
        eventoRepository.delete(eventoParaDeletar);

        // Retorna uma resposta de sucesso
        return ResponseEntity.status(HttpStatus.OK).body(
                EventoResponse.builder()
                        .nomeEvento(eventoParaDeletar.getNome())
                        .emailOrganizador(eventoParaDeletar.getEmailOrganizador())
                        .responseMessage("Evento deletado com sucesso.")
                        .build()
        );
    }
}
