package censusanalyser;
import org.junit.Assert;
import org.junit.Test;
import java.util.Map;

public class IndiaCensusAdapterTest {
    private static final String INDIA_CENSUS_CSV_FILE_PATH = "./src/test/resources/IndiaStateCensusData.csv";

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
}
