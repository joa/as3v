using System;
using System.Collections.Generic;
using System.Text;
using AS3V.CodeModel.Generic.Parser;
using System.IO;
using CodeModel.Language.AS3.AST;
using AS3V.CodeModel.Generic;
using Antlr.Runtime;
using AS3V.CodeModel.Exceptions;
using AS3V.CodeModel.Generic.Parser.Enumerators;
using Antlr.Runtime.Tree;

namespace AS3V.CodeModel.Language.AS3
{
    public class AS3Validator : IValidator
    {
        private List<ParserError> _errors;
        private List<ParserWarning> _warnings;

        public AS3Validator()
        {
            _errors = new List<ParserError>();
            _warnings = new List<ParserWarning>();

        }

        private void Validate(ICompilationUnit unit)
        {
            AS3Lexer lexer = new AS3Lexer(new ANTLRFileStream(unit.FilePath));
            AS3Parser parser = new AS3Parser(new CommonTokenStream(lexer));
            
            try
            {
                AS3Parser.compilationUnit_return r = parser.compilationUnit();
                Console.WriteLine(((ITree)r.Tree).ToStringTree());
            }
            catch (Exception exception)
            {
                Console.WriteLine(exception.Message);
                Console.WriteLine(exception.StackTrace);

                return;
            }
        }

        private void AddError(string message, int lineIndex, int characterIndex)
        {
            _errors.Add(new ParserError(message + " (line: " + lineIndex + ", char: " + characterIndex + ")"));
        }

        private void AddError(string message)
        {
            _errors.Add(new ParserError(message));
        }

        private void AddWarning(string message, int lineIndex, int characterIndex)
        {
            _errors.Add(new ParserError(message + " (line: " + lineIndex + ", char: " + characterIndex + ")"));
        }

        private void AddWarning(string message)
        {
            _warnings.Add(new ParserWarning(message));
        }

        #region IParser Member

        public void Start(ICompilationUnit unit)
        {
            try
            {
                Validate(unit);
            }
            catch (Exception)
            {
                Console.WriteLine("Error while validating ...");
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