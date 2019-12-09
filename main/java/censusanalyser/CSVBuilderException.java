package censusanalyser;

public class CSVBuilderException extends Exception {
    public CSVBuilderException(String message, CensusAnalyserException.ExceptionType unableToParse) {
    }

    enum ExceptionType {
       CENSUS_FILE_PROBLEM,UNABLE_TO_PARSE
    }
    ExceptionType type;

    public CSVBuilderException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }
}
