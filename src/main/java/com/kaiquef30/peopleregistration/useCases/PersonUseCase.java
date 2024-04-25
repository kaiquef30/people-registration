package com.kaiquef30.peopleregistration.useCases;

import com.kaiquef30.peopleregistration.dtos.PersonDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

public interface PersonUseCase {

    PagedModel<EntityModel<PersonDTO>> getAllPeople(Pageable pageable);

    EntityModel<PersonDTO> getPersonById(Long id);

    PersonDTO createPerson(PersonDTO personDTO);

    PersonDTO updatePerson(PersonDTO personDTO, Long id);

    void deletePerson(Long id);

}
