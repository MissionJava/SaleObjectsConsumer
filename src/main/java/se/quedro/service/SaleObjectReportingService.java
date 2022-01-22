package se.quedro.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.quedro.consumer.SaleObjectConsumer;
import se.quedro.dto.Address;
import se.quedro.dto.SaleObject;
import se.quedro.exceptions.TechnicalException;
import se.quedro.utils.OrderByAttributeHelper;
import se.quedro.utils.PriorityOrderAttribute;

import java.util.List;
import java.util.Objects;

public class SaleObjectReportingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SaleObjectReportingService.class);
    private SaleObjectConsumer saleObjectConsumer;

    public SaleObjectReportingService() {
    }

    public void reportSaleObjectPriorityByAttribute(List<SaleObject> saleObjectFailedResults, List<SaleObject> saleObjects, String orderByAttribute, SaleObjectConsumer saleObjectConsumer) {
        if (Objects.nonNull(saleObjectConsumer)) {
            PriorityOrderAttribute priorityOrderAttribute = saleObjectConsumer.getPriorityOrderAttribute(orderByAttribute);

            try {
                OrderByAttributeHelper.orderByAttribute(saleObjects, priorityOrderAttribute);
            } catch (Exception e) {
                LOGGER.error("Exception occurred while reporting sale object: ", e);
                saleObjectFailedResults.addAll(saleObjects);
            }

            saleObjectConsumer.startSaleObjectTransaction();

            for (int i = 0; i < saleObjects.size(); i++) {
                if (saleObjectConsumer.isTransactionCommitted()) {
                    saleObjectConsumer.startSaleObjectTransaction();
                }

                reportSaleObject(saleObjectFailedResults, saleObjects, saleObjectConsumer, i);

                if (i == saleObjects.size() - 1) {
                    saleObjectConsumer.commitSaleObjectTransaction();
                }
            }
        } else {

            saleObjectFailedResults.addAll(saleObjects);
        }
    }

    private void reportSaleObject(List<SaleObject> saleObjectFailedResults, List<SaleObject> saleObjects, SaleObjectConsumer saleObjectConsumer, int i) {

        SaleObject saleObject = saleObjects.get(i);

        int squareMeters = saleObject.getSizeSqm();
        Address address = saleObject.getAddress();
        String city = address != null ? address.getCity() : "";
        String street = address != null ? address.getStreet() : "";
        Integer floor = address != null ? address.getFloor() : 0;

        String pricePerSquareMeter = "0";
        if (saleObjects.get(i).getStartingPrice() > 0) {
            pricePerSquareMeter = Long.toString(saleObjects.get(i).getStartingPrice() / saleObjects.get(i).getSizeSqm());
        }

        try {
            saleObjectConsumer.reportSaleObject(squareMeters, pricePerSquareMeter, city, street, floor);
        } catch (TechnicalException e) {
            LOGGER.error("Exception occurred while reporting sale object: ", e);
            saleObjectFailedResults.add(saleObject);
        }
    }
}
