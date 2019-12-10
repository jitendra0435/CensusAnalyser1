package censusanalyser;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CensusAnalyserTest {

    private static final String INDIA_CENSUS_CSV_FILE_PATH = "./src/test/resources/IndiaStateCensusData.csv";
    private static final String WRONG_CSV_FILE_PATH = "./src/main/resources/IndiaStateCensusData.csv";
    private static final String INDIAN_STATE_CSV_PATH = "/home/admin1/Desktop/CensusAnalyser/src/test/resources/IndiaStateCode.csv";
    private static final String INDIAN_STATE_CSV_PATH_DUMMY="/home/admin1/Desktop/CensusAnalyser/src/test/resources/IndiaStateCensusDataInCorrectHeader.csv";

    @Test
    public void givenIndianCensusCSVFileReturnsCorrectRecords() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            int numOfRecords = censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
            Assert.assertEquals(29,numOfRecords);
        } catch (CensusAnalyserException e) {
        }
    }

    @Test
    public void givenIndiaCensusData_WithWrongFile_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CensusAnalyserException.class);
            censusAnalyser.loadIndiaCensusData(WRONG_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM,e.type);
        }
    }

    @Test
    public void givenIndianSateCSV_ShouldReturnExactCount(){
        CensusAnalyser censusAnalyser=new CensusAnalyser();
        try {
            int numOfStateCode = censusAnalyser.loadIndiaStateCodeData(INDIAN_STATE_CSV_PATH);
            Assert.assertEquals(37,numOfStateCode);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenIndianCensusData_WhenSortedOnState_ShouldReturnSortedResult()  {
        try {
            CensusAnalyser censusAnalyser=new CensusAnalyser();
            censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
            String sortedCensusData = censusAnalyser.getStateWiseSortedCensusData();
            IndiaCensusCSV[] censusCSV=new Gson().fromJson(sortedCensusData,IndiaCensusCSV[].class);
            Assert.assertEquals("Andhra Pradesh",censusCSV[0].state);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenIndianCencusData_whenContainInCorrectDelimeter(){
        try {
            CensusAnalyser censusAnalyser=new CensusAnalyser();
            censusAnalyser.loadIndiaStateCodeData(INDIAN_STATE_CSV_PATH_DUMMY);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.ERROR_WHILE_LOADING,e.type);
        }
    }

    @Test
    public void givenIndianCencusData_whenContainInCorrectHeader(){
        try {
            CensusAnalyser censusAnalyser=new CensusAnalyser();
            censusAnalyser.loadIndiaStateCodeData(INDIAN_STATE_CSV_PATH_DUMMY);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.ERROR_WHILE_LOADING,e.type);
        }
    }

    @Test
    public void givenIndianCencusData_whenSortedOnPopulation_ShouldReturnSortedResult(){
        try {
            CensusAnalyser censusAnalyser=new CensusAnalyser();
            censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
            String sortedCensusData=censusAnalyser.getPopulationWiseSortedCensusData();
            IndiaCensusCSV[] censusCSV=new Gson().fromJson(sortedCensusData,IndiaCensusCSV[].class);
            Assert.assertEquals(199812341,censusCSV[0].population);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        }
    }
}
