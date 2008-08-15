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
using AS3V.CodeModel.Generic.Parser;
using AS3V.CodeModel.Generic;
using System.IO;
using AS3V.CodeModel.Exceptions;
using AS3V.CodeModel.Generic.Parser.Enumerators;

namespace AS3V.CodeModel.Language.AS3.Parser
{
    public partial class AS3Parser : IParser
    {
        private List<ParserError> _errors;
        private List<ParserWarning> _warnings;

        public AS3Parser()
        {
            _errors = new List<ParserError>();
            _warnings = new List<ParserWarning>();

        }

        private void AddError(string message)
        {
            _errors.Add(new ParserError(message));
        }

        private void AddWarning(string message)
        {
            _warnings.Add(new ParserWarning(message));
        }

        #region IParser Member

        public void Start(ICompilationUnit unit)
        {
            FileStream stream = null;

            try
            {
                if (null != (stream = File.Open(unit.FilePath, FileMode.Open)))
                {
                    Parse(stream);
                    stream.Close();
                }
                else
                {
                    AddError("Could not open file.");
                }
            }
            catch (Exception)
            {
                AddError("Could not open file.");
            }
            finally
            {
                if (null != stream)
                {
                    try
                    {
                        stream.Close();
                    }
                    catch (Exception) { }
                }
            }
        }

        public int ErrorCount
        {
            get
            {
                return _errors.Count;
            }
        }

        public int WarningCount
        {
            get
            {
                return _warnings.Count;
            }
        }

        public ParserError ErrorAt(int index)
        {
            if (index < 0 || index >= ErrorCount)
            {
                throw new RangeException(index, ErrorCount);
            }

            return _errors[index];
        }

        public ParserWarning WarningAt(int index)
        {
            if (index < 0 || index >= WarningCount)
            {
                throw new RangeException(index, WarningCount);
            }

            return _warnings[index];
        }

        public IEnumerable<ParserError> ErrorEnumerator
        {
            get
            {
                return new ParserErrorEnumerator(this);
            }
        }

        public IEnumerable<ParserWarning> WarningEnumerator
        {
            get
            {
                return new ParserWarningEnumerator(this);
            }
        }

        #endregion
    }
}
