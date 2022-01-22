package se.quedro.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SaleObjectReport {

    private int squareMeters;
    private String pricePerSquareMeter;
    private String city;
    private String street;
    private Integer floor;

    @Override
    public String toString() {
        if (floor != 0) {
            return "SaleObjectReport {" +
                    "squareMeters=" + squareMeters +
                    ", pricePerSquareMeter='" + pricePerSquareMeter + '\'' +
                    ", city='" + city + '\'' +
                    ", street='" + street + '\'' +
                    ", floor=" + floor +
                    '}';
        }

        return "SaleObjectReport {" +
                "squareMeters=" + squareMeters +
                ", pricePerSquareMeter='" + pricePerSquareMeter + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                '}';

    }
}
