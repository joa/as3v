// $ANTLR 3.1 AS3.g 2008-08-27 19:31:34

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class AS3Parser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "NUMERIC"
    };
    public static final int NUMERIC=4;
    public static final int EOF=-1;

    // delegates
    // delegators


        public AS3Parser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public AS3Parser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return AS3Parser.tokenNames; }
    public String getGrammarFileName() { return "AS3.g"; }



    // $ANTLR start "compilationUnit"
    // AS3.g:58:1: compilationUnit : ( NUMERIC )+ EOF ;
    public final void compilationUnit() throws RecognitionException {
        try {
            // AS3.g:58:17: ( ( NUMERIC )+ EOF )
            // AS3.g:58:19: ( NUMERIC )+ EOF
            {
            // AS3.g:58:19: ( NUMERIC )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==NUMERIC) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // AS3.g:58:19: NUMERIC
            	    {
            	    match(input,NUMERIC,FOLLOW_NUMERIC_in_compilationUnit46); 

            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);

            match(input,EOF,FOLLOW_EOF_in_compilationUnit49); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "compilationUnit"

    // Delegated rules


 

    public static final BitSet FOLLOW_NUMERIC_in_compilationUnit46 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_EOF_in_compilationUnit49 = new BitSet(new long[]{0x0000000000000002L});

}