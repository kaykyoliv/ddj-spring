package com.kayky.controller;

import com.kayky.config.BrasilApiConfigurationProperties;
import com.kayky.response.CepGetResponse;
import com.kayky.service.BrasilApiService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/brasil-api/cep")
@Log4j2
@RequiredArgsConstructor
@SecurityRequirement(name = "basicAuth")
public class BrasilApiController {

    private final BrasilApiService service;
    private final BrasilApiConfigurationProperties brasilApiConfigurationProperties;

    @GetMapping("/{cep}")
    public ResponseEntity<CepGetResponse> findCep(@PathVariable String cep) {
        log.info("request received to find cep");
        var cepGetResponse = service.findCep(cep);
        return ResponseEntity.ok(cepGetResponse);
    }
}