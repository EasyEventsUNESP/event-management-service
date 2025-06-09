package com.easyevents.event_management_service.domain.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtualizarEventoRequest {

    private String email;
    private String novoNome;
    private String novaSenha;
    private String descricao;
    private String dataInicio;
    private String dataFim;
    private String localizacao;
    private String funcionariosId;
    private String convidadosId;
}
