package censusanalyser;
import com.google.gson.Gson;
import csvbuilder.CSVBuilderException;
import csvbuilder.CSVBuilderFactory;
import csvbuilder.ICSVBuilder;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CensusAnalyser {
    Map<String, CensusDAO> censusStateMap=null;
    Map<StateCensusColumnsName,Comparator<CensusDAO>> fieldNameComparatorMap=null;
    CensusLoader loader=new CensusLoader();
    public CensusAnalyser() {
        censusStateMap = new HashMap<>();
        this.fieldNameComparatorMap=new HashMap();
        this.fieldNameComparatorMap.put(StateCensusColumnsName.State,Comparator.comparing(census->census.state));
        this.fieldNameComparatorMap.put(StateCensusColumnsName.Population,Comparator.comparing(census->census.population,Comparator.reverseOrder()));
        this.fieldNameComparatorMap.put(StateCensusColumnsName.DensityPerSqKm,Comparator.comparing(census->census.densityPerSqKm,Comparator.reverseOrder()));
        this.fieldNameComparatorMap.put(StateCensusColumnsName.AreaInSqKm,Comparator.comparing(census->census.areaInSqKm,Comparator.reverseOrder()));
    }
    public int loadIndiaCensusData(String ...csvFilePath) throws CensusAnalyserException {
            return loader.commonLoader(IndiaCensusCSV.class, censusStateMap,csvFilePath);
    }
    public int loadUSCensusData(String... csvFilepath) throws CensusAnalyserException {
            return loader.commonLoader(USCensusCSV.class,censusStateMap,csvFilepath);

    }
    public String genericSort(StateCensusColumnsName columnName) throws CensusAnalyserException {
        if (censusStateMap == null || censusStateMap.size() == 0) {
            throw new CensusAnalyserException("No census data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
            String sortedStateCensusData=null;
            List<CensusDAO> censusDAOS = censusStateMap.values().stream().collect(Collectors.toList());
            this.sort(censusDAOS, this.fieldNameComparatorMap.get(columnName));
            sortedStateCensusData = new Gson().toJson(censusDAOS);
            return sortedStateCensusData;
    }

    private void sort(List<CensusDAO> censusDAOS, Comparator<CensusDAO> censusComparator){
        for(int i=0;i<censusDAOS.size()-1;i++){
            for(int j=0;j<censusDAOS.size()-i-1;j++){
                CensusDAO census1=censusDAOS.get(j);
                CensusDAO census2=censusDAOS.get(j+1);
                if(censusComparator.compare(census1,census2)>0){
                    censusDAOS.set(j,census2);
                    censusDAOS.set(j+1,census1);
                }
            }
        }
    }
}
