package com.stuco.sonar.fossil;

import java.nio.file.Path;

import org.sonar.api.utils.command.Command;
import org.sonar.api.utils.command.CommandExecutor;
import org.sonar.api.utils.command.StringStreamConsumer;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class InfoCommand {
	private static final Logger LOG = Loggers.get(InfoCommand.class);
	private final CommandExecutor commandExecutor = CommandExecutor.create();
	
	private String checkoutHash;
	private String parentHash;
	private Path repository;
	
	public void execute() {
		Command command = Command.create("fossil");
		command.setDirectory(repository.toFile());
		command.addArgument("info");
		
		InfoStreamConsumer stdoutConsumer = new InfoStreamConsumer();
		StringStreamConsumer stderrConsumer = new StringStreamConsumer();
		
		int exitCode = commandExecutor.execute(command, stdoutConsumer, stderrConsumer, -1);
		
		if (exitCode != 0) {
			LOG.warn("Fossil blame exit code is nonzero");
		}
		
		checkoutHash = stdoutConsumer.currentHash;
		parentHash = stdoutConsumer.parentHash;
	}

	public Path getRepository() {
		return repository;
	}

	public void setRepository(Path repository) {
		this.repository = repository;
	}

	public String getCheckoutHash() {
		return checkoutHash;
	}

	public String getParentHash() {
		return parentHash;
	}
}
