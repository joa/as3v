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

namespace AS3V.CodeModel.Generic.Parser.Enumerators
{
    public class ParserWarningEnumerator : IEnumerable<ParserWarning>
    {
        private IValidator _owner;

        public ParserWarningEnumerator(IValidator owner)
        {
            _owner = owner;
        }

        ~ParserWarningEnumerator()
        {
            _owner = null;
        }

        #region IEnumerable<ParserWarning> Member

        public IEnumerator<ParserWarning> GetEnumerator()
        {
            int n = _owner.WarningCount;

            for (int i = 0; i < n; ++i)
            {
                yield return _owner.WarningAt(i);
            }
        }

        #endregion

        #region IEnumerable Member

        System.Collections.IEnumerator System.Collections.IEnumerable.GetEnumerator()
        {
            int n = _owner.WarningCount;

            for (int i = 0; i < n; ++i)
            {
                yield return _owner.WarningAt(i);
            }
        }

        #endregion
    }
}
