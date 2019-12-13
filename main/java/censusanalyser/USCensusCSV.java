package censusanalyser;

import com.opencsv.bean.CsvBindByName;

public class USCensusCSV {
    public USCensusCSV() {
    }

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

    public USCensusCSV(String state, int population, double areaInSqKm, double densityPerSqKm) {
        this.state=state;
        this.population=population;
        this.populationDensity=areaInSqKm;
        this.totalArea=densityPerSqKm;
    }
}
