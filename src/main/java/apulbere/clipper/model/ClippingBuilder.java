package apulbere.clipper.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;

public class ClippingBuilder {
	private static Logger logger = Logger.getLogger(ClippingBuilder.class);
	private Clipping clipping;
	private List<String> formats;

	public ClippingBuilder(List<String> formats) {
		this.clipping = new Clipping();
		this.formats = formats;
	}

	public ClippingBuilder setTitle(String title) {
		clipping.setTitle(title.trim());
		return this;
	}

	public ClippingBuilder setQuote(String quote) {
		clipping.setQuote(quote.trim());
		return this;
	}

	public ClippingBuilder setAuthor(String author) {
		clipping.setAuthor(author.trim());
		return this;
	}

	public ClippingBuilder setDate(String rawDate) {
		Optional<Date> obj = tryParseDate(rawDate.trim());
		if(obj.isPresent()) {
			clipping.setDate(obj.get());
		}
		return this;
	}

	public ClippingBuilder setLocation(Optional<String> page, Optional<String> location) {
		StringBuilder builder = new StringBuilder();
		if(page.isPresent()) {
			builder.append("Page ").append(page.get()).append(" ");
		}
		if(location.isPresent()) {
			builder.append("Loc. ").append(location.get());
		}
		String result = builder.toString().trim();
		if(result.length() != 0) {
			clipping.setLocation(result);
		}
		return this;
	}

	private Optional<Date> tryParseDate(String rawDate) {
		for (String format: formats) {
			try {
				return Optional.of(new SimpleDateFormat(format).parse(rawDate));
			} catch (ParseException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return Optional.empty();
	}
	
	public Optional<Clipping> build() {
		return clipping.getQuote() == null || clipping.getDate() == null ? Optional.empty() : Optional.of(clipping);
	}
}
