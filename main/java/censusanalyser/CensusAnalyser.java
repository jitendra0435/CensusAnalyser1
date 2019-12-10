package censusanalyser;
import com.google.gson.Gson;
import csvbuilder.CSVBuilderException;
import csvbuilder.CSVBuilderFactory;
import csvbuilder.ICSVBuilder;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

public class CensusAnalyser {
    List<IndiaCensusDAO> censusList=null;

    public CensusAnalyser() {
        this.censusList = new ArrayList<IndiaCensusDAO>();
    }

    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath))) {
            ICSVBuilder csvBuilder= CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaCensusCSV> censusFileIterator= csvBuilder.getCSVFileIterartor(reader, IndiaCensusCSV.class);
            while(censusFileIterator.hasNext()){
                this.censusList.add(new IndiaCensusDAO(censusFileIterator.next()));
            }
            return censusList.size();
        } catch (IOException | CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }

    public int loadIndiaStateCodeData(String IndiaStateCodeCSV) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(IndiaStateCodeCSV));) {
            ICSVBuilder csvBuilder= CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaCensusDAO> stateCSVIterator = csvBuilder.getCSVFileIterartor(reader, IndiaStateCodeCSV.class);
            return this.getCount(stateCSVIterator);
        } catch (IOException | CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (RuntimeException e){
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.ERROR_WHILE_LOADING);
        }
    }

    private <E> int getCount(Iterator<E> iterator){
        Iterable<E> csvIterable = () -> iterator;
        int numberOfEntries = (int) StreamSupport.stream(csvIterable.spliterator(), false).count();
        return numberOfEntries;
    }

    public String getStateWiseSortedCensusData() throws CensusAnalyserException {
            if(censusList==null || censusList.size()==0){
                throw new CensusAnalyserException("No census data",CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
            }
            Comparator<IndiaCensusDAO>censusComparator=Comparator.comparing(census ->census.state);
            this.sort(censusComparator);
            String sortedStateCensusData=new Gson().toJson(censusList);
            return  sortedStateCensusData;
    }

    public String getPopulationWiseSortedCensusData() throws CensusAnalyserException {
        if (censusList == null || censusList.size() == 0) {
            throw new CensusAnalyserException("No census data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<IndiaCensusDAO> censusComparator = Comparator.comparing(census -> census.population);
        this.sortDescending(censusComparator);
        String sortedStateCensusData = new Gson().toJson(censusList);
        return sortedStateCensusData;

    }

    private void sort(Comparator<IndiaCensusDAO>censusComparator){
        for(int i=0;i<censusList.size()-1;i++){
            for(int j=0;j<censusList.size()-i-1;j++){
                IndiaCensusDAO census1=censusList.get(j);
                IndiaCensusDAO census2=censusList.get(j+1);
                if(censusComparator.compare(census1,census2)>0){
                    censusList.set(j,census2);
                    censusList.set(j+1,census1);
                }
            }
        }

    }

    private void sortDescending(Comparator<IndiaCensusDAO>censusComparator){
        for(int i=0;i<censusList.size()-1;i++){
            for(int j=0;j<censusList.size()-i-1;j++){
                IndiaCensusDAO census1=censusList.get(j);
                IndiaCensusDAO census2=censusList.get(j+1);
                if(censusComparator.compare(census1,census2)<0){
                    censusList.set(j,census2);
                    censusList.set(j+1,census1);
                }
            }
        }

    }
}
