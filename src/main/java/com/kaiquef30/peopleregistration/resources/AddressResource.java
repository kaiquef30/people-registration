package com.kaiquef30.peopleregistration.resources;

import com.kaiquef30.peopleregistration.configs.rateLimitAnnotation.WithRateLimitProtection;
import com.kaiquef30.peopleregistration.dtos.AddressDTO;
import com.kaiquef30.peopleregistration.useCases.AddressUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/addresses")
@Tag(name = "Addresses", description = "Endpoints for address management")
public class AddressResource {

    private final AddressUseCase addressUseCase;

    public AddressResource(AddressUseCase addressUseCase) {
        this.addressUseCase = addressUseCase;
    }

    @Operation(summary = "List", description = "Method that lists all addresses",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AddressDTO.class))
                    ),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)})
    @WithRateLimitProtection
    @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PagedModel<EntityModel<AddressDTO>>> getAllAddresses(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        PagedModel<EntityModel<AddressDTO>> addressPagedModel = addressUseCase.getAllAddresses(pageable);

        return ResponseEntity.ok(addressPagedModel);
    }

    @Operation(summary = "Obtain one", description = "Get an address by id",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AddressDTO.class))
                    ),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)})
    @WithRateLimitProtection
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<EntityModel<AddressDTO>> getAddressById(@PathVariable Long id) {
        EntityModel<AddressDTO> addressEntityModel = addressUseCase.getAddressById(id);

        return ResponseEntity.ok(addressEntityModel);
    }


    @Operation(summary = "Register", description = "Register address for a specific person",
            responses = {
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)})
    @WithRateLimitProtection
    @PostMapping(value = "/register/{personId}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO, @PathVariable Long personId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(addressUseCase.createAddress(addressDTO, personId));
    }

    @Operation(summary = "Update", description = "Update an address", responses = {
            @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)})
    @WithRateLimitProtection
    @PutMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AddressDTO> updateAddress(@Valid @RequestBody AddressDTO addressDTO, @PathVariable Long id) {
        return ResponseEntity.ok(addressUseCase.updateAddress(addressDTO, id));
    }

    @Operation(summary = "Delete", description = "Delete an address",
            responses = {
                    @ApiResponse(
                            description = "No content",
                            responseCode = "204",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)})
    @WithRateLimitProtection
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        addressUseCase.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }

    
    @Operation(summary = "Mark as main", description = "Mark an address as the primary address for a specific person",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)})
    @WithRateLimitProtection
    @PutMapping("/{addressId}/set-main/{personId}")
    public ResponseEntity<Void> setMainAddress(@PathVariable Long addressId, @PathVariable Long personId) {
        addressUseCase.setMainAddress(personId, addressId);
        return ResponseEntity.ok().build();
    }

}
