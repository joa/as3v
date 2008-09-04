package com.joa_ebert.as3v.language.as3;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.joa_ebert.as3v.projectModel.ICompilationUnit;
import com.joa_ebert.as3v.projectModel.IPackage;
import com.joa_ebert.as3v.projectModel.IPackageRoot;
import com.joa_ebert.as3v.projectModel.IProject;

public final class AS3Project implements IProject {
	private String _name = "";
	private String _path = "";
	private ArrayList<IPackageRoot> _roots = new ArrayList<IPackageRoot>();

	/**
	 * Creates a new AS3Project instance.
	 */
	public AS3Project() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(String filePath, ArrayList<String> packageRoots)
			throws IOException {
		if (!filePath.endsWith(File.separator))
			filePath += File.separator;

		File[] systemRoots = File.listRoots();
		
		File project = new File(filePath);

		if (!project.exists()) {
			throw new IOException("Project path does not exist.");
		}
		
		_name = project.getName();
		_path = filePath;

		for(Iterator<String> iter = packageRoots.iterator(); iter.hasNext();)
		{
			String rootPath = iter.next();
			
			boolean isAbsolute = false;
			
			for(int i = 0; i < systemRoots.length; ++i)
			{
				if(rootPath.startsWith(systemRoots[i].getAbsolutePath()))
				{
					isAbsolute = true;
					break;
				}
			}

			if(!isAbsolute)
			{
				rootPath = filePath + rootPath;
			}
			
			AS3PackageRoot root = new AS3PackageRoot();
			
			root.build(this, null, rootPath);
			
			_roots.add(root);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<ICompilationUnit> compilationUnit(String unitPath) {
		ArrayList<ICompilationUnit> results = new ArrayList<ICompilationUnit>();
		final int charIndex = unitPath.lastIndexOf('.');

		if (-1 == charIndex) {
			for (Iterator<IPackageRoot> iter = rootIterator(); iter.hasNext();) {
				ICompilationUnit unit = iter.next().compilationUnit(unitPath);

				if (null != unit)
					results.add(unit);
			}
		} else {
			String unitName = unitPath.substring(charIndex + 1);
			String packagePath = unitPath.substring(0, charIndex);

			ArrayList<IPackage> packages = package$(packagePath);

			if (null != packages) {
				for (Iterator<IPackage> iter = packages.iterator(); iter
						.hasNext();) {
					IPackage package$ = iter.next();
					ICompilationUnit unit = package$.compilationUnit(unitName);
					if (null != unit) {
						results.add(unit);
					}
				}
			}
		}

		return results;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String filePath() {
		return _path;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasCompilationUnit(String unitPath) {
		return 0 != compilationUnit(unitPath).size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasPackage(String packagePath) {
		return 0 != package$(packagePath).size();
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
	public ArrayList<IPackage> package$(String packagePath) {
		ArrayList<IPackage> results = new ArrayList<IPackage>();
		String[] pathElements = packagePath.split(".");

		int i;
		final int n = pathElements.length;

		for (Iterator<IPackageRoot> iter = rootIterator(); iter.hasNext();) {
			IPackage package$ = iter.next();

			for (i = 0; i < n; ++i) {
				package$ = package$.package$(pathElements[i]);

				if (null == package$)
					break;
			}

			if (null != package$) {
				results.add(package$);
			}
		}

		return results;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPackageRoot rootAt(int index) {
		if (index < 0 || index >= _roots.size())
			return null;
		return _roots.get(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int rootCount() {
		return _roots.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<IPackageRoot> rootIterator() {
		return _roots.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validate() {
		try
		{
			for (Iterator<IPackageRoot> iter = rootIterator(); iter.hasNext();) {
				validate(iter.next());
			}
		}
		catch(Exception exception)
		{
			System.out.println(exception.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	private void validate(IPackage package$) throws Exception {
		for (Iterator<ICompilationUnit> iter = package$
				.compilationUnitIterator(); iter.hasNext();) {
			iter.next().validate();
		}

		for (Iterator<IPackage> iter = package$.packageIterator(); iter
				.hasNext();) {
			validate(iter.next());
		}
	}
}
