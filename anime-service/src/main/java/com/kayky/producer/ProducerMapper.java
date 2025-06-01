package com.kayky.producer;

import com.kayky.domain.Producer;
import com.kayky.dto.ProducerGetResponse;
import com.kayky.dto.ProducerPostRequest;
import com.kayky.dto.ProducerPostResponse;
import com.kayky.dto.ProducerPutRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProducerMapper {
    Producer toProducer(ProducerPostRequest postRequest);

    Producer toProducer(ProducerPutRequest request);

    ProducerGetResponse toProducerGetResponse(Producer producer);

    ProducerPostResponse toProducerPostResponse(Producer producer);

    List<ProducerGetResponse> toProducerGetResponseList(List<Producer> producers);

}