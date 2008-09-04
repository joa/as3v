import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import com.joa_ebert.as3v.language.as3.AS3Project;
import com.joa_ebert.as3v.projectModel.ICompilationUnit;
import com.joa_ebert.as3v.projectModel.IPackage;
import com.joa_ebert.as3v.projectModel.IPackageRoot;
import com.joa_ebert.as3v.projectModel.IProject;

/**
 * @author Joa Ebert
 * 
 */
public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String projectPath = "";
		ArrayList<String> packageRoots = new ArrayList<String>();
		IProject project = null;

		try {
			for (int i = 0, n = args.length; i < n; ++i) {
				String argument = args[i].toLowerCase();

				if (argument.equals("-project") || argument.equals("-p")) {
					projectPath = args[++i];
				} else if (argument.equals("-src") || argument.equals("-s")) {
					packageRoots.add(args[++i]);
				} else if (argument.equals("-help") || argument.equals("-h")
						|| argument.equals("--help") || argument.equals("-?")
						|| argument.equals("/?") || argument.equals("/help")) {
					displayHelp();
				} else {
					failWithError("Unexpected argument " + args[i]
							+ ". Use -h for help.");
					break;
				}
			}
		} catch (Exception exception) {
			failWithError("Error while parsing arguments. Use -h for help.",
					exception);
		}

		if (projectPath.equals("")) {
			failWithError("No project path given. Use -h for help.");
		} else {
			if (0 == packageRoots.size()) {
				failWithError("No package root given. Use -h for help.");
			}
		}

		//
		//
		//
//		projectPath = "C:\\FLEX_SDK\\frameworks\\projects\\framework";
		//
		//
		//
		
		try {
			System.out.println("[i] Constructing Project Structure");
			project = new AS3Project();
			project.build(projectPath, packageRoots);
		} catch (Exception exception) {
			failWithError("Error while building project structure.", exception);
		}

		try {
			System.out.println("[+] Project Structure");
			System.out.println("\tName:\t" + project.name());
			System.out.print("\tRoots:\t");

			boolean firstPackageRoot = true;

			for (Iterator<IPackageRoot> iter = project.rootIterator(); iter
					.hasNext();) {
				IPackageRoot root = iter.next();
				String path = root.filePath();

				if (path.startsWith(project.filePath()))
					path = "." + File.separator
							+ path.substring(project.filePath().length());

				System.out.println((!firstPackageRoot ? "\t\t" : "") + path);

				firstPackageRoot = false;
			}

			System.out.println("\tCompilation Units:");

			for (Iterator<IPackageRoot> iter = project.rootIterator(); iter
					.hasNext();) {
				IPackageRoot root = iter.next();

				outputUnits(root);
			}
		} catch (Exception exception) {
			failWithError("Error while dumping project structure.", exception);
		}
		
		try
		{
			System.out.println("[i] Validating Compilation Units");
			project.validate();
		}
		catch(Exception exception)
		{
			failWithError("Error while validating compilation untis.", exception);
		}
	}

	private static void outputUnits(IPackage package$) {
		for (Iterator<ICompilationUnit> iter = package$
				.compilationUnitIterator(); iter.hasNext();) {
			ICompilationUnit unit = iter.next();

			System.out.println("\t\t" + unit.unitPath());
		}

		for (Iterator<IPackage> iter = package$.packageIterator(); iter
				.hasNext();) {
			outputUnits(iter.next());
		}
	}

	private static void failWithError(String message) {
		failWithError(message, null);
	}

	private static void failWithError(String message, Exception exception) {
		System.err.println("[-] " + message);
		if (null != exception)
			System.err.println("\t" + exception.getMessage());
		System.exit(-1);
	}

	private static void displayHelp() {
		System.out.println("[i] AS3V Help");
		System.out.println("\t\tTODO");
	}
}
