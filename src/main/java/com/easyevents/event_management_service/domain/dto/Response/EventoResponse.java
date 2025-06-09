package com.easyevents.event_management_service.domain.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoResponse {

    private String responseMessage;
    private String nomeEvento;
    private String emailOrganizador;
}
