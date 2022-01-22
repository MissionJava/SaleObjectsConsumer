package se.quedro.salesdatareader;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.quedro.dto.SaleObject;
import se.quedro.dto.SaleObjects;
import se.quedro.deserialization.CustomXmlDeserializer;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class XMLSalesDataReader implements SalesDataReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(XMLSalesDataReader.class);
    private final JacksonXmlModule module;
    private final ObjectMapper xmlMapper;

    public XMLSalesDataReader() {
        module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        module.addDeserializer(SaleObject.class, new CustomXmlDeserializer());

        xmlMapper = new XmlMapper(module);
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        xmlMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        xmlMapper.setLocale(Locale.forLanguageTag("sv-SE"));
    }

    @Override
    public List<SaleObject> getSaleObjects(Optional<String> filename) {
        if (filename.isPresent()) {
            ClassLoader classLoader = this.getClass().getClassLoader();
            File file = new File(filename.get());
            try {
                String data = FileUtils.readFileToString(file, "UTF-8");
                SaleObjects saleObjects = xmlMapper.readValue(data, SaleObjects.class);

                return Objects.nonNull(saleObjects) ? saleObjects.getSaleObjects() : Collections.emptyList();
            } catch (IOException e) {
                LOGGER.error("Exception occurred while populating sale objects list from xml file: ", e.getMessage());
            }
        }

        return Collections.emptyList();
    }
}
