package com.kaiquef30.peopleregistration.resources;

import com.kaiquef30.peopleregistration.configs.rateLimitAnnotation.WithRateLimitProtection;
import com.kaiquef30.peopleregistration.dtos.PersonDTO;
import com.kaiquef30.peopleregistration.useCases.PersonUseCase;
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
@RequestMapping("/api/people")
@Tag(name = "People", description = "Endpoints for people management")
public class PersonResource {

    private final PersonUseCase personUseCase;

    public PersonResource(PersonUseCase personUseCase) {
        this.personUseCase = personUseCase;
    }

    @Operation(summary = "List", description = "List all people",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))
                    ),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)})
    @WithRateLimitProtection
    @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> getAllPeople(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        PagedModel<EntityModel<PersonDTO>> pagedModel = personUseCase.getAllPeople(pageable);

        return ResponseEntity.ok(pagedModel);
    }

    @Operation(summary = "Obtain one", description = "Get a person by id",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))
                    ),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)})
    @WithRateLimitProtection
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<EntityModel<PersonDTO>> getPersonById(@PathVariable Long id) {
        EntityModel<PersonDTO> personDTOEntityModel = personUseCase.getPersonById(id);

        return ResponseEntity.ok(personDTOEntityModel);
    }

    @Operation(summary = "Register", description = "Register a person",
            responses = {
                    @ApiResponse(description = "Conflict", responseCode = "409", content = @Content),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)})
    @WithRateLimitProtection
    @PostMapping(value = "/register",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PersonDTO> createPerson(@Valid @RequestBody PersonDTO personDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personUseCase.createPerson(personDTO));
    }

    @Operation(summary = "Update", description = "Update a person",
            responses = {
                    @ApiResponse(description = "Conflict", responseCode = "409", content = @Content),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)})
    @WithRateLimitProtection
    @PutMapping(value = "/update/{id}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PersonDTO> updatePerson(@Valid @RequestBody PersonDTO personDTO, @PathVariable Long id) {
        return ResponseEntity.ok(personUseCase.updatePerson(personDTO, id));
    }

    @Operation(summary = "Delete", description = "Delete a person",
            responses = {
                    @ApiResponse(
                            description = "No content",
                            responseCode = "204",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)})
    @WithRateLimitProtection
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        personUseCase.deletePerson(id);
        return ResponseEntity.noContent().build();
    }

}
