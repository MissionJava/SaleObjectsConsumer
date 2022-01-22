package se.quedro.salesdatareader;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.quedro.dto.SaleObject;
import se.quedro.dto.SaleObjects;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class JSONSalesDataReader implements SalesDataReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(JSONSalesDataReader.class);
    private ObjectMapper objectMapper;

    public JSONSalesDataReader() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
    }

    @Override
    public List<SaleObject> getSaleObjects(Optional<String> filename) {
        if (filename.isPresent()) {
            File file = new File(filename.get());
            try {
                String data = FileUtils.readFileToString(file, "UTF-8");
                SaleObjects saleObjects = objectMapper.readValue(data, SaleObjects.class);
                return Objects.nonNull(saleObjects) ? saleObjects.getSaleObjects() : Collections.emptyList();
            } catch (IOException e) {
                LOGGER.error("Exception occurred while populating sale objects list from json file: ", e);
            }
        }

        return Collections.emptyList();

    }
}
