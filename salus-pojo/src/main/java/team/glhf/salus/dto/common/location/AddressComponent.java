package team.glhf.salus.dto.common.location;

import lombok.Data;

import java.util.List;

@Data
public class AddressComponent {
    private String adcode;

    private Building building;

    private List<BusinessArea> businessAreas;

    private String province;

    private String city;

    private String citycode;

    private String country;

    private String district;

    private Neighborhood neighborhood;

    private StreetNumber streetNumber;

    private String towncode;

    private String township;
}
