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
using AS3V.CodeModel.Generic;
using System.IO;
using AS3V.CodeModel.Language.AS3.Parser;
using AS3V.CodeModel.Generic.Parser;

namespace AS3V.CodeModel.Language.AS3
{
    public class AS3CompilationUnit : ICompilationUnit
    {
        private string _name;
        private string _path;
        private AS3Package _package;
        private AS3Project _project;
        private AS3Parser _parser;

        public AS3CompilationUnit()
        {
            _name = "";
            _path = "";
            _package = null;
            _project = null;
            _parser = new AS3Parser();
        }

        public void Build(AS3Project project, AS3Package package, string path)
        {
            _project = project;
            _package = package;
            _path = path;
            _name = new FileInfo(path).Name;
            _name = _name.Remove(_name.Length - 3);
        }

        #region ICompilationUnit Member

        public string Name
        {
            get { return _name;  }
        }

        public string UnitPath
        {
            get
            {
                string result = Name;
                string packagePath = _package.PackagePath;

                if (packagePath.Length > 0)
                    result = packagePath + "." + result;

                return result;
            }
        }

        public string FilePath
        {
            get { return _path; }
        }

        public IParser Parser
        {
            get
            {
                return _parser;
            }
        }

        public void Parse()
        {
#if DEBUG
            Console.WriteLine("\tParsing " + UnitPath);
#endif
            _parser.Start(this);
        }

        #endregion
    }
}
