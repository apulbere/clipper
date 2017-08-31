package apulbere.clipper.model;

import org.apache.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.util.regex.Pattern.compile;

public class ClippingBuilder {
	private static Logger logger = Logger.getLogger(ClippingBuilder.class);
	
	public static final Pattern KINDLE_DEFAULT_PATTERN = compile("(.+)\\((.+)\\)\\s+(?:.+?Page\\s+(\\d+))*.+?Loc\\.\\s+(\\d+-*\\d+).+?on\\s+(.+)\\s+(.+)");
	
	private Clipping clipping;
	private DateTimeFormatter dateTimeFormatter;

	public ClippingBuilder(DateTimeFormatter dateTimeFormatter) {
		this.clipping = new Clipping();
		this.dateTimeFormatter = dateTimeFormatter;
	}
	
	public ClippingBuilder fromRawData(String rawData) {
		Matcher matcher = KINDLE_DEFAULT_PATTERN.matcher(rawData);
		if(matcher.find()) {
			withTitle(matcher.group(1));
			withAuthor(matcher.group(2));
			withLocation(matcher.group(3), matcher.group(4));
			withDate(matcher.group(5));
			withQuote(matcher.group(6));
		}
		return this;
	}

	public ClippingBuilder withTitle(String title) {
		acceptValidString(clipping::setTitle, title);
		return this;
	}

	public ClippingBuilder withQuote(String quote) {
		acceptValidString(clipping::setQuote, quote);
		return this;
	}

	public ClippingBuilder withAuthor(String author) {
		acceptValidString(clipping::setAuthor, author);
		return this;
	}

	public ClippingBuilder withDate(String rawDate) {
		if(rawDate != null) {
			tryParseDate(rawDate.trim()).ifPresent(clipping::setDateTime);
		}
		return this;
	}

	public ClippingBuilder withLocation(String page, String location) {
		List<String> locationValues = new ArrayList<>(2);

		acceptValidString(locationValues::add, page, "Page %s");
		acceptValidString(locationValues::add, location, "Loc. %s");

		if(!locationValues.isEmpty()) {
			clipping.setLocation(String.join(" ", locationValues));
		}
		return this;
	}

	private Optional<LocalDateTime> tryParseDate(String rawDate) {
		Optional<LocalDateTime> dateTime = Optional.empty();
		try {
			dateTime = Optional.of(LocalDateTime.parse(rawDate, dateTimeFormatter));
		} catch (DateTimeParseException e) {
			logger.error(e.getMessage(), e);
		}
		return dateTime;
	}

	public boolean isValid() {
		return clipping.getQuote() != null || clipping.getDateTime() == null;

	}
	
	public Clipping build() {
		return clipping;
	}

	private void acceptValidString(Consumer<String> consumer, String value) {
		acceptValidString(consumer, value, null);
	}

	private void acceptValidString(Consumer<String> consumer, String value, String placeholder) {
		if(value != null) {
			String trimmedValue = value.trim();
			if(!trimmedValue.isEmpty()) {
				consumer.accept(placeholder != null ? format(placeholder, trimmedValue) : trimmedValue);
			}
		}
	}
}
