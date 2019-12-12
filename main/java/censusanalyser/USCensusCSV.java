package censusanalyser;

import com.opencsv.bean.CsvBindByName;

public class USCensusCSV {
    @CsvBindByName(column = "State",required = true)
    public String state;

    @CsvBindByName(column ="State Id",required = true)
    public String stateId;

    @CsvBindByName(column ="Population",required = true)
    public int population;

    @CsvBindByName(column ="Population Density",required = true)
    public Double populationDensity;

    @CsvBindByName(column ="Total area",required = true)
    public Double totalArea;
}
