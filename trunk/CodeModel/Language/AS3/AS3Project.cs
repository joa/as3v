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
using AS3V.CodeModel.Exceptions;
using System.IO;
using AS3V.CodeModel.Generic.Enumerators;

namespace AS3V.CodeModel.Language.AS3
{
    public class AS3Project : IProject
    {
        private string _name;
        private string _path;

        private List<AS3PackageRoot> _roots;

        public AS3Project()
        {
            _name = "";
            _path = "";
            _roots = new List<AS3PackageRoot>();
        }

        public void Build(string path, List<string> sourceFolder)
        {
            if (path.LastIndexOf('/') != path.Length - 1)
            {
                path += '/';
            }

            if (!Directory.Exists(path))
            {
                throw new Exception(path + " does not exist.");
            }

            _name = new FileInfo(path.Substring(0,path.Length - 1)).Name;
            _path = path;

            int n = sourceFolder.Count;

            for (int i = 0; i < n; ++i)
            {
                string rootPath;

                if (sourceFolder[i][1] != ':')
                {
                    rootPath = path + sourceFolder[i];
                }
                else
                {
                    rootPath = path;
                }

                AS3PackageRoot root = new AS3PackageRoot();

                root.Build(this, null, rootPath);

                _roots.Add(root);                
            }
        }

        #region IProject Member

        public int RootCount
        {
            get { return _roots.Count; }
        }

        public IPackageRoot RootAt(int index)
        {
            if (index < 0 || index >= RootCount)
                throw new RangeException(index, RootCount);

            return _roots[index];
        }

        public IEnumerable<IPackageRoot> RootEnumerator
        {
            get
            {
                return new PackageRootEnumerator(this);
            }
        }

        public string Name
        {
            get
            {
                return _name;
            }
        }

        public string DirectoryPath
        {
            get
            {
                return _path;
            }
        }

        public bool HasCompilationUnit(string unitPath)
        {
            return null != CompilationUnit(unitPath);
        }

        public ICompilationUnit CompilationUnit(string unitPath)
        {
            int charIndex = unitPath.LastIndexOf('.');

            if (-1 == charIndex)
            {
                foreach (AS3PackageRoot root in RootEnumerator)
                {
                    ICompilationUnit unit = root.CompilationUnit(unitPath);

                    if (null != unit)
                    {
                        return unit;
                    }
                }

                return null;
            }
            else
            {
                string unitName = unitPath.Substring(charIndex + 1);
                string packagePath = unitPath.Substring(0, charIndex);

                IPackage package = Package(packagePath);

                return package.CompilationUnit(unitName);
            }
        }

        public bool HasPackage(string packagePath)
        {
            return null != Package(packagePath);
        }

        public IPackage Package(string packagePath)
        {
            IPackage package;
            string[] pathElements = packagePath.Split('.');

            int i;
            int n = pathElements.Length;

            foreach (AS3PackageRoot root in RootEnumerator)
            {
                package = root;

                for (i = 0; i < n; ++i)
                {
                    package = package.Package(pathElements[i]);

                    if (null == package)
                    {
                        break;
                    }
                }

                if (null != package)
                {
                    return package;
                }
            }

            return null;
        }

        public void Parse()
        {
            foreach(IPackageRoot root in RootEnumerator)
            {
                ParsePackage(root);
            }
        }

        private void ParsePackage(IPackage package)
        {
            foreach (ICompilationUnit compilationUnit in package.CompilationUnitEnumerator)
            {
                compilationUnit.Parse();
            }

            foreach (IPackage nextPackage in package.PackageEnumerator)
            {
                ParsePackage(nextPackage);
            }
        }

        #endregion
    }
}
