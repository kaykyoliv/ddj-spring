package com.kayky.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kayky.config.BrasilApiConfigurationProperties;
import com.kayky.exception.NotFoundException;
import com.kayky.response.CepErrorResponse;
import com.kayky.response.CepGetResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@AllArgsConstructor
public class BrasilApiService {

    private final RestClient.Builder brasilApiClient;
    private final BrasilApiConfigurationProperties brasilApiConfigurationProperties;
    private final ObjectMapper mapper;

    public CepGetResponse findCep(String cep) {
        return brasilApiClient.build()
                .get()
                .uri(brasilApiConfigurationProperties.cepUri(), cep)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    var body = new String(response.getBody().readAllBytes());
                    var cepErrorResponse = mapper.readValue(body, CepErrorResponse.class);
                    throw new NotFoundException(cepErrorResponse.toString());
                })
                .body(CepGetResponse.class);

    }
}
