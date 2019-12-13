package censusanalyser;
import org.junit.Assert;
import org.junit.Test;
import java.util.Map;

public class USCensusAdapterTest {
    private static final String US_CENSUS_CSV_FILE_PATH = "/home/admin1/Desktop/CensusAnalyser/src/test/resources/USCensusData.csv";
    private static final String US_CENSUS_CSV_FILE_PATH_DUMMY = "/home/admin1/Desktop/CensusAnalyser/src/test/resources/USCensusDataDummy.csv" +
            ".csv";
    @Test
    public void givenUSCensusCSVFile_ReturnsCorrectRecords() {
        try {
            USCensusAdapter usCensusAdapter=new USCensusAdapter();
            Map<String, CensusDAO> numOfRecords=usCensusAdapter.loadCensusData(USCensusCSV.class,US_CENSUS_CSV_FILE_PATH);
            Assert.assertEquals(51, numOfRecords.size());
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void  givenUSCencusCSVfile_IfContainInCorrectHeaders_ThrowException(){
        try {
            USCensusAdapter usCensusAdapter = new USCensusAdapter();
            usCensusAdapter.loadCensusData(CensusAnalyser.Country.USA,US_CENSUS_CSV_FILE_PATH_DUMMY);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM,e.type);
        }
    }

    @Test
    public void givenMethod_IfInCorrectDelimetersRhrowsException(){
        try {
            USCensusAdapter usCensusAdapter = new USCensusAdapter();
            usCensusAdapter.loadCensusData(CensusAnalyser.Country.USA,US_CENSUS_CSV_FILE_PATH_DUMMY);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM,e.type);
        }

    }
}
