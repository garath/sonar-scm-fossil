package com.stuco.sonar.fossil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sonar.api.batch.scm.BlameLine;
import org.sonar.api.utils.command.StreamConsumer;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class FossilBlameStreamConsumer implements StreamConsumer {
	private static final Logger LOG = Loggers.get(FossilBlameStreamConsumer.class);
	
	private static final String BLAME_REGEX = "^(?<hash>[0-9a-fA-F]+)\\s(?<date>[0-9-]+)\\s+(?<user>[^:]+):.*";
	private static final String DATE_FORMAT = "YYYY-MM-dd";
	
	private DateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
	private List<BlameLine> blameLines = new ArrayList<>();
	
	@Override
	public void consumeLine(String line) {
		line = line.trim();
		
		Pattern pattern = Pattern.compile(BLAME_REGEX);
		Matcher matcher = pattern.matcher(line);
		
		if (!matcher.matches()) {
			throw new IllegalStateException("Unrecognized blame info at line " + (blameLines.size() + 1) + ": " + line);
	    }
		
		String hash = matcher.group("hash");
		String user = matcher.group("user");
		Date date = null;
		try {
			date = format.parse(matcher.group("date"));
		} catch (ParseException e) {
			LOG.warn("Unable to parse date", e);
		}
		
		String datestring = (date == null) ? "(null)" : date.toString();
		
		LOG.debug("hash: " + hash);
		LOG.debug("user: " + user);
		LOG.debug("date: " + datestring);
		
		BlameLine blameLine = new BlameLine();
		blameLine.author(user);
		blameLine.date(date);
		blameLine.revision(hash);
		
		blameLines.add(blameLine);
	}

	public List<BlameLine> getBlameLines() {
		return blameLines;
	}
}
