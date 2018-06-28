package com.stuco.sonar.fossil;

import org.sonar.api.Plugin;

public class FossilPlugin implements Plugin {

	@Override
	public void define(Context context) {
		context.addExtensions(FossilScmProvider.class, FossilBlameCommand.class);
	}

}
