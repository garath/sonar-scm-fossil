package com.stuco.sonar.fossil;

import java.util.List;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.scm.BlameCommand;
import org.sonar.api.batch.scm.BlameLine;
import org.sonar.api.utils.command.Command;
import org.sonar.api.utils.command.CommandExecutor;
import org.sonar.api.utils.command.StringStreamConsumer;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

class FossilBlameCommand extends BlameCommand {
	private static final Logger LOG = Loggers.get(FossilBlameCommand.class);
	private final CommandExecutor commandExecutor = CommandExecutor.create();

	@Override
	public void blame(BlameInput input, BlameOutput output) {
		
		for (InputFile file : input.filesToBlame()) {
			LOG.debug("Blaming " + file.filename());
			
			Command command = Command.create("fossil");
			command.setDirectory(input.fileSystem().baseDir());
			command.addArgument("blame");
			command.addArgument(file.relativePath());
			LOG.debug("Working directory: " + input.fileSystem().baseDir());
			LOG.debug("File: " + file.uri());
			LOG.debug("Command: " + command.toCommandLine());
			
			FossilBlameStreamConsumer stdoutConsumer = new FossilBlameStreamConsumer();
			StringStreamConsumer stderrConsumer = new StringStreamConsumer();
			int exitCode = commandExecutor.execute(command, stdoutConsumer, stderrConsumer, -1);
			
			if (exitCode != 0) {
				LOG.warn("Fossil blame exit code is nonzero");
			}
			
			List<BlameLine> lines = stdoutConsumer.getBlameLines();
			for (BlameLine l : lines) {
				ArtifactInfoCommand infoCmd = new ArtifactInfoCommand();
				infoCmd.setBasePath(input.fileSystem().baseDir());
				infoCmd.setArtifactId(l.revision());
				infoCmd.execute();
				
				LOG.debug("Replacing " + l.author() + " with " + infoCmd.getResult().getUser());
				l.author(infoCmd.getResult().getUser());
				LOG.debug("Replacing " + l.revision() + " with " + infoCmd.getResult().getUuidHash());
				l.revision(infoCmd.getResult().getUuidHash());
				LOG.debug("Replacing " + l.date() + " with " + infoCmd.getResult().getUuidDate());
				l.date(infoCmd.getResult().getUuidDate());
			}
		
			if (lines.size() == file.lines() - 1) {
			      // SONARPLUGINS-3097 Mercurial do not report blame on last empty line
			      lines.add(lines.get(lines.size() - 1));
			}
			output.blameResult(file, lines);
		}
	}

}
