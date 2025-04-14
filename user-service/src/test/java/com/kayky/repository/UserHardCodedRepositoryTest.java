package com.kayky.repository;

import com.kayky.commons.UserUtils;
import com.kayky.domain.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
 @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
 class UserHardCodedRepositoryTest {
     @InjectMocks
     private UserHardCodedRepository respository;
     @Mock
     private UserData userData;
     private List<User> userList;
     @InjectMocks
     private UserUtils userUtils;
 
     @BeforeEach
     void init() {
         userList = userUtils.newUserList();
     }
 
     @Test
     @DisplayName("findAll returns a list with all users")
     @Order(1)
     void findAll_ReturnsAllUsers_WhenSuccessful() {
         BDDMockito.when(userData.getUsers()).thenReturn(userList);
 
         var users = respository.findAll();
         assertThat(users).isNotNull().hasSameElementsAs(userList);
     }

    @Test
    @DisplayName("findById returns an user with given id")
    @Order(2)
    void findById_ReturnsUserById_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var expectedUser = userList.getFirst();
        var users = respository.findById(expectedUser.getId());
        assertThat(users).isPresent().contains(expectedUser);
    }

    @Test
    @DisplayName("findByFirstName returns empty list when first name is null")
    @Order(3)
    void findByFirstName_ReturnsEmptyList_WhenFirstNameIsNull() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var users = respository.findByFirstName(null);
        assertThat(users).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findByFistName returns list with found object when first name exists")
    @Order(4)
    void findByFirstName_ReturnsFoundUserInList_WhenFirstNameIsFound() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var expectedUser = userList.getFirst();
        var users = respository.findByFirstName(expectedUser.getFirstName());
        assertThat(users).hasSize(1).contains(expectedUser);
    }

    @Test
    @DisplayName("save creates an user")
    @Order(5)
    void save_CreatesUser_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var userToSave = userUtils.newUserToSave();
        var user = respository.save(userToSave);

        assertThat(user).isEqualTo(userToSave).hasNoNullFieldsOrProperties();

        var userSavedOptional = respository.findById(userToSave.getId());

        assertThat(userSavedOptional).isPresent().contains(userToSave);
    }

    @Test
    @DisplayName("delete removes an user")
    @Order(6)
    void delete_RemoveUser_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var userToDelete = userList.getFirst();
        respository.delete(userToDelete);

        var users = respository.findAll();

        assertThat(users).isNotEmpty().doesNotContain(userToDelete);
    }

    @Test
    @DisplayName("update updates an user")
    @Order(7)
    void update_UpdatesUser_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
        var userToUpdate = this.userList.getFirst();
        userToUpdate.setFirstName("Inuyasha");

        respository.update(userToUpdate);

        assertThat(this.userList).contains(userToUpdate);

        var userUpdatedOptional = respository.findById(userToUpdate.getId());

        assertThat(userUpdatedOptional).isPresent();
        assertThat(userUpdatedOptional.get().getFirstName()).isEqualTo(userToUpdate.getFirstName());
    }
 }