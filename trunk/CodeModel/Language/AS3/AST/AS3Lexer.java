// $ANTLR 3.1 AS3.g 2008-08-27 19:31:34

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class AS3Lexer extends Lexer {
    public static final int NUMERIC=4;
    public static final int EOF=-1;

    // delegates
    // delegators

    public AS3Lexer() {;} 
    public AS3Lexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public AS3Lexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "AS3.g"; }

    // $ANTLR start "NUMERIC"
    public final void mNUMERIC() throws RecognitionException {
        try {
            int _type = NUMERIC;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // AS3.g:60:9: ( '0' .. '9' )
            // AS3.g:60:11: '0' .. '9'
            {
            matchRange('0','9'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NUMERIC"

    public void mTokens() throws RecognitionException {
        // AS3.g:1:8: ( NUMERIC )
        // AS3.g:1:10: NUMERIC
        {
        mNUMERIC(); 

        }


    }


 

}