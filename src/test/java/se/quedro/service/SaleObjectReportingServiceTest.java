package se.quedro.service;


import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;
import se.quedro.consumer.SaleObjectConsumer;
import se.quedro.consumer.SaleObjectConsumerService;
import se.quedro.dto.Address;
import se.quedro.dto.SaleObject;
import se.quedro.exceptions.TechnicalException;
import se.quedro.utils.PriorityOrderAttribute;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

public class SaleObjectReportingServiceTest {


    public static List<SaleObject> getSaleObjectList() {
        List<SaleObject> saleObjects = new ArrayList<>();

        SaleObject saleObject = new SaleObject();
        Address address = new Address();
        address.setCity("Test2");
        address.setStreet("street 9");
        address.setFloor(11);
        saleObject.setType("apt");


        saleObject.setId(5);
        saleObject.setSizeSqm(403);
        saleObject.setStartingPrice(6895857);
        saleObject.setAddress(address);
        saleObjects.add(saleObject);

        SaleObject saleObject1 = new SaleObject();
        Address address1 = new Address();
        address1.setCity("Test1");
        address1.setStreet("street 5");
        saleObject1.setType("house");


        saleObject1.setId(9);
        saleObject1.setSizeSqm(123);
        saleObject1.setStartingPrice(12344);
        saleObject1.setAddress(address1);
        saleObjects.add(saleObject1);

        return saleObjects;

    }

    @Test
    public void testReportSaleObjectWithoutMatchingPriorityByAttribute_shouldSucceedReportForAllSaleObjects() {
        List<SaleObject> saleObjectFailedResults = new ArrayList<>();
        List<SaleObject> actualSaleObjects = getSaleObjectList();
        List<SaleObject> expectedSaleObjects = getSaleObjectList();
        String orderByAttribute = "Street";

        SaleObjectConsumer saleObjectConsumer = mock(SaleObjectConsumerService.class);
        when(saleObjectConsumer.getPriorityOrderAttribute(anyString())).thenReturn(PriorityOrderAttribute.NONE);
        SaleObjectReportingService reportingService = new SaleObjectReportingService();
        reportingService.reportSaleObjectPriorityByAttribute(saleObjectFailedResults, actualSaleObjects, orderByAttribute, saleObjectConsumer);

        Assert.assertEquals(actualSaleObjects.get(0).getType(), expectedSaleObjects.get(0).getType());
        Assert.assertEquals(actualSaleObjects.get(0).getStartingPrice(), expectedSaleObjects.get(0).getStartingPrice());
        Assert.assertEquals(saleObjectFailedResults.size(), 0);

        verify(saleObjectConsumer, times(1)).getPriorityOrderAttribute(orderByAttribute);
        verify(saleObjectConsumer, times(2)).reportSaleObject(anyInt(), anyString(), anyString(), anyString(), any(Integer.class));

    }

    @Test
    public void testReportSaleObjectPriorityByAttribute_shouldFailReportForAllSaleObjects_whenSaleObjectConsumer_isNull() {
        List<SaleObject> saleObjectFailedResults = new ArrayList<>();
        List<SaleObject> actualSaleObjects = getSaleObjectList();
        List<SaleObject> expectedSaleObjects = getSaleObjectList();
        Comparator<SaleObject> comparatorByCity = Comparator.comparing(saleObject -> saleObject.getAddress().getCity(),
                Comparator.nullsFirst(Comparator.naturalOrder()));
        expectedSaleObjects.sort(comparatorByCity);
        String orderByAttribute = "PricePerSquareMeter";

        SaleObjectReportingService reportingService = new SaleObjectReportingService();
        reportingService.reportSaleObjectPriorityByAttribute(saleObjectFailedResults, actualSaleObjects, orderByAttribute, null);

        Assert.assertEquals(saleObjectFailedResults.size(), 2);
    }

