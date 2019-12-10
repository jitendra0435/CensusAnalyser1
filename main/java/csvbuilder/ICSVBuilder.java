package csvbuilder;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;

public interface ICSVBuilder<E> {
    public Iterator<E> getCSVFileIterartor(Reader reader, Class csvClass) throws CSVBuilderException;
    public List<E>getCSVFileList(Reader reader, Class csvClass) throws CSVBuilderException;
}
