package se.quedro.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@JacksonXmlRootElement(localName = "SaleObjects")
@Builder
@Data
@Jacksonized
public class SaleObjects {

    @JsonProperty(value = "numberOfSaleObjects", required = true)
    @JacksonXmlProperty(isAttribute = true, localName = "count")
    private long count;

    @JsonProperty(value = "saleObjects", required = true)
    @JacksonXmlProperty(isAttribute = false, localName = "SaleObject")
    private List<SaleObject> SaleObjects;

}
