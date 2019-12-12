package censusanalyser;
import csvbuilder.CSVBuilderException;
import csvbuilder.CSVBuilderFactory;
import csvbuilder.ICSVBuilder;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.StreamSupport;

public class CensusLoader {
    public <E> int commonLoader(String csvFilepath, Class<E> censusClass, Map<String, CensusDAO> censusStateMap) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilepath))) {
            ICSVBuilder csvBuilder= CSVBuilderFactory.createCSVBuilder();
            Iterator<E> csvIterator= csvBuilder.getCSVFileIterartor(reader,censusClass);
            Iterable<E> csvIterable= () ->csvIterator;
            if(censusClass.getName().equals("censusanalyser.IndiaCensusCSV")) {
                StreamSupport.stream(csvIterable.spliterator(), false)
                        .map(IndiaCensusCSV.class::cast)
                        .forEach(censusCSV -> censusStateMap.put(censusCSV.state, new CensusDAO(censusCSV)));
            }else if(censusClass.getName().equals("censusanalyser.USCensusCSV"))
                StreamSupport.stream(csvIterable.spliterator(), false)
                        .map(USCensusCSV.class::cast)
                        .forEach(censusCSV -> censusStateMap.put(censusCSV.state, new CensusDAO(censusCSV)));
            return censusStateMap.size();
        } catch (IOException | CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (RuntimeException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.ERROR_WHILE_LOADING);
        }
    }
}
