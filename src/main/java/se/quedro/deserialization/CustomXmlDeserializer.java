package se.quedro.deserialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import se.quedro.dto.Address;
import se.quedro.dto.SaleObject;

import java.io.IOException;

import static se.quedro.utils.Constants.*;

public class CustomXmlDeserializer extends StdDeserializer<SaleObject> {

    public CustomXmlDeserializer() {
        super(SaleObject.class);
    }

    @Override
    public SaleObject deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);
        String type = node.get(TYPE) != null ? node.get(TYPE).asText() : "";
        int id = node.get(ID) != null ? Integer.parseInt(node.get(ID).asText()) : 0;
        int sizeSqm = node.get(SIZE_SQM) != null ? Integer.parseInt(node.get(SIZE_SQM).asText()) : 0;
        String startingPrice = node.get(STARTING_PRICE) != null ? node.get(STARTING_PRICE).asText() : "";
        long startingPriceValue;
        if (startingPrice.contains(".") || startingPrice.contains(".")) {
            startingPrice = startingPrice.replace(".", "");
            startingPrice = startingPrice.replace(",", "");
        }
        startingPriceValue = Long.valueOf(startingPrice);

        JsonNode addressNode = node.get(ADDRESS) != null ? node.get(ADDRESS) : null;
        String city = addressNode.get(CITY) != null ? addressNode.get(CITY).asText() : "";
        String street = addressNode.get(STREET) != null ? addressNode.get(STREET).asText() : "";

        String floor = addressNode.get(FLOOR) != null ? addressNode.get(FLOOR).asText() : "";
        int floorValue = floor.equals("") ? 0 : Integer.parseInt(floor);

        Address address = new Address(city, street, floorValue);


        return new SaleObject(type, id, sizeSqm, startingPriceValue, address);

    }
}