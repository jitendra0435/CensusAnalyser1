package censusanalyser;
public class CensusDAO {
    public String state;
    public int population;
    public double areaInSqKm;
    public double densityPerSqKm;
    public String stateCode;

    public CensusDAO(IndiaCensusCSV indiaCensusCSV) {
        this.state = indiaCensusCSV.state;
        this.population = indiaCensusCSV.population;
        this.areaInSqKm = indiaCensusCSV.areaInSqKm;
        this.densityPerSqKm = indiaCensusCSV.densityPerSqKm;
    }
    public CensusDAO(USCensusCSV usCensusCSV) {
        this.state = usCensusCSV.state;
        this.population = usCensusCSV.population;
        this.areaInSqKm = usCensusCSV.totalArea;
        this.densityPerSqKm = usCensusCSV.populationDensity;

    }

    public Object getDTO(CensusAnalyser.Country country) {
        if (country.equals(CensusAnalyser.Country.INDIA))
            return new IndiaCensusCSV(state,population,(int)areaInSqKm,(int)densityPerSqKm);
        return new USCensusCSV(state,population,(int) areaInSqKm,(int) densityPerSqKm);
    }
}
