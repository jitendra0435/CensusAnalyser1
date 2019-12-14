package censusanalyser;
import com.google.gson.Gson;
import java.util.*;
import java.util.stream.Collectors;
public class CensusAnalyser{
    public enum Country {INDIA,USA};
    private Country country;
    Map<String, CensusDAO> censusStateMap=null;
    Map<StateCensusColumnsName,Comparator<CensusDAO>> fieldNameComparatorMap=null;
    public CensusAnalyser(Country country) {
        this.country=country;
        censusStateMap = new HashMap<>();
        this.fieldNameComparatorMap=new HashMap();
        this.fieldNameComparatorMap.put(StateCensusColumnsName.STATE,Comparator.comparing(census->census.state));
        this.fieldNameComparatorMap.put(StateCensusColumnsName.POPULATION,Comparator.comparing(census->census.population,Comparator.reverseOrder()));
        this.fieldNameComparatorMap.put(StateCensusColumnsName.DENSITY_PER_SQ_KM,Comparator.comparing(census->census.densityPerSqKm,Comparator.reverseOrder()));
        this.fieldNameComparatorMap.put(StateCensusColumnsName.AREAIN_SQKM,Comparator.comparing(census->census.areaInSqKm,Comparator.reverseOrder()));
        Comparator<CensusDAO> populationComparator = Comparator.comparing(census -> census.population);
        Comparator<CensusDAO> densityComparator = Comparator.comparing(census -> census.densityPerSqKm);
        Comparator<CensusDAO> populusStateWithDensityComparator = populationComparator.thenComparing(densityComparator);
        this.fieldNameComparatorMap.put(StateCensusColumnsName.POPULUSSTATEWITH_DENSITY, populusStateWithDensityComparator);
    }

    public Map<String, CensusDAO> loadCensusData(Country country, String... csvFilePath) throws CensusAnalyserException {
        CensusAdapter censusAdapter = CensusAdapterFactory.getCensusData(country);
        censusStateMap = censusAdapter.loadCensusData(country, csvFilePath);
        return censusStateMap;
    }
    public String genericSort(StateCensusColumnsName sortByColunnName) throws CensusAnalyserException {
        if (censusStateMap == null || censusStateMap.size() == 0) {
            throw new CensusAnalyserException("No census data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        ArrayList censusDTO = censusStateMap.values().stream()
                .sorted(this.fieldNameComparatorMap.get(sortByColunnName))
                .map(censusDAO -> censusDAO.getDTO(country))
                .collect(Collectors.toCollection(ArrayList::new));
        return new Gson().toJson(censusDTO);
    }
}
