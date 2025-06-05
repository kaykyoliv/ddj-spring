package com.kayky.commons;

import com.kayky.response.CepErrorResponse;
import com.kayky.response.CepGetResponse;
import com.kayky.response.CepInnerErrorResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CepUtils {
    public CepGetResponse newCepGetResponse() {
        return CepGetResponse.builder()
                .cep("00000")
                .city("São Paulo")
                .neighborhood("Vila Mariana")
                .street("Rua 123")
                .service("viacep")
                .build();
    }

    public CepErrorResponse newCepErrorResponse() {
        var cepInnerErrorResponse = CepInnerErrorResponse
                .builder()
                .name("ServiceError")
                .message("CEP INVÁLIDO")
                .service("correios")
                .build();

        return CepErrorResponse.builder()
                .name("CepPromiseError")
                .message("Todos os serviços de CEP retornaram erro.")
                .type("service_error")
                .errors(List.of(cepInnerErrorResponse))
                .build();
    }
}