    @Test
    public void testReportSaleObjectPriorityByAttributeCity_shouldSucceedReportForAllSaleObjects() {
        List<SaleObject> saleObjectFailedResults = new ArrayList<>();
        List<SaleObject> actualSaleObjects = getSaleObjectList();
        List<SaleObject> expectedSaleObjects = getSaleObjectList();
        Comparator<SaleObject> comparatorByCity = Comparator.comparing(saleObject -> saleObject.getAddress().getCity(),
                Comparator.nullsFirst(Comparator.naturalOrder()));
        expectedSaleObjects.sort(comparatorByCity);
        String orderByAttribute = "City";

        SaleObjectConsumer saleObjectConsumer = mock(SaleObjectConsumerService.class);
        when(saleObjectConsumer.getPriorityOrderAttribute(anyString())).thenReturn(PriorityOrderAttribute.CITY);
        SaleObjectReportingService reportingService = new SaleObjectReportingService();
        reportingService.reportSaleObjectPriorityByAttribute(saleObjectFailedResults, actualSaleObjects, orderByAttribute, saleObjectConsumer);

        Assert.assertEquals(actualSaleObjects.size(), expectedSaleObjects.size());
        Assert.assertEquals(actualSaleObjects.get(0).getType(), expectedSaleObjects.get(0).getType());
        Assert.assertEquals(saleObjectFailedResults.size(), 0);

        verify(saleObjectConsumer, times(1)).getPriorityOrderAttribute(orderByAttribute);
        verify(saleObjectConsumer, times(2)).reportSaleObject(anyInt(), anyString(), anyString(), anyString(), any(Integer.class));

    }

    @Test
    public void testReportSaleObjectPriorityByAttributeCity_shouldFailReportForAllSaleObjects() {
        List<SaleObject> saleObjectFailedResults = new ArrayList<>();
        List<SaleObject> actualSaleObjects = getSaleObjectList();
        List<SaleObject> expectedSaleObjects = getSaleObjectList();
        Comparator<SaleObject> comparatorByCity = Comparator.comparing(saleObject -> saleObject.getAddress().getCity(),
                Comparator.nullsFirst(Comparator.naturalOrder()));
        expectedSaleObjects.sort(comparatorByCity);
        String orderByAttribute = "City";

        SaleObjectConsumer saleObjectConsumer = mock(SaleObjectConsumerService.class);
        when(saleObjectConsumer.getPriorityOrderAttribute(anyString())).thenReturn(PriorityOrderAttribute.CITY);

        TechnicalException technicalException = new TechnicalException("some technical issue occurred");

        Mockito.doThrow(technicalException).when(saleObjectConsumer).reportSaleObject(anyInt(), anyString(), anyString(), anyString(), any(Integer.class));
        SaleObjectReportingService reportingService = new SaleObjectReportingService();
        reportingService.reportSaleObjectPriorityByAttribute(saleObjectFailedResults, actualSaleObjects, orderByAttribute, saleObjectConsumer);

        Assert.assertEquals(saleObjectFailedResults.size(), 2);

        verify(saleObjectConsumer, times(1)).getPriorityOrderAttribute(orderByAttribute);
        verify(saleObjectConsumer, times(2)).reportSaleObject(anyInt(), anyString(), anyString(), anyString(), any(Integer.class));

    }

    @Test
    public void testReportSaleObjectPriorityByAttributeCity_shouldFailReportForSaleObjects_withCityNull() {
        List<SaleObject> saleObjectFailedResults = new ArrayList<>();
        List<SaleObject> actualSaleObjects = getSaleObjectList();
        actualSaleObjects.get(0).setAddress(null);
        List<SaleObject> expectedSaleObjects = getSaleObjectList();
        Comparator<SaleObject> comparatorByCity = Comparator.comparing(saleObject -> saleObject.getAddress().getCity(),
                Comparator.nullsFirst(Comparator.naturalOrder()));
        expectedSaleObjects.sort(comparatorByCity);
        String orderByAttribute = "City";

        SaleObjectConsumer saleObjectConsumer = mock(SaleObjectConsumerService.class);
        when(saleObjectConsumer.getPriorityOrderAttribute(anyString())).thenReturn(PriorityOrderAttribute.CITY);

        /*TechnicalException technicalException = new TechnicalException("some technical issue occurred");

        Mockito.doThrow(technicalException).when(saleObjectConsumer).reportSaleObject(anyInt(), anyString(), anyString(), anyString(), any(Integer.class));
        */
        SaleObjectReportingService reportingService = new SaleObjectReportingService();
        reportingService.reportSaleObjectPriorityByAttribute(saleObjectFailedResults, actualSaleObjects, orderByAttribute, saleObjectConsumer);

        Assert.assertEquals(saleObjectFailedResults.size(), 2);

        verify(saleObjectConsumer, times(1)).getPriorityOrderAttribute(orderByAttribute);
        verify(saleObjectConsumer, times(2)).reportSaleObject(anyInt(), anyString(), anyString(), anyString(), any(Integer.class));

    }

