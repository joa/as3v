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

namespace AS3V.CodeModel.Generic.Enumerators
{
    public class PackageRootEnumerator : IEnumerable<IPackageRoot>
    {
        private IProject _owner;

        public PackageRootEnumerator(IProject owner)
        {
            _owner = owner;
        }

        ~PackageRootEnumerator()
        {
            _owner = null;
        }

        #region IEnumerable<IPackageRoot> Member

        public IEnumerator<IPackageRoot> GetEnumerator()
        {
            int i = 0;
            int n = _owner.RootCount;

            for (; i < n; ++i)
            {
                yield return _owner.RootAt(i);
            }
        }

        #endregion

        #region IEnumerable Member

        System.Collections.IEnumerator System.Collections.IEnumerable.GetEnumerator()
        {
            int i = 0;
            int n = _owner.RootCount;

            for (; i < n; ++i)
            {
                yield return _owner.RootAt(i);
            }
        }

        #endregion
    }
}