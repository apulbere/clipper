package apulbere.clipper.service;

import apulbere.clipper.model.Clipping;

import java.io.File;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by adrian on 8/31/17.
 */
public interface ClippingService {

    List<Clipping> create(File file);
    List<Clipping> create(File file, String clippingSeparator);
    List<Clipping> create(File file, String clippingSeparator, DateTimeFormatter dateTimeFormatter);

    void writeToFile(List<Clipping> clippings, Path path);


    /**
     * @param clippings
     * @param path
     * @param dateTimeFormatter
     * @param separator
     */
    void writeToFile(List<Clipping> clippings,
                     Path path,
                     DateTimeFormatter dateTimeFormatter,
                     String separator);
}
