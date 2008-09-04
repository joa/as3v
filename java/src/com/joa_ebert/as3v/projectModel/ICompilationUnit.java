package com.joa_ebert.as3v.projectModel;

import com.joa_ebert.as3v.validator.IValidator;

public interface ICompilationUnit {
	String name();

	String unitPath();

	String fileSystemPath();

	void validate() throws Exception;

	IValidator validator();
	
	IProject project();
}
