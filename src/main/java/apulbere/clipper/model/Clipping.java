package apulbere.clipper.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Clipping {
	private String author;
	private String title;
	private String location;
	private LocalDateTime dateTime;
	private String quote;

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public String getQuote() {
		return quote;
	}

	public void setQuote(String quote) {
		this.quote = quote;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Clipping clipping = (Clipping) o;
		return Objects.equals(author, clipping.author) &&
				Objects.equals(title, clipping.title) &&
				Objects.equals(location, clipping.location) &&
				Objects.equals(dateTime, clipping.dateTime) &&
				Objects.equals(quote, clipping.quote);
	}

	@Override
	public int hashCode() {
		return Objects.hash(author, title, location, dateTime, quote);
	}
}
