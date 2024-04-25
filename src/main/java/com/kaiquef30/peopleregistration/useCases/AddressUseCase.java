package com.kaiquef30.peopleregistration.useCases;

import com.kaiquef30.peopleregistration.dtos.AddressDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

public interface AddressUseCase {

    PagedModel<EntityModel<AddressDTO>> getAllAddresses(Pageable pageable);

    EntityModel<AddressDTO> getAddressById(Long id);

    AddressDTO createAddress(AddressDTO AddressDTO, Long personId);

    AddressDTO updateAddress(AddressDTO addressDTO, Long id);

    void deleteAddress(Long id);

    void setMainAddress(Long addressId, Long personId);
}
