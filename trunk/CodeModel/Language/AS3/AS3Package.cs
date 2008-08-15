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
using AS3V.CodeModel.Exceptions;
using AS3V.CodeModel.Generic.Enumerators;

namespace AS3V.CodeModel.Language.AS3
{
    public class AS3Package : IPackage
    {
        private string _name;
        private string _path;

        private AS3Project _project;
        private AS3Package _parent;
        
        private List<AS3Package> _packages;
        private List<AS3CompilationUnit> _units;

        public AS3Package()
        {
            _name = "";
            _path = "";

            _project = null;
            _parent = null;

            _packages = new List<AS3Package>();
            _units = new List<AS3CompilationUnit>();
        }

        public void Build(AS3Project project, AS3Package parent, string path)
        {
            _project = project;
            _parent = parent;

            _path = path;
            _name = new FileInfo(path).Name;

            string[] dirs = Directory.GetDirectories(path);
            string[] files = Directory.GetFiles(path);

            int i, n;

            for (i = 0, n = dirs.Length; i < n; ++i)
            {
                AS3Package package = new AS3Package();

                package.Build(project, this, dirs[i]);

                _packages.Add(package);
            }

            for (i = 0, n = files.Length; i < n; ++i)
            {
                AS3CompilationUnit unit = new AS3CompilationUnit();

                unit.Build(project, this, files[i]);

                _units.Add(unit);
            }
        }

        #region IPackage Member

        public virtual string Name
        {
            get { return _name; }
        }

        public string DirectoryPath
        {
            get { return _path; }
        }

        public virtual IPackage Parent
        {
            get { return _parent;  }
        }

        public IProject Project
        {
            get { return _project; }
        }

        public int PackageCount
        {
            get { return _packages.Count; }
        }

        public int CompilationUnitCount
        {
            get { return _units.Count; }
        }

        public IPackage PackageAt(int index)
        {
            if (index < 0 || index >= PackageCount)
            {
                throw new RangeException(index, PackageCount);
            }

            return _packages[index];
        }

        public ICompilationUnit CompilationUnitAt(int index)
        {
            if (index < 0 || index >= CompilationUnitCount)
            {
                throw new RangeException(index, CompilationUnitCount);
            }

            return _units[index];
        }

        public virtual string PackagePath
        {
            get
            {
                string result = Name;

                IPackage package = Parent;

                while (package != null)
                {
                    if (package.Name.Length > 0)
                    {
                        result = package.Name + "." + result;
                    }
                    else
                    {
                        break;
                    }

                    package = package.Parent;
                }

                return result;
            }
        }

        public IEnumerable<IPackage> PackageEnumerator
        {
            get { return new PackageEnumerator(this); }
        }

        public IEnumerable<ICompilationUnit> CompilationUnitEnumerator
        {
            get { return new CompilationUnitEnumerator(this); }
        }

        public bool HasPackage(string name)
        {
            return null != Package(name);
        }

        public IPackage Package(string name)
        {
            foreach (AS3Package package in PackageEnumerator)
            {
                if (package.Name == name)
                {
                    return package;
                }
            }

            return null;
        }

        public bool HasCompilationUnit(string name)
        {
            return null != CompilationUnit(name);
        }

        public ICompilationUnit CompilationUnit(string name)
        {
            foreach (AS3CompilationUnit unit in CompilationUnitEnumerator)
            {
                if (unit.Name == name)
                {
                    return unit;
                }
            }

            return null;
        }

        #endregion
    }
}
