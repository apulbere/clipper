package apulbere.clipper;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Before;
import org.junit.Test;

import apulbere.clipper.model.Clipping;

public class ClipperTest {
	private Clipper clipper;
	private Clipping clipping;
	private String rawData;
	
	@Before
	public void init() throws ParseException {
		String rawDate = "Sunday, July 28, 2013, 10:06 PM";
		clipper = new Clipper();
		clipping = new Clipping();
		clipping.setDate(new SimpleDateFormat("EEEE, MMMM d, y, h:mm a").parse(rawDate));
		clipping.setAuthor("Mauris sollicitudin");
		clipping.setTitle("Nam justo augue");
		clipping.setLocation("Page 96 Loc. 1460-62");
		clipping.setQuote("In maximus ornare odio, eu tincidunt lacus ullamcorper suscipit.");
		
		rawData = String.format("%s (%s) - %s | Added on %s\n%s", clipping.getTitle(), clipping.getAuthor(), clipping.getLocation(), rawDate, clipping.getQuote());
	}
	
	@Test
	public void create() {
		Clipping createdClipping = clipper.create(rawData).get();
		
		assertThat(createdClipping.getAuthor(), is(clipping.getAuthor()));
		assertThat(createdClipping.getTitle(), is(clipping.getTitle()));
		assertThat(createdClipping.getLocation(), is(clipping.getLocation()));
		assertThat(createdClipping.getQuote(), is(clipping.getQuote()));
	}
}

