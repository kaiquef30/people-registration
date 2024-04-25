package com.kaiquef30.peopleregistration.useCases.impl;

import com.kaiquef30.peopleregistration.domains.Address;
import com.kaiquef30.peopleregistration.domains.Person;
import com.kaiquef30.peopleregistration.dtos.AddressDTO;
import com.kaiquef30.peopleregistration.exceptions.ResourceNotFoundException;
import com.kaiquef30.peopleregistration.repositories.AddressRepository;
import com.kaiquef30.peopleregistration.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class AddressUseCaseImplTests {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PagedResourcesAssembler<AddressDTO> pagedResourcesAssembler;

    @InjectMocks
    private AddressUseCaseImpl addressUseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllAddresses_ReturnsPageOfAddressDTO() {
        Pageable pageable = Pageable.unpaged();
        PageImpl<Address> addressPage = new PageImpl<>(Collections.singletonList(new Address()));
        when(addressRepository.findAll(pageable)).thenReturn(addressPage);
        when(modelMapper.map(any(), eq(AddressDTO.class))).thenReturn(new AddressDTO());

        addressUseCase.getAllAddresses(pageable);

        verify(pagedResourcesAssembler).toModel(any(), (RepresentationModelAssembler<AddressDTO, RepresentationModel<?>>) any());
    }

    @Test
    void getAddressById_ExistingId_ReturnsEntityModel() {
        Long id = 1L;
        var address = new Address();
        when(addressRepository.findById(id)).thenReturn(Optional.of(address));
        when(modelMapper.map(address, AddressDTO.class)).thenReturn(new AddressDTO());

        addressUseCase.getAddressById(id);

        verify(modelMapper).map(address, AddressDTO.class);
    }

    @Test
    void createAddress_ValidAddressDTO_ReturnsAddressDTO() {
        Long personId = 1L;
        var addressDTO = new AddressDTO();
        var person = new Person();
        when(personRepository.findById(personId)).thenReturn(Optional.of(person));
        when(modelMapper.map(addressDTO, Address.class)).thenReturn(new Address());
        when(addressRepository.save(any())).thenReturn(new Address());
        when(modelMapper.map(any(), eq(AddressDTO.class))).thenReturn(addressDTO);

        AddressDTO createdAddress = addressUseCase.createAddress(addressDTO, personId);

        assertNotNull(createdAddress);
        assertEquals(addressDTO, createdAddress);
    }

    @Test
    void createAddress_InvalidPersonId_ThrowsResourceNotFoundException() {
        Long personId = 1L;
        var addressDTO = new AddressDTO();
        when(personRepository.findById(personId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> addressUseCase.createAddress(addressDTO, personId));
    }

    @Test
    void updateAddress_ExistingId_ReturnsUpdatedAddressDTO() {
        Long id = 1L;
        var addressDTO = new AddressDTO();
        var existingAddress = new Address();
        when(addressRepository.findById(id)).thenReturn(Optional.of(existingAddress));
        when(addressRepository.save(existingAddress)).thenReturn(existingAddress);
        when(modelMapper.map(existingAddress, AddressDTO.class)).thenReturn(addressDTO);

        AddressDTO updatedAddress = addressUseCase.updateAddress(addressDTO, id);

        assertNotNull(updatedAddress);
        assertEquals(addressDTO, updatedAddress);
    }

    @Test
    void deleteAddress_ExistingId_DeletesAddress() {
        Long id = 1L;

        addressUseCase.deleteAddress(id);

        verify(addressRepository).deleteById(id);
    }

    @Test
    void setMainAddress_ValidIds_SetsMainAddress() {
        Long personId = 1L;
        Long addressId = 1L;
        var person = new Person();
        var address = new Address();
        address.setId(addressId);
        person.getAddress().add(address);
        when(personRepository.findById(personId)).thenReturn(Optional.of(person));
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));
        when(personRepository.save(person)).thenReturn(person);

        addressUseCase.setMainAddress(personId, addressId);

        assertTrue(person.getAddress().stream().anyMatch(Address::isMainAddress));
    }

    @Test
    void setMainAddress_InvalidPersonId_ThrowsResourceNotFoundException() {
        Long personId = 1L;
        Long addressId = 1L;
        when(personRepository.findById(personId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> addressUseCase.setMainAddress(personId, addressId));
    }


    @Test
    void setMainAddress_InvalidAddressId_ThrowsResourceNotFoundException() {
        Long personId = 1L;
        Long addressId = 1L;
        var person = new Person();
        when(personRepository.findById(personId)).thenReturn(Optional.of(person));
        when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> addressUseCase.setMainAddress(personId, addressId));
    }
}
