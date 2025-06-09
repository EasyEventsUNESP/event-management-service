package com.easyevents.event_management_service.domain.model;

import com.easyevents.event_management_service.domain.dto.Orcamento;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "evento")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventoModel {

    @Id
    private String id;
    private String nome;
    private String descricao;
    private String local;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Orcamento orcamento;
    private String emailOrganizador;
    private List<String> funcionariosId;
    private List<String> convidadosId;
}
