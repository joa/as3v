/*
Copyright (c) 2008 Joa Ebert

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

using System;
using System.Collections.Generic;
using System.Text;
using AS3V.CodeModel.Language.AS3;
using AS3V.CodeModel.Generic;
using AS3V.CodeModel.Generic.Parser;
using System.IO;

namespace AS3V
{
    class Program
    {
        private enum Language
        {
            None,
            ActionScript3
        }

        public static void Main(string[] args)
        {
            Console.Title = "AS3V";

            string projectPath = "";
            List<string> packageRoots = new List<string>();
            Language language = Language.None;
            IProject project = null;

            #region Parse input arguments

            try
            {
                for (int i = 0, n = args.Length; i < n; ++i)
                {
                    switch (args[i])
                    {
                        case "-project":
                        case "-p":
                            projectPath = args[++i];
                            break;

                        case "-src":
                        case "-s":
                            packageRoots.Add(args[++i]);
                            break;

                        case "-h"://try to make everyone happy...
                        case "-help":
                        case "--help":
                        case "-?":
                        case "/?":
                        case "/help":
                            DisplayHelp();
                            break;

                        case "-language":
                        case "-l":
                            string lang = args[++i].ToLower();
                            switch (lang)
                            {
                                case "as3":
                                    language = Language.ActionScript3;
                                    break;
                                default:
                                    ErrorExit("Unknown langauge " + language + ". Use -h for help.");
                                    break;
                            }
                            break;
                        default:
                            ErrorExit("Unexpected argument " + args[i] + ". Use -h for help.");
                            break;
                    }
                }
            }
            catch (Exception exception)
            {
                ErrorExit("Error while parsing arguments. Use -h for help.", exception);
            }

            #endregion

            #region Validate arguments

            if ("" == projectPath)
            {
                ErrorExit("No project path given.");
            }
            if (0 == packageRoots.Count)
            {
                ErrorExit("No package root given.");
            }

            switch (language)
            {
                case Language.None:
                    ErrorExit("No language specified.");
                    break;

                case Language.ActionScript3:
                    project = new AS3Project();
                    break;
            }

            #endregion

            #region Build project structure

            try
            {
                Console.Out.WriteLine("[i] Constructing project structure ...");
                project.Build(projectPath, packageRoots);
            }
            catch (Exception exception)
            {
                ErrorExit("Error while constructing project structure ...", exception);
            }

            #endregion

            #region Output project structure

            try
            {
                Console.Out.WriteLine("[i] Project structure:");
                Console.Out.WriteLine("\tName: " + project.Name);
                Console.Out.WriteLine("\tRoots:");

                foreach (IPackageRoot root in project.RootEnumerator)
                {
                    if (root.DirectoryPath.StartsWith(project.DirectoryPath))
                    {
                        Console.Out.WriteLine("\t\t" + root.DirectoryPath.Substring(project.DirectoryPath.Length));
                    }
                    else
                    {
                        Console.Out.WriteLine("\t\t" + root.DirectoryPath);
                    }
                }

                Console.Out.WriteLine("\tCompilation units:");

                foreach (IPackageRoot root in project.RootEnumerator)
                {
                    OutputUnits(root);
                }
            }
            catch (Exception exception)
            {
                ErrorExit("Error while outputtting project structure ...", exception);
            }

            #endregion

            #region Build AST

            Console.Out.WriteLine("[i] Building AST ...");

            try
            {
                project.Parse();
            }
            catch (Exception exception)
            {
                ErrorExit("Error while parsing source code ...", exception);
            }

            Console.WriteLine("[+] Done parsing");

            #endregion 

            #region Output messages

            try
            {
                int totalWarnings = 0;
                int totalErrors = 0;

                Console.WriteLine("[i] Messages:");

                foreach (IPackageRoot root in project.RootEnumerator)
                {
                    OutputMessages(root, ref totalWarnings, ref totalErrors);
                }

                Console.WriteLine(String.Format("[i] Errors: {0}, Warnings: {1}", totalErrors, totalWarnings));
            }
            catch (Exception exception)
            {
                ErrorExit("Error while outputting messages ...", exception);
            }

            #endregion

#if DEBUG
            Console.ReadLine();
#endif
        }

        private static void DisplayHelp()
        {
            //TODO display arguments etc.
        }

        private static void ErrorExit(string message)
        {
            ErrorExit(message, null);
        }

        private static void ErrorExit(string message, Exception exception)
        {
            Console.Error.WriteLine("[-] " + message);

            if (null != exception)
            {
                Console.Error.WriteLine(exception.Message);
                Console.Error.WriteLine(exception.StackTrace);
            }
#if DEBUG
            Console.ReadLine();
#endif
            Environment.Exit(-1);
        }

        private static void OutputUnits(IPackage package)
        {
            foreach (ICompilationUnit unit in package.CompilationUnitEnumerator)
            {
                Console.WriteLine("\t\t" + unit.UnitPath);
            }

            foreach (IPackage nextPackage in package.PackageEnumerator)
            {
                OutputUnits(nextPackage);
            }
        }

        private static void OutputMessages(IPackage package, ref int warnings, ref int errors)
        {
            foreach (ICompilationUnit unit in package.CompilationUnitEnumerator)
            {
                if (unit.Parser.WarningCount > 0 || unit.Parser.ErrorCount > 0)
                {
                    Console.Out.WriteLine("\tUnit: " + unit.UnitPath);

                    if (unit.Parser.WarningCount > 0)
                    {
                        Console.Out.WriteLine("\tWarnings:");

                        foreach (ParserWarning warning in unit.Parser.WarningEnumerator)
                        {
                            Console.Out.WriteLine("\t\t" + warning.Message);
                            ++warnings;
                        }
                    }

                    if (unit.Parser.ErrorCount > 0)
                    {
                        Console.Out.WriteLine("\tErrors:");

                        foreach (ParserError error in unit.Parser.ErrorEnumerator)
                        {
                            Console.Out.WriteLine("\t\t" + error.Message);
                            ++errors;
                        }
                    }

                    Console.Out.Write("\n");
                }
            }

            foreach (IPackage nextPackage in package.PackageEnumerator)
            {
                OutputMessages(nextPackage, ref warnings, ref errors);
            }
        }
    }
}
