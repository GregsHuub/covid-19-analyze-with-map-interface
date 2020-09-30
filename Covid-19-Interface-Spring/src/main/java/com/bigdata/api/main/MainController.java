package com.bigdata.api.main;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class MainController {

    String path = "C:\\Users\\GregHP\\Desktop\\SparkProj\\Covid-19-Interface-Spring\\src\\main\\resources\\csv\\Customer_Location_2020-03-19_top_200.csv";

    private CovidService covidService;

    public MainController(CovidService covidService) {
        this.covidService = covidService;
    }

    @GetMapping("/map")
    public String test(Model model) throws IOException {
        model.addAttribute("countryDetails",covidService.getCountryCovidDetails());
        return "map";
    }

    @GetMapping("/map_polygons")
    public String polygon(Model model) throws IOException {
        model.addAttribute("polygonDetails", covidService.getAffectedPersonDetails(path));
        return "mapPolygonOnly";
    }

}
