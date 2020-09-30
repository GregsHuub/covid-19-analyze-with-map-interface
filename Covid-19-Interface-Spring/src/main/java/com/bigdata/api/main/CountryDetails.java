package com.bigdata.api.main;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CountryDetails {

    private double lat;
    private double lon;
    private String province;
    private String countryName;
    private String lastReleaseDate;
    private Integer infected;
    private Integer killed;
    private Integer recovered;

}
