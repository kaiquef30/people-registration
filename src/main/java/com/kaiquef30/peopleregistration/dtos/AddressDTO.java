package com.kaiquef30.peopleregistration.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;


@Relation(collectionRelation = "addressList")
@Schema(hidden = true)
public class AddressDTO extends RepresentationModel<AddressDTO> {

    @JsonIgnore
    private Long id;

    @NotNull
    @NotBlank
    @Schema(description = "Your street", example = "Rua das flores")
    private String street;

    @NotNull
    @NotBlank
    @Schema(description = "Your zip´code", example = "0984-88")
    private String cep;

    @NotNull
    @NotBlank
    @Schema(description = "Your house Number", example = "756")
    private String number;

    @NotNull
    @NotBlank
    @Schema(description = "Your city", example = "Tubarão")
    private String city;

    @NotNull
    @NotBlank
    @Schema(description = "Your state", example = "Santa Catarina")
    private String state;


    private boolean isMainAddress;

    public boolean isMainAddress() {
        return isMainAddress;
    }

    public void setMainAddress(boolean mainAddress) {
        isMainAddress = mainAddress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
