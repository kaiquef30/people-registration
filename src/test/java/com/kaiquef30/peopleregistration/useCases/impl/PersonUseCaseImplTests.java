package com.kaiquef30.peopleregistration.useCases.impl;

import com.kaiquef30.peopleregistration.domains.Address;
import com.kaiquef30.peopleregistration.domains.Person;
import com.kaiquef30.peopleregistration.dtos.AddressDTO;
import com.kaiquef30.peopleregistration.dtos.PersonDTO;
import com.kaiquef30.peopleregistration.exceptions.ConflictException;
import com.kaiquef30.peopleregistration.exceptions.ResourceNotFoundException;
import com.kaiquef30.peopleregistration.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class PersonUseCaseImplTests {


    @Mock
    private PersonRepository personRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PagedResourcesAssembler<PersonDTO> pagedResourcesAssembler;

    @InjectMocks
    private PersonUseCaseImpl personUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void getAllPersons_ReturnsPageOfPersonDTO() {
        Pageable pageable = Pageable.unpaged();
        PageImpl<Person> personPage = new PageImpl<>(Collections.singletonList(new Person()));
        when(personRepository.findAll(pageable)).thenReturn(personPage);
        when(modelMapper.map(any(), eq(PersonDTO.class))).thenReturn(new PersonDTO());

        personUseCase.getAllPeople(pageable);

        verify(pagedResourcesAssembler).toModel(any(Page.class), (RepresentationModelAssembler<PersonDTO, RepresentationModel<?>>) any());
    }

    @Test
    void getPersonById_ExistingId_ReturnsEntityModel() {
        Long id = 1L;
        var person = new Person();
        when(personRepository.findById(id)).thenReturn(Optional.of(person));
        when(modelMapper.map(person, PersonDTO.class)).thenReturn(new PersonDTO());

        personUseCase.getPersonById(id);

        verify(modelMapper).map(person, PersonDTO.class);
    }

    @Test
    void createPerson_SetsAddressPersonRelationship() {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setAddress(Collections.singletonList(new AddressDTO()));
        var person = new Person();
        when(modelMapper.map(personDTO, Person.class)).thenReturn(person);
        when(personRepository.existsByEmail(anyString())).thenReturn(false);
        when(personRepository.save(person)).thenReturn(person);
        when(modelMapper.map(person, PersonDTO.class)).thenReturn(personDTO);

        PersonDTO createdPerson = personUseCase.createPerson(personDTO);

        assertNotNull(createdPerson);
        assertNotNull(createdPerson.getAddress());
        assertFalse(createdPerson.getAddress().isEmpty());
        assertEquals(createdPerson, personDTO);
        for (Address address : person.getAddress()) {
            assertSame(person, address.getPerson());
        }
    }

    @Test
    void createPerson_ExistingEmail_ThrowsConflictException() {
        var personDTO = new PersonDTO();
        personDTO.setEmail("kaique@gmail.com");
        when(personRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(ConflictException.class, () -> personUseCase.createPerson(personDTO));
    }

    @Test
    void updatePerson_ExistingId_ReturnsUpdatedPersonDTO() {
        Long id = 1L;
        var personDTO = new PersonDTO();
        var existingPerson = new Person();
        when(personRepository.findById(id)).thenReturn(Optional.of(existingPerson));
        when(personRepository.save(existingPerson)).thenReturn(existingPerson);
        when(modelMapper.map(existingPerson, PersonDTO.class)).thenReturn(personDTO);

        PersonDTO updatedPerson = personUseCase.updatePerson(personDTO, id);

        assertNotNull(updatedPerson);
        assertEquals(personDTO, updatedPerson);
    }


    @Test
    void deletePerson_ExistingId_DeletesPerson() {
        Long id = 1L;

        personUseCase.deletePerson(id);

        verify(personRepository).deleteById(id);
    }


    @Test
    void getPersonById_NonExistingId_ThrowsResourceNotFoundException() {
        Long id = 1L;
        when(personRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> personUseCase.getPersonById(id));
    }

}
