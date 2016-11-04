package apulbere.clipper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import apulbere.clipper.model.Clipping;
import apulbere.clipper.model.ClippingBuilder;

public class Clipper {
	private static Logger logger = Logger.getLogger(Clipper.class);
	private Properties properties;
	private List<String> dateFormats;
	private Pattern pattern;

	public Clipper() {
		properties = loadProperties();
		dateFormats = Arrays.asList(properties.getProperty("dateFormats").split(";"));
		pattern = Pattern.compile(properties.getProperty("expression"));
	}

	public Optional<Clipping> create(String content) {
		Matcher matcher = pattern.matcher(content);
		if(matcher.find()) {
			ClippingBuilder builder = new ClippingBuilder(dateFormats);
			builder.setTitle(matcher.group(1));
			builder.setAuthor(matcher.group(2));
			builder.setLocation(Optional.ofNullable(matcher.group(3)), Optional.ofNullable(matcher.group(4)));
			builder.setDate(matcher.group(5));
			builder.setQuote(matcher.group(6));
			return builder.build();
		}
		return Optional.empty();
	}

	public Collection<Clipping> createFromFile(File file) {
		Collection<Clipping> clippings = new TreeSet<>();
		try {
			String content = new String(Files.readAllBytes(file.toPath()));
			for(String rawClipping: content.split(properties.getProperty("separator"))) {
				Optional<Clipping> obj = create(rawClipping);
				if(obj.isPresent()) {
					clippings.add(obj.get());
				}
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return clippings;
	}
	
	public void writeToFile(Collection<Clipping> clippings, Path path) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormats.get(0));
		String outputFormat = properties.getProperty("outputFormat")
										.replaceAll("${newLine}", System.getProperty("line.separator"))
										.replaceAll("${separator}", properties.getProperty("separator"));
		
		List<String> outputLines = new ArrayList<>(clippings.size());
		for(Clipping clipping: clippings) {
			String clippingLine = String.format(outputFormat, clipping.getTitle(), clipping.getAuthor(), clipping.getLocation(),
					dateFormat.format(clipping.getDate()), clipping.getQuote());
			outputLines.add(clippingLine);
		}
		try {
			Files.write(path, outputLines, StandardCharsets.UTF_8, StandardOpenOption.APPEND, StandardOpenOption.CREATE);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public Collection<Clipping> getDifference(Collection<Clipping> source, Collection<Clipping> destination) {
		if(!destination.isEmpty()) {
			TreeSet<Clipping> sortedClippings = new TreeSet<>(destination);
			final Clipping last = sortedClippings.last();
			return source.stream().filter(a -> a.isGreater(last)).collect(Collectors.toList());
		} else {
			return new ArrayList<>(source);
		}
	}

	public Properties loadProperties() {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties properties = new Properties();
		try(InputStream resourceStream = loader.getResourceAsStream("config.properties")) {
			properties.load(resourceStream);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return properties;
	}
}
