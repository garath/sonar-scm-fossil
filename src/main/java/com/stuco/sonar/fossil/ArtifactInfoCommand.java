package com.stuco.sonar.fossil;

import java.io.File;

import org.sonar.api.utils.command.Command;
import org.sonar.api.utils.command.CommandExecutor;
import org.sonar.api.utils.command.StringStreamConsumer;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class ArtifactInfoCommand {
	private static final Logger LOG = Loggers.get(ArtifactInfoCommand.class);
	private final CommandExecutor commandExecutor = CommandExecutor.create();
	
	private File baseDirectory;
	private String artifactId;
	private ArtifactInfoCommandResult result;
	
	public void execute() {
		if (artifactId == null)
			throw new IllegalStateException("artifactId cannot be null");
		
		Command command = Command.create("fossil");
		command.setDirectory(baseDirectory);
		command.addArgument("info");
		command.addArgument(artifactId);
		
		ArtifactInfoStreamConsumer stdoutConsumer = new ArtifactInfoStreamConsumer();
		StringStreamConsumer stderrConsumer = new StringStreamConsumer();
		
		int exitCode = commandExecutor.execute(command, stdoutConsumer, stderrConsumer, -1);
		
		if (exitCode != 0) {
			LOG.warn("Fossil blame exit code is nonzero.");
		}
		
		result = stdoutConsumer.getResult();
	}
	
	public void setArtifactId(String artifactId) {		
		this.artifactId = artifactId;
	}
	
	public ArtifactInfoCommandResult getResult() {
		return result;
	}

	public File getBaseDirectory() {
		return baseDirectory;
	}

	public void setBasePath(File baseDirectory) {
		this.baseDirectory = baseDirectory;
	}
}
