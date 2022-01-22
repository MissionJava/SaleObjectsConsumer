package se.quedro.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.quedro.dto.SaleObjectReport;
import se.quedro.exceptions.TechnicalException;
import se.quedro.utils.PriorityOrderAttribute;

public class SaleObjectConsumerService implements SaleObjectConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SaleObjectConsumerService.class);
    private volatile boolean isTransactionCommitted;

    @Override
    public PriorityOrderAttribute getPriorityOrderAttribute(String priorityOrderAttribute) {
        switch (priorityOrderAttribute) {
            case "City":
                return PriorityOrderAttribute.CITY;
            case "SquareMeters":
                return PriorityOrderAttribute.SQUAREMETERS;
            case "PricePerSquareMeter":
                return PriorityOrderAttribute.PRICEPERSQUAREMETER;
            default:
                LOGGER.info("Invalid priorityOrderAttribute");
                return PriorityOrderAttribute.NONE;
        }
    }

    @Override
    public void startSaleObjectTransaction() {
        this.isTransactionCommitted = false;
    }

    @Override
    public void reportSaleObject(int squareMeters, String pricePerSquareMeter, String city, String street, Integer floor) throws TechnicalException {

        SaleObjectReport saleObjectReport = new SaleObjectReport();
        saleObjectReport.setSquareMeters(squareMeters);
        saleObjectReport.setPricePerSquareMeter(pricePerSquareMeter);
        saleObjectReport.setCity(city);
        saleObjectReport.setStreet(street);
        saleObjectReport.setFloor(floor);

        LOGGER.info(saleObjectReport.toString());
    }

    @Override
    public void commitSaleObjectTransaction() {
        this.isTransactionCommitted = true;
    }

    public boolean isTransactionCommitted() {
        return isTransactionCommitted;
    }
}
