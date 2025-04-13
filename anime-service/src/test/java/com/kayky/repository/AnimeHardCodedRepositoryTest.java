package com.kayky.repository;

import com.kayky.commons.AnimeUtils;
import com.kayky.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
 @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
 class AnimeHardCodedRepositoryTest {
     @InjectMocks
     private AnimeHardCodedRepository respository;
     @Mock
     private AnimeData animeData;
     private List<Anime> animesList;

    @InjectMocks
    private AnimeUtils animeUtils;
 
     @BeforeEach
     void init() {
        animesList = animeUtils.newAnimeList();
     }
 
     @Test
     @DisplayName("findAll returns a list with all animes")
     @Order(1)
     void findAll_ReturnsAllAnimes_WhenSuccessful() {
         BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
 
         var animes = respository.findAll();
         org.assertj.core.api.Assertions.assertThat(animes).isNotNull().hasSameElementsAs(animesList);
     }
 
     @Test
     @DisplayName("findById returns an anime with given id")
     @Order(2)
     void findById_ReturnsAnimeById_WhenSuccessful() {
         BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
 
         var expectedAnime = animesList.getFirst();
         var animes = respository.findById(expectedAnime.getId());
         org.assertj.core.api.Assertions.assertThat(animes).isPresent().contains(expectedAnime);
     }
 
     @Test
     @DisplayName("findByName returns empty list when name is null")
     @Order(3)
     void findByName_ReturnsEmptyList_WhenNameIsNull() {
         BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
 
         var animes = respository.findByName(null);
         org.assertj.core.api.Assertions.assertThat(animes).isNotNull().isEmpty();
     }
 
     @Test
     @DisplayName("findByName returns list with found object when name exists")
     @Order(4)
     void findByName_ReturnsFoundAnimeInList_WhenNameIsFound() {
         BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
 
         var expectedAnime = animesList.getFirst();
         var animes = respository.findByName(expectedAnime.getName());
         org.assertj.core.api.Assertions.assertThat(animes).hasSize(1).contains(expectedAnime);
     }
 
     @Test
     @DisplayName("save creates an anime")
     @Order(5)
     void save_CreatesAnime_WhenSuccessful() {
         BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
 
         var animeToSave = animeUtils.newAnimeToSave();
         var anime = respository.save(animeToSave);
 
         org.assertj.core.api.Assertions.assertThat(anime).isEqualTo(animeToSave).hasNoNullFieldsOrProperties();
 
         var animeSavedOptional = respository.findById(animeToSave.getId());
 
         org.assertj.core.api.Assertions.assertThat(animeSavedOptional).isPresent().contains(animeToSave);
     }
 
     @Test
     @DisplayName("delete removes an anime")
     @Order(6)
     void delete_RemoveAnime_WhenSuccessful() {
         BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
 
         var animeToDelete = animesList.getFirst();
         respository.delete(animeToDelete);
 
         var animes = respository.findAll();
 
         org.assertj.core.api.Assertions.assertThat(animes).isNotEmpty().doesNotContain(animeToDelete);
     }
 
     @Test
     @DisplayName("update updates an anime")
     @Order(7)
     void update_UpdatesAnime_WhenSuccessful() {
         BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
         var animeToUpdate = this.animesList.getFirst();
         animeToUpdate.setName("Hellsing");
 
         respository.update(animeToUpdate);
 
         org.assertj.core.api.Assertions.assertThat(this.animesList).contains(animeToUpdate);
 
         var animeUpdatedOptional = respository.findById(animeToUpdate.getId());
 
         org.assertj.core.api.Assertions.assertThat(animeUpdatedOptional).isPresent();
         Assertions.assertThat(animeUpdatedOptional.get().getName()).isEqualTo(animeToUpdate.getName());
     }
 
 }