    @Test
    public void testReportSaleObjectPriorityByAttributePricePerSquareMeter_shouldSucceedReportForAllSaleObjects() {
        List<SaleObject> saleObjectFailedResults = new ArrayList<>();
        List<SaleObject> actualSaleObjects = getSaleObjectList();
        List<SaleObject> expectedSaleObjects = getSaleObjectList();
        Comparator<SaleObject> comparatorByCity = Comparator.comparing(saleObject -> saleObject.getStartingPrice() == 0 ? 0 : saleObject.getStartingPrice() / saleObject.getSizeSqm());
        expectedSaleObjects.sort(comparatorByCity);
        String orderByAttribute = "PricePerSquareMeter";

        SaleObjectConsumerService saleObjectConsumer = mock(SaleObjectConsumerService.class);
        when(saleObjectConsumer.getPriorityOrderAttribute(anyString())).thenReturn(PriorityOrderAttribute.PRICEPERSQUAREMETER);

        SaleObjectReportingService reportingService = new SaleObjectReportingService();
        reportingService.reportSaleObjectPriorityByAttribute(saleObjectFailedResults, actualSaleObjects, orderByAttribute, saleObjectConsumer);

        Assert.assertEquals(saleObjectFailedResults.size(), 0);

        verify(saleObjectConsumer, times(1)).getPriorityOrderAttribute(orderByAttribute);
        verify(saleObjectConsumer, times(2)).reportSaleObject(anyInt(), anyString(), anyString(), anyString(), any(Integer.class));

    }

    @Test
    public void testReportSaleObjectPriorityByAttributePricePerSquareMeter_shouldFailReportForAllSaleObjects() {
        List<SaleObject> saleObjectFailedResults = new ArrayList<>();
        List<SaleObject> actualSaleObjects = getSaleObjectList();
        List<SaleObject> expectedSaleObjects = getSaleObjectList();
        Comparator<SaleObject> comparatorByCity = Comparator.comparing(saleObject -> saleObject.getAddress().getCity(),
                Comparator.nullsFirst(Comparator.naturalOrder()));
        expectedSaleObjects.sort(comparatorByCity);
        String orderByAttribute = "PricePerSquareMeter";

        SaleObjectConsumer saleObjectConsumer = mock(SaleObjectConsumerService.class);
        when(saleObjectConsumer.getPriorityOrderAttribute(anyString())).thenReturn(PriorityOrderAttribute.PRICEPERSQUAREMETER);

        TechnicalException technicalException = new TechnicalException("some technical issue occurred");

        Mockito.doThrow(technicalException).when(saleObjectConsumer).reportSaleObject(anyInt(), anyString(), anyString(), anyString(), any(Integer.class));
        SaleObjectReportingService reportingService = new SaleObjectReportingService();
        reportingService.reportSaleObjectPriorityByAttribute(saleObjectFailedResults, actualSaleObjects, orderByAttribute, saleObjectConsumer);

        Assert.assertEquals(saleObjectFailedResults.size(), 2);

        Assert.assertEquals(saleObjectConsumer.isTransactionCommitted(), false);

        verify(saleObjectConsumer, times(1)).getPriorityOrderAttribute(orderByAttribute);
        verify(saleObjectConsumer, times(2)).reportSaleObject(anyInt(), anyString(), anyString(), anyString(), any(Integer.class));

    }

}
