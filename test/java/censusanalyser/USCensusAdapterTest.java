package censusanalyser;
import org.junit.Assert;
import org.junit.Test;
import java.util.Map;

public class USCensusAdapterTest {
    private static final String US_CENSUS_CSV_FILE_PATH = "/home/admin1/Desktop/CensusAnalyser/src/test/resources/USCensusData.csv";

    @Test
    public void givenUSCensusCSVFileReturnsCorrectRecords() {
        try {
            USCensusAdapter usCensusAdapter=new USCensusAdapter();
            Map<String, CensusDAO> numOfRecords=usCensusAdapter.loadCensusData(USCensusCSV.class,US_CENSUS_CSV_FILE_PATH);
            Assert.assertEquals(51, numOfRecords.size());
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        }
    }


}
