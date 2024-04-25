package com.kaiquef30.peopleregistration.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;
import java.util.List;

@Relation(collectionRelation = "personList")
@Schema(hidden = true)
public class PersonDTO extends RepresentationModel<PersonDTO> {

    @JsonIgnore
    private Long id;

    @NotNull
    @NotBlank
    @Schema(description = "Your full name", example = "Maria Drummond Rodrigues")
    private String fullName;

    @NotNull
    @NotBlank
    @Email
    @Schema(description = "Your e-mail", example = "mariadrummond@outlook.com")
    private String email;

    @NotNull
    @Schema(description = "Your date of birth", example = "2000-04-11")
    private LocalDate birthDate;

    @NotNull
    private List<AddressDTO> address;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public List<AddressDTO> getAddress() {
        return address;
    }

    public void setAddress(List<AddressDTO> address) {
        this.address = address;
    }
}
