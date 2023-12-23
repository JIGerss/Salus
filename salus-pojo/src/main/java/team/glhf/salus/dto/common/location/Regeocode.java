package team.glhf.salus.dto.common.location;

import lombok.Data;

@Data
public class Regeocode {
    private AddressComponent addressComponent;

    private String formattedAddress;
}
