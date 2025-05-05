package com.kayky.producer;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ProducerPostResponse {
    private Long id;
    private String name;
}