package se.quedro.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

//@JsonPropertyOrder({"type", "id", "sizeSqm", "startingPrice", "address"})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
@JsonPropertyOrder({ "type", "id", "sizeSqm", "startingPrice", "address" })
public class SaleObject {
    @JacksonXmlProperty(isAttribute = true)
    @JsonProperty(required = true)
    private String type;

    @JacksonXmlProperty(isAttribute = true)
    @JsonProperty(required = true)
    private int id;

    @JsonProperty(required = true)
    private int sizeSqm;

    @JsonProperty(required = true)
    private long startingPrice;

    @JsonProperty(value = "postalAddress", required = true)
    @JacksonXmlProperty(localName = "address")
    private Address address;

}
