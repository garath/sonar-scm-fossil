package com.stuco.sonar.fossil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sonar.api.utils.command.StreamConsumer;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class ArtifactInfoStreamConsumer implements StreamConsumer {
	private static final Logger LOG = Loggers.get(ArtifactInfoStreamConsumer.class);

	private Pattern uuid = Pattern.compile("^uuid:\\s+(?<hash>[0-9a-fA-F]+) (?<date>[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2} .*)");
	private Pattern parent = Pattern.compile("^parent:\\s+(?<hash>[0-9a-fA-F]+) (?<date>[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2} .*)");
	private Pattern child = Pattern.compile("^child:\\s+(?<hash>[0-9a-fA-F]+) (?<date>[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2} .*)");
	private Pattern commentAndUser = Pattern.compile("^comment:\\s+(?<comment>.*) \\(user: (?<user>[^\\)]+).*");
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz", Locale.ENGLISH);
	
	private ArtifactInfoCommandResult result = new ArtifactInfoCommandResult();
	
	@Override
	public void consumeLine(String line) {
		Matcher m = uuid.matcher(line);
		if (m.matches()) {
			result.setUuidHash(m.group("hash"));
			try {
				result.setUuidDate(dateFormat.parse(m.group("date")));
			} catch (ParseException ex) {
				LOG.warn("Unrecognized UUID date", ex);
			}
		}
		
		m = parent.matcher(line);
		if (m.matches()) {
			result.setParentHash(m.group("hash"));
			try {
				result.setParentDate(dateFormat.parse(m.group("date")));
			} catch (ParseException ex) {
				LOG.warn("Unrecognized parent date", ex);
			}
		}
		
		m = child.matcher(line);
		if (m.matches()) {
			result.setChildHash(m.group("hash"));
			try {
				result.setChildDate(dateFormat.parse(m.group("date")));
			} catch (ParseException ex) {
				LOG.warn("Unrecognized child date", ex);
			}
		}
		
		m = commentAndUser.matcher(line);
		if (m.matches()) {
			result.setComment(m.group("comment"));
			result.setUser(m.group("user"));
		}
	}

	public ArtifactInfoCommandResult getResult() {
		return result;
	}
}
