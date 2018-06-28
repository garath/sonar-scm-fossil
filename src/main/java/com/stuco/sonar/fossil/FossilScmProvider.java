package com.stuco.sonar.fossil;

import java.io.File;
import java.nio.file.Path;

import org.sonar.api.batch.scm.BlameCommand;
import org.sonar.api.batch.scm.ScmProvider;

public class FossilScmProvider extends ScmProvider {

	@Override
	public String key() {
		return "fossil";
	}

	@Override
	public boolean supports(File baseDir) {
		return new File(baseDir, "_FOSSIL_").exists() || new File(baseDir, ".fslckout").exists();
	}

	@Override
	public BlameCommand blameCommand() {
		return new FossilBlameCommand();
	}

	@Override
	public String revisionId(Path path) {
		InfoCommand cmd = new InfoCommand();
		
		cmd.setRepository(path);
		cmd.execute();
		
		return cmd.getCheckoutHash();
	}

}
