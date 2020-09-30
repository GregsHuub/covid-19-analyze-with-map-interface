package com.bigdata.api.main;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CovidService {
    public List<CountryDetails> getCountryCovidDetails() throws IOException {
        AtomicInteger sumOfUs = new AtomicInteger();
        //autodetect header splited by comma
        List<CountryDetails> data = new ArrayList<>();
        Reader in = new FileReader("src/main/resources/csv/03-24-2020.csv");
        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withHeader(Headers.class)
                .withFirstRecordAsHeader()
                .parse(in);
        records.forEach(row -> data.add(new CountryDetails(
                Double.parseDouble(row.get(Headers.Lat)),
                Double.parseDouble(row.get(Headers.Long_)),
                row.get(Headers.Province_State),
                row.get(Headers.Country_Region),
                row.get(Headers.Last_Update),
                Integer.parseInt(row.get(Headers.Confirmed)),
                Integer.parseInt(row.get(Headers.Deaths)),
                Integer.parseInt(row.get(Headers.Recovered))
        )));
        return data;
    }
    public List<AffectedPersonDetails> getAffectedPersonDetails(String filePath) throws IOException {
        List<AffectedPersonDetails> data = new ArrayList<>();
        Reader in = new FileReader(filePath);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withHeader(Headers.class)
                .withFirstRecordAsHeader()
                .withDelimiter(';')
                .parse(in);
        records.forEach(row -> data.add(new AffectedPersonDetails(
                (row.get(0)),
                (row.get(1)),
                Double.parseDouble(row.get(2)),
                Double.parseDouble(row.get(3))
        )));
        return data;
    }
}
