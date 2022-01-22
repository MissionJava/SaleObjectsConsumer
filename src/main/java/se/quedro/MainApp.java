package se.quedro;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.quedro.consumer.SaleObjectConsumer;
import se.quedro.consumer.SaleObjectConsumerService;
import se.quedro.dto.SaleObject;
import se.quedro.exceptions.FileFormatNotSupportedException;
import se.quedro.salesdatareader.SaleObjectDataHelper;
import se.quedro.service.SaleObjectReportingService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static se.quedro.utils.Constants.JSON;
import static se.quedro.utils.Constants.XML;

public class MainApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainApp.class);

    public static void main(String[] args) throws IOException {
        List<String> files = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            for (int i = 1; i <= 2; i++) {
                LOGGER.info("Please enter filepath for file " + i + " [only .xml and .json file format are supported]: ");
                String filepath = reader.readLine();

                String fileExtension = FilenameUtils.getExtension(filepath);

                if (!isSupportedFileExtension(fileExtension)) {
                    LOGGER.error("Exiting program as this file format is currently not supported: " + fileExtension);
                    throw new FileFormatNotSupportedException("The provide file format is currently not supported: " + fileExtension);
                }

                files.add(filepath);
            }
        }

        List<SaleObject> saleObjectFailedResults = new ArrayList<>();


        SaleObjectDataHelper saleObjectDataHelper = new SaleObjectDataHelper();
        List<SaleObject> saleObjects = saleObjectDataHelper.readFilesAndPopulateSaleObjectList(files);

        String orderByAttribute = "City";
        SaleObjectConsumer saleObjectConsumer = new SaleObjectConsumerService();
        SaleObjectReportingService reportingService = new SaleObjectReportingService();

        reportingService.reportSaleObjectPriorityByAttribute(saleObjectFailedResults, saleObjects, orderByAttribute, saleObjectConsumer);

        LOGGER.info("Sale objects reporting failed count: " + saleObjectFailedResults.size());
        saleObjectFailedResults.forEach(System.out::println);

    }

    private static boolean isSupportedFileExtension(String filetype) {
        return filetype.equalsIgnoreCase(XML) || filetype.equalsIgnoreCase(JSON);
    }
}