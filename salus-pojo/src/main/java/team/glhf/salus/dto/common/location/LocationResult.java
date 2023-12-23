package team.glhf.salus.dto.common.location;

import lombok.Data;


@Data
public class LocationResult {
    private String info;

    private String status;

    private String infocode;

    private Regeocode regeocode;
}


