package censusanalyser;

import java.io.Reader;
import java.util.Iterator;

public interface ICSVBuilder<E> {
    public Iterator<E> getCSVFileIterartor(Reader reader, Class csvClass) throws CSVBuilderException;
}