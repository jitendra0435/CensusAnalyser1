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
    public CensusAnalyser() {
        censusStateMap = new HashMap<>();
        this.fieldNameComparatorMap=new HashMap();
        this.fieldNameComparatorMap.put(StateCensusColumnsName.State,Comparator.comparing(census->census.state));
        this.fieldNameComparatorMap.put(StateCensusColumnsName.Population,Comparator.comparing(census->census.population,Comparator.reverseOrder()));
        this.fieldNameComparatorMap.put(StateCensusColumnsName.DensityPerSqKm,Comparator.comparing(census->census.densityPerSqKm,Comparator.reverseOrder()));
        this.fieldNameComparatorMap.put(StateCensusColumnsName.AreaInSqKm,Comparator.comparing(census->census.areaInSqKm,Comparator.reverseOrder()));
    }
    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath))) {
            ICSVBuilder csvBuilder= CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaCensusCSV> csvIterator= csvBuilder.getCSVFileIterartor(reader, IndiaCensusCSV.class);
            Iterable<IndiaCensusCSV> csvIterable= () ->csvIterator;
            StreamSupport.stream(csvIterable.spliterator(),false).forEach(
            censusCSV ->censusStateMap.put(censusCSV.state,new CensusDAO(censusCSV)));
            return censusStateMap.size();
        } catch (IOException | CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (RuntimeException e){
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.ERROR_WHILE_LOADING);
        }
    }

    public int loadIndiaStateCodeData(String IndiaStateCodeCSV) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(IndiaStateCodeCSV));) {
            ICSVBuilder csvBuilder= CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaStateCodeCSV> stateCSVIterator = csvBuilder.getCSVFileIterartor(reader, IndiaStateCodeCSV.class);
            int count=0;
            while(stateCSVIterator.hasNext()){
                count++;
                IndiaStateCodeCSV stateCSV = stateCSVIterator.next();
                CensusDAO censusDAO=censusStateMap.get(stateCSV.stateName);
                if(censusDAO==null) continue;
                censusDAO.stateCode=stateCSV.stateCode;
            }
            return count;
        } catch (IOException | CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (RuntimeException e){
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.ERROR_WHILE_LOADING);
        }
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

    public int loadUSCensusData(String csvFilepath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilepath))) {
            ICSVBuilder csvBuilder= CSVBuilderFactory.createCSVBuilder();
            Iterator<USCensusCSV> csvIterator= csvBuilder.getCSVFileIterartor(reader, USCensusCSV.class);
            Iterable<USCensusCSV> csvIterable= () ->csvIterator;
            StreamSupport.stream(csvIterable.spliterator(),false).forEach(
                    censusCSV ->censusStateMap.put(censusCSV.state,new CensusDAO(censusCSV)));
            return censusStateMap.size();
        } catch (IOException | CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (RuntimeException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.ERROR_WHILE_LOADING);
        }
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
