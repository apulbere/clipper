package apulbere.clipper.model;

import java.util.Date;

public class Clipping implements Comparable<Clipping> {
	private String author;
	private String title;
	private String location;
	private Date date;
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
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getQuote() {
		return quote;
	}
	public void setQuote(String quote) {
		this.quote = quote;
	}
	public boolean isGreater(Clipping clipping) {
		return compareTo(clipping) == 1;
	}
	
	@Override
	public String toString() {
		return quote;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((quote == null) ? 0 : quote.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Clipping other = (Clipping) obj;
		if (quote == null) {
			if (other.quote != null)
				return false;
		} else if (!quote.equals(other.quote))
			return false;
		return true;
	}
	@Override
	public int compareTo(Clipping o) {
		return this.date.compareTo(o.getDate());
	}
}
