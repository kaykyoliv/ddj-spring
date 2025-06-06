package com.kayky.producer;

import com.kayky.api.ProducerControllerApi;
import com.kayky.dto.ProducerGetResponse;
import com.kayky.dto.ProducerPostRequest;
import com.kayky.dto.ProducerPostResponse;
import com.kayky.dto.ProducerPutRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/producers")
@Slf4j
@RequiredArgsConstructor
@SecurityRequirement(name = "basicAuth")
public class ProducerController implements ProducerControllerApi {
    private final ProducerMapper mapper;

    private final ProducerService service;

    @GetMapping
    public ResponseEntity<List<ProducerGetResponse>> findAllProducer(@RequestParam(required = false) String name) {
        log.debug("Request received to list all producers, param name '{}'", name);
        var producers = service.findAll(name);

        var producerGetResponses = mapper.toProducerGetResponseList(producers);

        return ResponseEntity.ok(producerGetResponses);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProducerGetResponse> findProducerById(@PathVariable Long id) {
        log.debug("Request to find producer by id: {}", id);

        var producer = service.findByIdOrThrowNotFound(id);

        var producerGetResponse = mapper.toProducerGetResponse(producer);

        return ResponseEntity.ok(producerGetResponse);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProducerPostResponse> saveProducer(@RequestBody @Valid ProducerPostRequest producerPostRequest) {

        var producer = mapper.toProducer(producerPostRequest);

        var producerSaved = service.save(producer);

        var producerPostResponse = mapper.toProducerPostResponse(producerSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(producerPostResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProducerById(@PathVariable Long id) {
        log.debug("Request to delete producer by id: {}", id);

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> updateProducer(@RequestBody @Valid ProducerPutRequest request) {
        log.debug("Request to update producer {}", request);

        var producerToUpdate = mapper.toProducer(request);

        service.update(producerToUpdate);

        return ResponseEntity.noContent().build();
    }

}