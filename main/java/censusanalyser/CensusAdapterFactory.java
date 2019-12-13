package censusanalyser;

public class CensusAdapterFactory {
    public static CensusAdapter getCensusData(CensusAnalyser.Country country) throws CensusAnalyserException {
        if (country.equals(CensusAnalyser.Country.INDIA)) {
            return  new IndianCensusAdapter();
        } else if (country.equals(CensusAnalyser.Country.USA)) {
            return  new USCensusAdapter();
        }
        throw new CensusAnalyserException("Unkonwn country", CensusAnalyserException.ExceptionType.ERROR_WHILE_LOADING);
    }
}
