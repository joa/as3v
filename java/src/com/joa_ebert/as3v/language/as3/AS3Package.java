package com.joa_ebert.as3v.language.as3;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import com.joa_ebert.as3v.projectModel.ICompilationUnit;
import com.joa_ebert.as3v.projectModel.IPackage;
import com.joa_ebert.as3v.projectModel.IProject;

public class AS3Package implements IPackage {

	private String _name = "";
	private String _path = "";
	private String _packagePath = null;
	private AS3Project _project = null;

	private AS3Package _parent = null;
	private ArrayList<IPackage> _packages = new ArrayList<IPackage>();
	private ArrayList<ICompilationUnit> _units = new ArrayList<ICompilationUnit>();

	public AS3Package() {
	}

	protected void build(AS3Project project, AS3Package parent,
			String fileSystemPath) {
		File packageFile = new File(fileSystemPath);

		_project = project;
		_parent = parent;
		_path = fileSystemPath;
		_name = packageFile.getName();

		String files[] = packageFile.list();

		int i = 0;
		int n = files.length;

		for (; i < n; ++i) {
			File child = new File(packageFile.getAbsolutePath()
					+ File.separator + files[i]);

			if (child.isDirectory()) {
				AS3Package package$ = new AS3Package();

				package$.build(project, this, child.getAbsolutePath());

				_packages.add(package$);
			} else {
				if (child.getAbsolutePath().endsWith(".as")) {
					AS3CompilationUnit unit = new AS3CompilationUnit();

					unit.build(project, this, child.getAbsolutePath());

					_units.add(unit);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ICompilationUnit compilationUnit(String name) {
		for (Iterator<ICompilationUnit> iter = compilationUnitIterator(); iter
				.hasNext();) {
			ICompilationUnit unit = iter.next();

			if (unit.name().equals(name))
				return unit;
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ICompilationUnit compilationUnitAt(int index) {
		if (index < 0 || index >= _units.size())
			return null;
		return _units.get(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compilationUnitCount() {
		return _units.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<ICompilationUnit> compilationUnitIterator() {
		return _units.iterator();
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
	public boolean hasCompilationUnit(String name) {
		return null != compilationUnit(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasPackage(String name) {
		return null != package$(name);
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
	public IPackage package$(String name) {
		for (Iterator<IPackage> iter = packageIterator(); iter.hasNext();) {
			IPackage package$ = iter.next();

			if (package$.name().equals(name))
				return package$;
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPackage packageAt(int index) {
		if (index < 0 || index >= _packages.size())
			return null;
		return _packages.get(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int packageCount() {
		return _packages.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<IPackage> packageIterator() {
		return _packages.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String packagePath() {
		if (null != _packagePath)
			return _packagePath;

		String result = name();

		IPackage package$ = parent();

		while (null != package$) {
			if (package$.name().length() > 0) {
				result = package$.name() + "." + result;
			} else {
				break;
			}

			package$ = package$.parent();
		}

		_packagePath = result;
		
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPackage parent() {
		return _parent;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IProject project() {
		return _project;
	}

}
