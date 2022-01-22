package se.quedro.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import javax.management.ConstructorParameters;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Jacksonized
@JsonPropertyOrder({ "city", "street", "floor" })
public class Address {

    @JsonProperty(required = true, defaultValue = "")
    private String city;

    @JsonProperty(required = true)
    private String street;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int floor;

}
