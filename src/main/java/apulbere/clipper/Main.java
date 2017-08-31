package apulbere.clipper;

import apulbere.clipper.service.ClipperService;
import apulbere.clipper.service.ClipperServiceImpl;

import java.io.File;

/**
 * Created by adrian on 8/31/17.
 */
public class Main {
    public static void main(String[] args) {
        ClipperService cs = new ClipperServiceImpl();

        cs.create(new File("/home/adrian/Downloads/My_Clippings_Original_2017_08_31.txt"))
    }
}
