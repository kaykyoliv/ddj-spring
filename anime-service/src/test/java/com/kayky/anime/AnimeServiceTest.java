package com.kayky.anime;

import com.kayky.anime.AnimeService;
import com.kayky.commons.AnimeUtils;
import com.kayky.domain.Anime;
import com.kayky.anime.AnimeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
 import static java.util.Collections.singletonList;
 
 @ExtendWith(MockitoExtension.class)
 @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
 class AnimeServiceTest {
     @InjectMocks
     private AnimeService service;
     @Mock
     private AnimeRepository repository;
     private List<Anime> animesList;

     @InjectMocks
     private AnimeUtils animeUtils;

     @BeforeEach
     void init() {
       animesList = animeUtils.newAnimeList();
     }
 
     @Test
     @DisplayName("findAll returns a list with all animes when argument is null")
     @Order(1)
     void findAll_ReturnsAllAnime_WhenArgumentIsNull() {
         BDDMockito.when(repository.findAll()).thenReturn(animesList);
 
         var animes = service.findAll(null);
         org.assertj.core.api.Assertions.assertThat(animes).isNotNull().hasSameElementsAs(animesList);
     }

     @Test
     @DisplayName("findAllPaginated returns a paginated list of animes")
     @Order(1)
     void findAllPaginated_ReturnsPaginatedAnime_WhenSuccessful() {

         var pageRequest = PageRequest.of(0, animesList.size());
         var pagedAnime = new PageImpl<>(animesList, pageRequest, 3);
         BDDMockito.when(repository.findAll(BDDMockito.any(Pageable.class))).thenReturn(pagedAnime);


         var animesFound = service.findAllPaginated(pageRequest);

         Assertions.assertThat(animesFound).isNotNull().hasSameElementsAs(animesList);
     }
 
     @Test
     @DisplayName("findAll returns list with found object when name exists")
     @Order(2)
     void findByName_ReturnsFoundAnimeInList_WhenNameIsFound() {
         var anime = animesList.getFirst();
         var expectedAnimesFound = singletonList(anime);
 
         BDDMockito.when(repository.findByName(anime.getName())).thenReturn(expectedAnimesFound);
 
         var animesFound = service.findAll(anime.getName());
         org.assertj.core.api.Assertions.assertThat(animesFound).containsAll(expectedAnimesFound);
     }
 
     @Test
     @DisplayName("findAll returns empty list when name is not found")
     @Order(3)
     void findByName_ReturnsEmptyList_WhenNameIsNotFound() {
         var name = "not-found";
         BDDMockito.when(repository.findByName(name)).thenReturn(emptyList());
 
         var animes = service.findAll(name);
         org.assertj.core.api.Assertions.assertThat(animes).isNotNull().isEmpty();
     }
 
     @Test
     @DisplayName("findById returns an anime with given id")
     @Order(4)
     void findById_ReturnsAnimeById_WhenSuccessful() {
         var expectedAnime = animesList.getFirst();
         BDDMockito.when(repository.findById(expectedAnime.getId())).thenReturn(Optional.of(expectedAnime));
 
         var animes = service.findByIdOrThrowNotFound(expectedAnime.getId());
 
         org.assertj.core.api.Assertions.assertThat(animes).isEqualTo(expectedAnime);
     }
 
     @Test
     @DisplayName("findById throws ResponseStatusException when anime is not found")
     @Order(5)
     void findById_ThrowsResponseStatusException_WhenAnimeIsNotFound() {
         var expectedAnime = animesList.getFirst();
         BDDMockito.when(repository.findById(expectedAnime.getId())).thenReturn(Optional.empty());
 
         org.assertj.core.api.Assertions.assertThatException()
                 .isThrownBy(() -> service.findByIdOrThrowNotFound(expectedAnime.getId()))
                 .isInstanceOf(ResponseStatusException.class);
     }
 
     @Test
     @DisplayName("save creates an anime")
     @Order(6)
     void save_CreatesAnime_WhenSuccessful() {
         var animeToSave = animeUtils.newAnimeToSave();
 
         BDDMockito.when(repository.save(animeToSave)).thenReturn(animeToSave);
 
         var savedAnime = service.save(animeToSave);
 
         org.assertj.core.api.Assertions.assertThat(savedAnime).isEqualTo(animeToSave).hasNoNullFieldsOrProperties();
     }
 
     @Test
     @DisplayName("delete removes an anime")
     @Order(7)
     void delete_RemoveAnime_WhenSuccessful() {
         var animeToDelete = animesList.getFirst();
         BDDMockito.when(repository.findById(animeToDelete.getId())).thenReturn(Optional.of(animeToDelete));
         BDDMockito.doNothing().when(repository).delete(animeToDelete);
 
         org.assertj.core.api.Assertions.assertThatNoException().isThrownBy(() -> service.delete(animeToDelete.getId()));
     }
 
     @Test
     @DisplayName("delete throws ResponseStatusException when anime is not found")
     @Order(8)
     void delete_ThrowsResponseStatusException_WhenAnimeIsNotFound() {
         var animeToDelete = animesList.getFirst();
         BDDMockito.when(repository.findById(animeToDelete.getId())).thenReturn(Optional.empty());
 
         org.assertj.core.api.Assertions.assertThatException()
                 .isThrownBy(() -> service.delete(animeToDelete.getId()))
                 .isInstanceOf(ResponseStatusException.class);
     }
 
     @Test
     @DisplayName("update updates an anime")
     @Order(9)
     void update_UpdatesAnime_WhenSuccessful() {
         var animeToUpdate = animesList.getFirst();
         animeToUpdate.setName("Grand Blue");
 
         BDDMockito.when(repository.findById(animeToUpdate.getId())).thenReturn(Optional.of(animeToUpdate));
         BDDMockito.when(repository.save(animeToUpdate)).thenReturn(animeToUpdate);
 
         org.assertj.core.api.Assertions.assertThatNoException().isThrownBy(() -> service.update(animeToUpdate));
     }
 
     @Test
     @DisplayName("update throws ResponseStatusException when anime is not found")
     @Order(10)
     void update_ThrowsResponseStatusException_WhenProducerIsNotFound() {
         var animeToUpdate = animesList.getFirst();
 
         BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());
 
         Assertions.assertThatException()
                 .isThrownBy(() -> service.update(animeToUpdate))
                 .isInstanceOf(ResponseStatusException.class);
     }
 
 }