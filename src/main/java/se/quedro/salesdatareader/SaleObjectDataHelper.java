package se.quedro.salesdatareader;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.quedro.dto.SaleObject;
import se.quedro.salesdatareader.SalesDataReader;
import se.quedro.salesdatareader.JSONSalesDataReader;
import se.quedro.salesdatareader.XMLSalesDataReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SaleObjectDataHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(SaleObjectDataHelper.class);

    public SaleObjectDataHelper() {

    }

    public List<SaleObject> readFilesAndPopulateSaleObjectList(List<String> files) {
        List<SaleObject> saleObjects = new ArrayList<>();
        files.stream().forEach(file -> {
            switch (FilenameUtils.getExtension(file)) {
                case "json":
                    SalesDataReader jsonSalesDataReader = new JSONSalesDataReader();
                    List<SaleObject> jsonSaleObjects = jsonSalesDataReader.getSaleObjects(Optional.of(file));
                    saleObjects.addAll(jsonSaleObjects);
                    break;
                case "xml":
                    SalesDataReader xmlSalesDataReader = new XMLSalesDataReader();
                    List<SaleObject> xmlSaleObjects = xmlSalesDataReader.getSaleObjects(Optional.of(file));
                    saleObjects.addAll(xmlSaleObjects);
                    break;
                default:
                    LOGGER.warn("Invalid file extension: " + FilenameUtils.getExtension(file));
                    break;
            }
        });

        return saleObjects;
    }
}
