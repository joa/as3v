package com.joa_ebert.as3v.language.as3;

import java.io.File;

import com.joa_ebert.as3v.projectModel.ICompilationUnit;
import com.joa_ebert.as3v.projectModel.IProject;
import com.joa_ebert.as3v.validator.IValidator;

public class AS3CompilationUnit implements ICompilationUnit {

	private String _name = "";
	private String _path = "";
	private AS3Package _package = null;
	private AS3Project _project = null;
	private AS3Validator _validator = new AS3Validator();
	private String _unitPath = null;

	protected void build(AS3Project project, AS3Package parent,
			String fileSystemPath) {
		_project = project;
		_package = parent;
		_path = fileSystemPath;

		_name = new File(fileSystemPath).getName();
		_name = _name.substring(0, _name.length() - 3);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String fileSystemPath() {
		return _path;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String name() {
		return _name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String unitPath() {
		if (null != _unitPath)
			return _unitPath;

		String result = name();
		String packagePath = _package.packagePath();

		if (packagePath.length() > 0)
			result = packagePath + "." + result;

		_unitPath = result;

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validate() throws Exception {
		validator().validate(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IValidator validator() {
		return _validator;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IProject project() {
		return _project;
	}

}
