package com.kayky.service;

import com.kayky.domain.Producer;
import com.kayky.repository.ProducerHardCodedRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
 import static java.util.Collections.singletonList;
 
 @ExtendWith(MockitoExtension.class)
 @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
 class ProducerServiceTest {
     @InjectMocks
     private ProducerService service;
     @Mock
     private ProducerHardCodedRepository repository;
     private List<Producer> producerList;
 
     @BeforeEach
     void init() {
         var ufotable = Producer.builder().id(1L).name("Ufotable").createdAt(LocalDateTime.now()).build();
         var witStudio = Producer.builder().id(2L).name("Wit Studio").createdAt(LocalDateTime.now()).build();
         var studioGhibli = Producer.builder().id(3L).name("Studio Ghibli").createdAt(LocalDateTime.now()).build();
         producerList = new ArrayList<>(List.of(ufotable, witStudio, studioGhibli));
     }
 
     @Test
     @DisplayName("findAll returns a list with all producers when argument is null")
     @Order(1)
     void findAll_ReturnsAllProducers_WhenArgumentIsNull() {
         BDDMockito.when(repository.findAll()).thenReturn(producerList);
 
         var producers = service.findAll(null);
         org.assertj.core.api.Assertions.assertThat(producers).isNotNull().hasSameElementsAs(producerList);
     }
 
     @Test
     @DisplayName("findAll returns list with found object when name exists")
     @Order(2)
     void findByName_ReturnsFoundProducerInList_WhenNameIsFound() {
         var producer = producerList.getFirst();
         var expectedProducersFound = singletonList(producer);
 
         BDDMockito.when(repository.findByName(producer.getName())).thenReturn(expectedProducersFound);
 
         var producersFound = service.findAll(producer.getName());
         org.assertj.core.api.Assertions.assertThat(producersFound).containsAll(expectedProducersFound);
     }
 
     @Test
     @DisplayName("findAll returns empty list when name is not found")
     @Order(3)
     void findByName_ReturnsEmptyList_WhenNameIsNotFound() {
         var name = "not-found";
         BDDMockito.when(repository.findByName(name)).thenReturn(emptyList());
 
         var producers = service.findAll(name);
         Assertions.assertThat(producers).isNotNull().isEmpty();
     }
 }