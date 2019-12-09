package censusanalyser;
import com.google.gson.Gson;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;
public class CensusAnalyser {
    List<IndiaCensusCSV> censusCSVList=null;
    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder csvBuilder= CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaCensusCSV> censusCSVIterator = csvBuilder.getCSVFileIterartor(reader, IndiaCensusCSV.class);
            return this.getCount(censusCSVIterator);
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),e.type.name());
        }
    }

    public int loadIndiaStateCodeData(String IndiaStateCodeCSV) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(IndiaStateCodeCSV));) {
            ICSVBuilder csvBuilder= CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaStateCodeCsv> stateCSVIterator = csvBuilder.getCSVFileIterartor(reader, IndiaStateCodeCsv.class);
            return this.getCount(stateCSVIterator);
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),e.type.name());
        }

    }

    private <E> int getCount(Iterator<E> iterator){
        Iterable<E> csvIterable = () -> iterator;
        int numberOfEntries = (int) StreamSupport.stream(csvIterable.spliterator(), false).count();
        return numberOfEntries;
    }

    public String getStateWiseSortedCensusData(String csvFilePath) throws CensusAnalyserException {
            if(censusCSVList==null || censusCSVList.size()==0){
                throw new CensusAnalyserException("No census data",CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
            }
            Comparator<IndiaCensusCSV>censusComparator=Comparator.comparing(census ->census.state);
            this.sort(censusComparator);
            String sortedStateCensusData=new Gson().toJson(censusCSVList);
            return  sortedStateCensusData;
    }

    private void sort(Comparator<IndiaCensusCSV>censusComparator){
        for(int i=0;i<censusCSVList.size()-1;i++){
            for(int j=0;j<censusCSVList.size()-i-1;j++){
                IndiaCensusCSV census1=censusCSVList.get(j);
                IndiaCensusCSV census2=censusCSVList.get(j+1);
                if(censusComparator.compare(census1,census2)>0){
                    censusCSVList.set(j,census2);
                    censusCSVList.set(j+1,census1);
                }
            }
        }

    }
}
