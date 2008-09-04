package com.joa_ebert.as3v.projectModel;

import java.util.Iterator;

public interface IPackage {

	String name();

	String filePath();

	IPackage parent();

	IProject project();

	int packageCount();

	int compilationUnitCount();

	IPackage packageAt(int index);

	ICompilationUnit compilationUnitAt(int index);

	Iterator<IPackage> packageIterator();

	Iterator<ICompilationUnit> compilationUnitIterator();

	String packagePath();

	boolean hasPackage(String name);

	boolean hasCompilationUnit(String name);

	IPackage package$(String name);

	ICompilationUnit compilationUnit(String name);
}
