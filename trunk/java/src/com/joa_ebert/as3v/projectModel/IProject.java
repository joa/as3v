package com.joa_ebert.as3v.projectModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public interface IProject {

	int rootCount();

	IPackageRoot rootAt(int index);

	Iterator<IPackageRoot> rootIterator();

	
	/**
	 * Returns the name of the current project.
	 * 
	 * @return The name of the current project.
	 */
	String name();

	String fileSystemPath();

	boolean hasCompilationUnit(String unitPath);

	ArrayList<ICompilationUnit> compilationUnit(String unitPath);

	boolean hasPackage(String packagePath);
	
	ArrayList<IPackage> package$(String packagePath);
	
	void build(String fileSystemPath, ArrayList<String> packageRoots) throws IOException;
	
	void validate();
}
