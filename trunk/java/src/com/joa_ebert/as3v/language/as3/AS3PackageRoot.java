package com.joa_ebert.as3v.language.as3;

import com.joa_ebert.as3v.projectModel.IPackage;
import com.joa_ebert.as3v.projectModel.IPackageRoot;

public class AS3PackageRoot extends AS3Package implements IPackageRoot {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String name() {
		return "";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPackage parent() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String packagePath() {
		return "";
	}
}
