using System;
using System.Collections.Generic;
using System.Text;

namespace AS3V.CodeModel.Generic.Parser
{
    public interface INode
    {
        int ChildCount { get; }
        INode ChildAt(int index);

        IEnumerable<INode> ChildEnumerator { get; }
    }
}
