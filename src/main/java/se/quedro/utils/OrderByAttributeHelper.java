package se.quedro.utils;

import se.quedro.dto.SaleObject;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class OrderByAttributeHelper {

    public static void orderByAttribute(List<SaleObject> saleObjects, PriorityOrderAttribute priorityOrderAttribute) {
        switch (priorityOrderAttribute) {
            case CITY:
                Comparator<SaleObject> comparatorByCity = Comparator.comparing(saleObject -> saleObject.getAddress().getCity(),
                        Comparator.nullsFirst(Comparator.naturalOrder()));
                saleObjects.sort(comparatorByCity);
                break;
            case SQUAREMETERS:
                Comparator<SaleObject> comparatorBySquareMeters = Comparator.comparing(saleObject -> saleObject.getSizeSqm());
                saleObjects.sort(comparatorBySquareMeters);
                break;
            case PRICEPERSQUAREMETER:
                Comparator<SaleObject> comparatorByPricePerSquareMeter = Comparator.comparing(saleObject -> saleObject.getStartingPrice()==0 ? 0 :  saleObject.getStartingPrice()/ saleObject.getSizeSqm());
                saleObjects.sort(comparatorByPricePerSquareMeter);
                break;
            default:
                break;
        }
    }
}
