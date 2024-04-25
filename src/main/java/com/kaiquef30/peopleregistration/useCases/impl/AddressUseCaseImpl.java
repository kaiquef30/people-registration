package com.kaiquef30.peopleregistration.useCases.impl;

import com.kaiquef30.peopleregistration.dtos.AddressDTO;
import com.kaiquef30.peopleregistration.domains.Address;
import com.kaiquef30.peopleregistration.domains.Person;
import com.kaiquef30.peopleregistration.exceptions.ResourceNotFoundException;
import com.kaiquef30.peopleregistration.repositories.AddressRepository;
import com.kaiquef30.peopleregistration.repositories.PersonRepository;
import com.kaiquef30.peopleregistration.resources.AddressResource;
import com.kaiquef30.peopleregistration.useCases.AddressUseCase;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

@Service
public class AddressUseCaseImpl implements AddressUseCase {

    private final AddressRepository addressRepository;

    private final PersonRepository personRepository;

    private final ModelMapper modelMapper;

    private final PagedResourcesAssembler<AddressDTO> pagedResourcesAssembler;

    public AddressUseCaseImpl(AddressRepository addressRepository, PersonRepository personRepository,
                              ModelMapper modelMapper, PagedResourcesAssembler<AddressDTO> pagedResourcesAssembler) {
        this.addressRepository = addressRepository;
        this.personRepository = personRepository;
        this.modelMapper = modelMapper;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Override
    public PagedModel<EntityModel<AddressDTO>> getAllAddresses(Pageable pageable) {
        Page<Address> addressPage = addressRepository.findAll(pageable);

        Page<AddressDTO> addressDTOPage = addressPage.map(address -> modelMapper.map(address, AddressDTO.class));

        return pagedResourcesAssembler.toModel(addressDTOPage, address -> EntityModel.of(address,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AddressResource.class)
                                .getAddressById(address.getId()))
                        .withSelfRel()));
    }

    @Override
    public EntityModel<AddressDTO> getAddressById(Long id) {
        Address address = getAddressByIdOrElseThrow(id);
        AddressDTO addressDTO = modelMapper.map(address, AddressDTO.class);
        EntityModel<AddressDTO> entityModel = EntityModel.of(addressDTO);

        entityModel.add(Link.of(String.valueOf(WebMvcLinkBuilder.linkTo(AddressResource.class).withRel("addresses"))));

        return entityModel;
    }

    @Transactional
    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, Long personId) {
        Person person = getPersonByIdOrElseThrow(personId);

        Address address = modelMapper.map(addressDTO, Address.class);
        address.setPerson(person);
        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Transactional
    @Override
    public AddressDTO updateAddress(AddressDTO addressDTO, Long id) {
        Address existingAddress = getAddressByIdOrElseThrow(id);
        modelMapper.map(addressDTO, existingAddress);
        Address updatedAddress = addressRepository.save(existingAddress);
        return modelMapper.map(updatedAddress, AddressDTO.class);
    }

    @Transactional
    @Override
    public void deleteAddress(Long id) {
        addressRepository.deleteById(id);
    }

    @Override
    public void setMainAddress(Long addressId, Long personId) {
        Person person = getPersonByIdOrElseThrow(personId);
        Address address = getAddressByIdOrElseThrow(addressId);

        person.getAddress().forEach(a -> a.setMainAddress(a.getId().equals(addressId)));
        personRepository.save(person);
    }

    private Person getPersonByIdOrElseThrow(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada com o ID: " + id));
    }

    private Address getAddressByIdOrElseThrow(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrada com o ID: " + id));
    }
}
