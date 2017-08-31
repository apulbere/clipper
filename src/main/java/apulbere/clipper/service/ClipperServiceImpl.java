package apulbere.clipper.service;

import apulbere.clipper.model.Clipping;
import apulbere.clipper.model.ClippingBuilder;
import apulbere.clipper.model.KindleConstants;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

import static apulbere.clipper.model.KindleConstants.OUTPUT_FORMAT;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class ClipperServiceImpl implements ClipperService {
	private static Logger logger = Logger.getLogger(ClipperServiceImpl.class);
	private Properties properties;

	public ClipperServiceImpl() {
		properties = loadProperties();
	}

	private DateTimeFormatter createDateTimeFormater() {
		if(properties != null) {
			return DateTimeFormatter.ofPattern(properties.getProperty("dateFormats"));
		}
		return null;
	}

	@Override
	public void writeToFile(List<Clipping> clippings, Path path) {
		writeToFile(clippings, path, createDateTimeFormater(), properties.getProperty("separator"));
	}

	@Override
	public void writeToFile(List<Clipping> clippings, Path path, DateTimeFormatter dateTimeFormatter, String separator) {
		Objects.requireNonNull(dateTimeFormatter);
		Objects.requireNonNull(separator);

		List<String> content = clippings.stream()
				.map(clipping -> format(OUTPUT_FORMAT,
								clipping.getTitle(),
								clipping.getAuthor(),
								clipping.getLocation(),
								dateTimeFormatter.format(clipping.getDateTime()),
								clipping.getQuote(),
								separator))
				.collect(Collectors.toList());

		try {
			Files.write(path, content, StandardCharsets.UTF_8, StandardOpenOption.APPEND, StandardOpenOption.CREATE);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public List<Clipping> create(File file) {
		return create(file, properties.getProperty("separator"), createDateTimeFormater());
	}

	@Override
	public List<Clipping> create(File file, String clippingSeparator) {
		return create(file, clippingSeparator, createDateTimeFormater());
	}

	@Override
	public List<Clipping> create(File file, String clippingSeparator, DateTimeFormatter dateTimeFormatter) {
		Objects.requireNonNull(clippingSeparator);
		Objects.requireNonNull(dateTimeFormatter);
		try {
			String content = new String(Files.readAllBytes(file.toPath()));
			return stream(content.split(clippingSeparator))
					.map(rawData -> new ClippingBuilder(dateTimeFormatter).fromRawData(rawData))
					.filter(ClippingBuilder::isValid)
					.map(ClippingBuilder::build)
					.collect(toList());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return emptyList();
		}
	}

	private Properties loadProperties() {
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
