package com.kaiquef30.peopleregistration.useCases.impl;

import com.kaiquef30.peopleregistration.dtos.PersonDTO;
import com.kaiquef30.peopleregistration.domains.Address;
import com.kaiquef30.peopleregistration.domains.Person;
import com.kaiquef30.peopleregistration.exceptions.ConflictException;
import com.kaiquef30.peopleregistration.exceptions.ResourceNotFoundException;
import com.kaiquef30.peopleregistration.repositories.PersonRepository;
import com.kaiquef30.peopleregistration.resources.PersonResource;
import com.kaiquef30.peopleregistration.useCases.PersonUseCase;
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
public class PersonUseCaseImpl implements PersonUseCase {

    private final PersonRepository personRepository;

    private final ModelMapper modelMapper;

    private final PagedResourcesAssembler<PersonDTO> pagedResourcesAssembler;

    public PersonUseCaseImpl(PersonRepository personRepository, ModelMapper modelMapper,
                             PagedResourcesAssembler<PersonDTO> pagedResourcesAssembler) {
        this.personRepository = personRepository;
        this.modelMapper = modelMapper;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Override
    public PagedModel<EntityModel<PersonDTO>> getAllPeople(Pageable pageable) {
        Page<Person> personPage = personRepository.findAll(pageable);

        Page<PersonDTO> personDTOPage = personPage.map(person -> modelMapper.map(person, PersonDTO.class));

        return pagedResourcesAssembler.toModel(personDTOPage, person -> EntityModel.of(person,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonResource.class).getPersonById(person.getId()))
                        .withSelfRel()));
    }

    @Override
    public EntityModel<PersonDTO> getPersonById(Long id) {
        Person person = getPersonByIdOrElseThrow(id);
        PersonDTO personDTO = modelMapper.map(person, PersonDTO.class);
        EntityModel<PersonDTO> entityModel = EntityModel.of(personDTO);

        entityModel.add(Link.of(String.valueOf(WebMvcLinkBuilder.linkTo(PersonResource.class).withRel("Persons"))));

        return entityModel;
    }

    @Transactional
    @Override
    public PersonDTO createPerson(PersonDTO personDTO) {
        Person person = modelMapper.map(personDTO, Person.class);

        getPersonByEmailOrElseThrow(personDTO.getEmail());

        for (Address address : person.getAddress()) {
            address.setPerson(person);
        }
        Person savedPerson = personRepository.save(person);

        return modelMapper.map(savedPerson, PersonDTO.class);
    }

    @Transactional
    @Override
    public PersonDTO updatePerson(PersonDTO personDTO, Long id) {
        Person existingPerson = getPersonByIdOrElseThrow(id);
        modelMapper.map(personDTO, existingPerson);
        Person updatedPerson = personRepository.save(existingPerson);
        return modelMapper.map(updatedPerson, PersonDTO.class);
    }

    @Transactional
    @Override
    public void deletePerson(Long id) {
        personRepository.deleteById(id);
    }

    private Person getPersonByIdOrElseThrow(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada com o ID: " + id));
    }

    private void getPersonByEmailOrElseThrow(String email) {
        if (personRepository.existsByEmail(email)) {
            throw new ConflictException("Já existe uma pessoa cadastrada com o e-mail: " + email);
        }
    }
}
