package censusanalyser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Map;

public class IndiaCensusAdapterTest {
    private static final String INDIA_CENSUS_CSV_FILE_PATH = "./src/test/resources/IndiaStateCensusData.csv";
    private static final String WRONG_CSV_FILE_PATH = "./resources/IndiaStateCensusData.csv";
    private static final String INDIAN_STATECODE_CSV_PATH_DUMMY = "./resources/IndiaStateCensusData.csv";

    @Test
    public void givenIndianCensusCSVFile_ReturnCorrectRecords() {
        Map<String, CensusDAO> numOfRecords = null;
        try {
            IndianCensusAdapter indianCensusAdapter = new IndianCensusAdapter();
            numOfRecords = indianCensusAdapter.loadCensusData(IndiaCensusCSV.class, INDIA_CENSUS_CSV_FILE_PATH);
            Assert.assertEquals(29, numOfRecords.size());
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenIndiaCensusData_WithWrongFile_ShouldThrowException() {
        try {
            IndianCensusAdapter indianCensusAdapter = new IndianCensusAdapter();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CensusAnalyserException.class);
            indianCensusAdapter.loadCensusData(CensusAnalyser.Country.INDIA,WRONG_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM,e.type);
        }
    }

    @Test
    public void givenIndianCencusData_whenContainInCorrectHeader() {
        try {
            IndianCensusAdapter indianCensusAdapter = new IndianCensusAdapter();
            indianCensusAdapter.loadCensusData(CensusAnalyser.Country.INDIA,INDIAN_STATECODE_CSV_PATH_DUMMY);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }

    @Test
    public void givenIndianStateCodeCSVfile_whenContainInCorrectDelimeter() {
        try {
            IndianCensusAdapter indianCensusAdapter = new IndianCensusAdapter();
            indianCensusAdapter.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.ERROR_WHILE_LOADING, e.type);
        }
    }
}
