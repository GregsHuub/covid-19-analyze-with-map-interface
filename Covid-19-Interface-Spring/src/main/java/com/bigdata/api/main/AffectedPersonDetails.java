package com.bigdata.api.main;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AffectedPersonDetails {

    private String timestamp;
    private String msisdn;
    private Double location_lat;
    private Double location_lon;
}
