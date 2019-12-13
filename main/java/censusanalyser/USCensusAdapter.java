package censusanalyser;
import java.util.Map;

public class USCensusAdapter extends CensusAdapter {
    @Override
    public Map<String, CensusDAO> loadCensusData(CensusAnalyser.Country country, String... csvFilePath) throws CensusAnalyserException {
        return loadCensusData(USCensusCSV.class,csvFilePath);
    }
}
