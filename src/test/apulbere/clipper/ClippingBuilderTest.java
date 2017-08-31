package apulbere.clipper;

import static apulbere.clipper.model.KindleConstants.OUTPUT_FORMAT;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;

import apulbere.clipper.model.ClippingBuilder;
import apulbere.clipper.model.KindleConstants;
import apulbere.clipper.service.ClipperService;
import org.junit.Before;
import org.junit.Test;

import apulbere.clipper.model.Clipping;

public class ClippingBuilderTest {
	private Clipping clipping;
	private String rawData;
	private DateTimeFormatter dateTimeFormatter;
	
	public void prepareSingleClippingObject() {
		dateTimeFormatter = DateTimeFormatter.ofPattern("[EEEE, MMMM d, y, h:mm a]");

		String rawDate = "Sunday, July 28, 2013, 10:06 PM";
		clipping = new Clipping();
		clipping.setDateTime(LocalDateTime.parse(rawDate, dateTimeFormatter));
		clipping.setAuthor("Mauris sollicitudin");
		clipping.setTitle("Nam justo augue");
		clipping.setLocation("Page 96 Loc. 1460-62");
		clipping.setQuote("In maximus ornare odio, eu tincidunt lacus ullamcorper suscipit.");
		
		rawData = String.format(OUTPUT_FORMAT,
				clipping.getTitle(),
				clipping.getAuthor(),
				clipping.getLocation(),
				rawDate,
				clipping.getQuote(),
				"=");
	}
	
	@Test
	public void singleClippingObject() {
		prepareSingleClippingObject();

		ClippingBuilder clippingBuilder = new ClippingBuilder(dateTimeFormatter);
		clippingBuilder.fromRawData(rawData);
		Clipping createdClipping = clippingBuilder.build();
		
		assertThat(createdClipping.getAuthor(), is(clipping.getAuthor()));
		assertThat(createdClipping.getTitle(), is(clipping.getTitle()));
		assertThat(createdClipping.getLocation(), is(clipping.getLocation()));
		assertThat(createdClipping.getQuote(), is(clipping.getQuote()));
	}

	@Test
	public void multipleDateTimeFormats1() {
		dateTimeFormatter = DateTimeFormatter.ofPattern("[EEEE, MMMM d, y, h:mm a][EEEE, d MMMM yy HH:mm:ss z]");

		String rawDate1 = "Sunday, July 28, 2013, 10:06 PM";
		String rawDate2 = "Sunday, 5 June 16 15:26:13 GMT+03:01";

		LocalDateTime dateTime1 = LocalDateTime.parse(rawDate1, dateTimeFormatter);
		assertThat(dateTime1.getYear(), is(2013));

		LocalDateTime dateTime2 = LocalDateTime.parse(rawDate2, dateTimeFormatter);
		assertThat(dateTime2.getYear(), is(2016));
	}
}

