package com.joa_ebert.as3v.validator;

import java.util.Iterator;

import com.joa_ebert.as3v.projectModel.ICompilationUnit;
import com.joa_ebert.as3v.validator.messages.ValidatorError;
import com.joa_ebert.as3v.validator.messages.ValidatorWarning;

public interface IValidator {
	void validate(ICompilationUnit compilationUnit) throws Exception;

	int errorCount();

	int warningCount();

	int errorAt(int index);

	int warningAt(int index);

	Iterator<ValidatorError> errorIterator();

	Iterator<ValidatorWarning> warningIterator();
}
