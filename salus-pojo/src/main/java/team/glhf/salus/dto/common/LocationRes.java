package team.glhf.salus.dto.common;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Felix
 * @since 2023/10/30
 */
@Data
@Builder
public class LocationRes implements Serializable {

    private String formattedAddress;

    private String country;

    private String province;

    private String city;

    private String cityCode;

    private String district;

    private String township;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
