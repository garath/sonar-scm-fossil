package com.stuco.sonar.fossil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sonar.api.utils.command.StreamConsumer;

class InfoStreamConsumer implements StreamConsumer {
	private Pattern current = Pattern.compile("^checkout:\\s+ (?<hash>[0-9a-fA-F]+) .*");
	private Pattern parent = Pattern.compile("^parent:\\s+ (?<hash>[0-9a-fA-F]+) .*");
	
	String currentHash;
	String parentHash;

	@Override
	public void consumeLine(String line) {
		Matcher m = current.matcher(line);
		if (m.matches()) {
			currentHash = m.group("hash");
		}
		
		m = parent.matcher(line);
		if (m.matches()) {
			parentHash = m.group("hash");
		}
	}
	
	public String getCurrentHash() {
		return currentHash;
	}
	
	public String getParentHash() {
		return parentHash;
	}
}