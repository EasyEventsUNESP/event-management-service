package com.easyevents.event_management_service.domain.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtualizarEventoRequest {

    private String email;
    private String novoNome;
    private String descricao;
    private String dataInicio;
    private String dataFim;
    private String localizacao;
}
