package com.easyevents.event_management_service.domain.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CriarEventoRequest {

    private String nome;
    private String descricao;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private String localizacao;
    private String emailOrganizador;
    private String orcamentoId;
    private List<String> funcionariosId;
    private List<String> convidadosId;
}
