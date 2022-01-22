package se.quedro.salesdatareader;

import se.quedro.dto.SaleObject;

import java.util.List;
import java.util.Optional;

@FunctionalInterface
public interface SalesDataReader {
    List<SaleObject> getSaleObjects(Optional<String> filename);
}
