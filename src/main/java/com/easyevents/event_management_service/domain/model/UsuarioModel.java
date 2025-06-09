package com.easyevents.event_management_service.domain.model;

import com.easyevents.event_management_service.domain.enumerator.Cargo;
import com.easyevents.event_management_service.domain.enumerator.Provedor;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "usuario")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioModel extends PessoaModel {

    private String senha;
    private Provedor provedor;
    private Boolean admin;
    private LocalDateTime criacao;
    private LocalDateTime update;
    private LocalDateTime updatedAt;
    private Map<EventoModel, Cargo> eventoCargo;

}
