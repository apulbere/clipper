package apulbere.clipper.model;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * Created by adrian on 8/31/17.
 */
public final class KindleConstants {

    public static final Pattern INPUT_PATTERN =
        compile("(.+)((.+))s+(?:.+?Pages+(d+))*.+?Loc.s+(d+-*d+).+?ons+(.+)s+(.+)");

    public static final String OUTPUT_FORMAT = "%s (%s)\n- Note %s | Added on %s\n\n%s\n%s";
}
