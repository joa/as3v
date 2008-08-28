using System;
using System.Collections.Generic;
using System.Text;

namespace AS3V.CodeModel.Generic.Parser.Enumerators
{
    public class NodeEnumerator : IEnumerable<INode>
    {
        private INode _owner;

        public NodeEnumerator(INode owner)
        {
            _owner = owner;
        }

        ~NodeEnumerator()
        {
            _owner = null;
        }

        #region IEnumerable<INode> Member

        public IEnumerator<INode> GetEnumerator()
        {
            int n = _owner.ChildCount;
            int i = 0;

            for (; i < n; ++i)
            {
                yield return _owner.ChildAt(i);
            }
        }

        #endregion

        #region IEnumerable Member

        System.Collections.IEnumerator System.Collections.IEnumerable.GetEnumerator()
        {
            int n = _owner.ChildCount;
            int i = 0;

            for (; i < n; ++i)
            {
                yield return _owner.ChildAt(i);
            }
        }

        #endregion
    }
}
