package org.monet.editor.dsl.ui.contentassist.antlr.internal;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.ui.editor.contentassist.antlr.internal.Lexer;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalMonetLocalizationLanguageLexer extends Lexer {
    public static final int RULE_HEX=6;
    public static final int T__50=50;
    public static final int T__19=19;
    public static final int T__15=15;
    public static final int T__59=59;
    public static final int T__16=16;
    public static final int T__17=17;
    public static final int T__18=18;
    public static final int T__55=55;
    public static final int T__56=56;
    public static final int T__57=57;
    public static final int T__14=14;
    public static final int T__58=58;
    public static final int T__51=51;
    public static final int T__52=52;
    public static final int T__53=53;
    public static final int T__54=54;
    public static final int T__60=60;
    public static final int T__61=61;
    public static final int RULE_ID=4;
    public static final int RULE_DECIMAL=8;
    public static final int T__26=26;
    public static final int T__27=27;
    public static final int T__28=28;
    public static final int RULE_INT=7;
    public static final int T__29=29;
    public static final int T__22=22;
    public static final int T__66=66;
    public static final int RULE_ML_COMMENT=10;
    public static final int T__23=23;
    public static final int T__67=67;
    public static final int T__24=24;
    public static final int T__68=68;
    public static final int T__25=25;
    public static final int T__69=69;
    public static final int T__62=62;
    public static final int T__63=63;
    public static final int T__20=20;
    public static final int T__64=64;
    public static final int T__21=21;
    public static final int T__65=65;
    public static final int T__70=70;
    public static final int T__71=71;
    public static final int T__72=72;
    public static final int RULE_LOCALE=5;
    public static final int RULE_STRING=9;
    public static final int RULE_SL_COMMENT=11;
    public static final int T__37=37;
    public static final int T__38=38;
    public static final int T__39=39;
    public static final int T__33=33;
    public static final int T__77=77;
    public static final int T__34=34;
    public static final int T__78=78;
    public static final int T__35=35;
    public static final int T__79=79;
    public static final int T__36=36;
    public static final int T__73=73;
    public static final int EOF=-1;
    public static final int T__30=30;
    public static final int T__74=74;
    public static final int T__31=31;
    public static final int T__75=75;
    public static final int T__32=32;
    public static final int T__76=76;
    public static final int T__80=80;
    public static final int T__81=81;
    public static final int T__82=82;
    public static final int T__83=83;
    public static final int RULE_WS=12;
    public static final int RULE_ANY_OTHER=13;
    public static final int T__48=48;
    public static final int T__49=49;
    public static final int T__44=44;
    public static final int T__88=88;
    public static final int T__45=45;
    public static final int T__46=46;
    public static final int T__47=47;
    public static final int T__40=40;
    public static final int T__84=84;
    public static final int T__41=41;
    public static final int T__85=85;
    public static final int T__42=42;
    public static final int T__86=86;
    public static final int T__43=43;
    public static final int T__87=87;

    // delegates
    // delegators

    public InternalMonetLocalizationLanguageLexer() {;} 
    public InternalMonetLocalizationLanguageLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public InternalMonetLocalizationLanguageLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g"; }

    // $ANTLR start "T__14"
    public final void mT__14() throws RecognitionException {
        try {
            int _type = T__14;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:11:7: ( '=' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:11:9: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__14"

    // $ANTLR start "T__15"
    public final void mT__15() throws RecognitionException {
        try {
            int _type = T__15;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:12:7: ( '||' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:12:9: '||'
            {
            match("||"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__15"

    // $ANTLR start "T__16"
    public final void mT__16() throws RecognitionException {
        try {
            int _type = T__16;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:13:7: ( '&&' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:13:9: '&&'
            {
            match("&&"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__16"

    // $ANTLR start "T__17"
    public final void mT__17() throws RecognitionException {
        try {
            int _type = T__17;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:14:7: ( 'default' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:14:9: 'default'
            {
            match("default"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__17"

    // $ANTLR start "T__18"
    public final void mT__18() throws RecognitionException {
        try {
            int _type = T__18;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:15:7: ( '+=' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:15:9: '+='
            {
            match("+="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__18"

    // $ANTLR start "T__19"
    public final void mT__19() throws RecognitionException {
        try {
            int _type = T__19;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:16:7: ( '-=' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:16:9: '-='
            {
            match("-="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__19"

    // $ANTLR start "T__20"
    public final void mT__20() throws RecognitionException {
        try {
            int _type = T__20;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:17:7: ( '*=' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:17:9: '*='
            {
            match("*="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__20"

    // $ANTLR start "T__21"
    public final void mT__21() throws RecognitionException {
        try {
            int _type = T__21;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:18:7: ( '/=' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:18:9: '/='
            {
            match("/="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__21"

    // $ANTLR start "T__22"
    public final void mT__22() throws RecognitionException {
        try {
            int _type = T__22;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19:7: ( '%=' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19:9: '%='
            {
            match("%="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__22"

    // $ANTLR start "T__23"
    public final void mT__23() throws RecognitionException {
        try {
            int _type = T__23;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:20:7: ( '==' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:20:9: '=='
            {
            match("=="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__23"

    // $ANTLR start "T__24"
    public final void mT__24() throws RecognitionException {
        try {
            int _type = T__24;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:21:7: ( '!=' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:21:9: '!='
            {
            match("!="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__24"

    // $ANTLR start "T__25"
    public final void mT__25() throws RecognitionException {
        try {
            int _type = T__25;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:22:7: ( '===' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:22:9: '==='
            {
            match("==="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__25"

    // $ANTLR start "T__26"
    public final void mT__26() throws RecognitionException {
        try {
            int _type = T__26;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:23:7: ( '!==' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:23:9: '!=='
            {
            match("!=="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__26"

    // $ANTLR start "T__27"
    public final void mT__27() throws RecognitionException {
        try {
            int _type = T__27;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:24:7: ( '>=' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:24:9: '>='
            {
            match(">="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__27"

    // $ANTLR start "T__28"
    public final void mT__28() throws RecognitionException {
        try {
            int _type = T__28;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:25:7: ( '>' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:25:9: '>'
            {
            match('>'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__28"

    // $ANTLR start "T__29"
    public final void mT__29() throws RecognitionException {
        try {
            int _type = T__29;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:26:7: ( '<' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:26:9: '<'
            {
            match('<'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__29"

    // $ANTLR start "T__30"
    public final void mT__30() throws RecognitionException {
        try {
            int _type = T__30;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:27:7: ( '->' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:27:9: '->'
            {
            match("->"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__30"

    // $ANTLR start "T__31"
    public final void mT__31() throws RecognitionException {
        try {
            int _type = T__31;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:28:7: ( '..<' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:28:9: '..<'
            {
            match("..<"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__31"

    // $ANTLR start "T__32"
    public final void mT__32() throws RecognitionException {
        try {
            int _type = T__32;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:29:7: ( '..' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:29:9: '..'
            {
            match(".."); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__32"

    // $ANTLR start "T__33"
    public final void mT__33() throws RecognitionException {
        try {
            int _type = T__33;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:30:7: ( '=>' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:30:9: '=>'
            {
            match("=>"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__33"

    // $ANTLR start "T__34"
    public final void mT__34() throws RecognitionException {
        try {
            int _type = T__34;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:31:7: ( '<>' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:31:9: '<>'
            {
            match("<>"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__34"

    // $ANTLR start "T__35"
    public final void mT__35() throws RecognitionException {
        try {
            int _type = T__35;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:32:7: ( '?:' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:32:9: '?:'
            {
            match("?:"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__35"

    // $ANTLR start "T__36"
    public final void mT__36() throws RecognitionException {
        try {
            int _type = T__36;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:33:7: ( '+' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:33:9: '+'
            {
            match('+'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__36"

    // $ANTLR start "T__37"
    public final void mT__37() throws RecognitionException {
        try {
            int _type = T__37;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:34:7: ( '-' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:34:9: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__37"

    // $ANTLR start "T__38"
    public final void mT__38() throws RecognitionException {
        try {
            int _type = T__38;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:35:7: ( '*' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:35:9: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__38"

    // $ANTLR start "T__39"
    public final void mT__39() throws RecognitionException {
        try {
            int _type = T__39;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:36:7: ( '**' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:36:9: '**'
            {
            match("**"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__39"

    // $ANTLR start "T__40"
    public final void mT__40() throws RecognitionException {
        try {
            int _type = T__40;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:37:7: ( '/' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:37:9: '/'
            {
            match('/'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__40"

    // $ANTLR start "T__41"
    public final void mT__41() throws RecognitionException {
        try {
            int _type = T__41;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:38:7: ( '%' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:38:9: '%'
            {
            match('%'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__41"

    // $ANTLR start "T__42"
    public final void mT__42() throws RecognitionException {
        try {
            int _type = T__42;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:39:7: ( '!' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:39:9: '!'
            {
            match('!'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__42"

    // $ANTLR start "T__43"
    public final void mT__43() throws RecognitionException {
        try {
            int _type = T__43;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:40:7: ( '++' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:40:9: '++'
            {
            match("++"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__43"

    // $ANTLR start "T__44"
    public final void mT__44() throws RecognitionException {
        try {
            int _type = T__44;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:41:7: ( '--' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:41:9: '--'
            {
            match("--"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__44"

    // $ANTLR start "T__45"
    public final void mT__45() throws RecognitionException {
        try {
            int _type = T__45;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:42:7: ( '.' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:42:9: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__45"

    // $ANTLR start "T__46"
    public final void mT__46() throws RecognitionException {
        try {
            int _type = T__46;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:43:7: ( 'val' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:43:9: 'val'
            {
            match("val"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__46"

    // $ANTLR start "T__47"
    public final void mT__47() throws RecognitionException {
        try {
            int _type = T__47;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:44:7: ( 'extends' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:44:9: 'extends'
            {
            match("extends"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__47"

    // $ANTLR start "T__48"
    public final void mT__48() throws RecognitionException {
        try {
            int _type = T__48;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:45:7: ( 'static' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:45:9: 'static'
            {
            match("static"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__48"

    // $ANTLR start "T__49"
    public final void mT__49() throws RecognitionException {
        try {
            int _type = T__49;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:46:7: ( 'import' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:46:9: 'import'
            {
            match("import"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__49"

    // $ANTLR start "T__50"
    public final void mT__50() throws RecognitionException {
        try {
            int _type = T__50;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:47:7: ( 'extension' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:47:9: 'extension'
            {
            match("extension"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__50"

    // $ANTLR start "T__51"
    public final void mT__51() throws RecognitionException {
        try {
            int _type = T__51;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:48:7: ( 'super' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:48:9: 'super'
            {
            match("super"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__51"

    // $ANTLR start "T__52"
    public final void mT__52() throws RecognitionException {
        try {
            int _type = T__52;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:49:7: ( 'false' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:49:9: 'false'
            {
            match("false"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__52"

    // $ANTLR start "T__53"
    public final void mT__53() throws RecognitionException {
        try {
            int _type = T__53;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:50:7: ( 'language' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:50:9: 'language'
            {
            match("language"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__53"

    // $ANTLR start "T__54"
    public final void mT__54() throws RecognitionException {
        try {
            int _type = T__54;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:51:7: ( '{' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:51:9: '{'
            {
            match('{'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__54"

    // $ANTLR start "T__55"
    public final void mT__55() throws RecognitionException {
        try {
            int _type = T__55;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:52:7: ( '}' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:52:9: '}'
            {
            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__55"

    // $ANTLR start "T__56"
    public final void mT__56() throws RecognitionException {
        try {
            int _type = T__56;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:53:7: ( ':' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:53:9: ':'
            {
            match(':'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__56"

    // $ANTLR start "T__57"
    public final void mT__57() throws RecognitionException {
        try {
            int _type = T__57;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:54:7: ( ';' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:54:9: ';'
            {
            match(';'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__57"

    // $ANTLR start "T__58"
    public final void mT__58() throws RecognitionException {
        try {
            int _type = T__58;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:55:7: ( 'instanceof' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:55:9: 'instanceof'
            {
            match("instanceof"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__58"

    // $ANTLR start "T__59"
    public final void mT__59() throws RecognitionException {
        try {
            int _type = T__59;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:56:7: ( 'as' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:56:9: 'as'
            {
            match("as"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__59"

    // $ANTLR start "T__60"
    public final void mT__60() throws RecognitionException {
        try {
            int _type = T__60;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:57:7: ( ',' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:57:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__60"

    // $ANTLR start "T__61"
    public final void mT__61() throws RecognitionException {
        try {
            int _type = T__61;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:58:7: ( ')' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:58:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__61"

    // $ANTLR start "T__62"
    public final void mT__62() throws RecognitionException {
        try {
            int _type = T__62;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:59:7: ( '#' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:59:9: '#'
            {
            match('#'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__62"

    // $ANTLR start "T__63"
    public final void mT__63() throws RecognitionException {
        try {
            int _type = T__63;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:60:7: ( '[' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:60:9: '['
            {
            match('['); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__63"

    // $ANTLR start "T__64"
    public final void mT__64() throws RecognitionException {
        try {
            int _type = T__64;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:61:7: ( ']' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:61:9: ']'
            {
            match(']'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__64"

    // $ANTLR start "T__65"
    public final void mT__65() throws RecognitionException {
        try {
            int _type = T__65;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:62:7: ( '(' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:62:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__65"

    // $ANTLR start "T__66"
    public final void mT__66() throws RecognitionException {
        try {
            int _type = T__66;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:63:7: ( 'if' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:63:9: 'if'
            {
            match("if"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__66"

    // $ANTLR start "T__67"
    public final void mT__67() throws RecognitionException {
        try {
            int _type = T__67;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:64:7: ( 'else' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:64:9: 'else'
            {
            match("else"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__67"

    // $ANTLR start "T__68"
    public final void mT__68() throws RecognitionException {
        try {
            int _type = T__68;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:65:7: ( 'switch' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:65:9: 'switch'
            {
            match("switch"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__68"

    // $ANTLR start "T__69"
    public final void mT__69() throws RecognitionException {
        try {
            int _type = T__69;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:66:7: ( 'case' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:66:9: 'case'
            {
            match("case"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__69"

    // $ANTLR start "T__70"
    public final void mT__70() throws RecognitionException {
        try {
            int _type = T__70;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:67:7: ( 'for' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:67:9: 'for'
            {
            match("for"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__70"

    // $ANTLR start "T__71"
    public final void mT__71() throws RecognitionException {
        try {
            int _type = T__71;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:68:7: ( 'while' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:68:9: 'while'
            {
            match("while"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__71"

    // $ANTLR start "T__72"
    public final void mT__72() throws RecognitionException {
        try {
            int _type = T__72;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:69:7: ( 'do' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:69:9: 'do'
            {
            match("do"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__72"

    // $ANTLR start "T__73"
    public final void mT__73() throws RecognitionException {
        try {
            int _type = T__73;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:70:7: ( 'new' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:70:9: 'new'
            {
            match("new"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__73"

    // $ANTLR start "T__74"
    public final void mT__74() throws RecognitionException {
        try {
            int _type = T__74;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:71:7: ( 'null' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:71:9: 'null'
            {
            match("null"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__74"

    // $ANTLR start "T__75"
    public final void mT__75() throws RecognitionException {
        try {
            int _type = T__75;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:72:7: ( 'typeof' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:72:9: 'typeof'
            {
            match("typeof"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__75"

    // $ANTLR start "T__76"
    public final void mT__76() throws RecognitionException {
        try {
            int _type = T__76;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:73:7: ( 'throw' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:73:9: 'throw'
            {
            match("throw"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__76"

    // $ANTLR start "T__77"
    public final void mT__77() throws RecognitionException {
        try {
            int _type = T__77;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:74:7: ( 'return' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:74:9: 'return'
            {
            match("return"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__77"

    // $ANTLR start "T__78"
    public final void mT__78() throws RecognitionException {
        try {
            int _type = T__78;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:75:7: ( 'try' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:75:9: 'try'
            {
            match("try"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__78"

    // $ANTLR start "T__79"
    public final void mT__79() throws RecognitionException {
        try {
            int _type = T__79;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:76:7: ( 'finally' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:76:9: 'finally'
            {
            match("finally"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__79"

    // $ANTLR start "T__80"
    public final void mT__80() throws RecognitionException {
        try {
            int _type = T__80;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:77:7: ( 'synchronized' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:77:9: 'synchronized'
            {
            match("synchronized"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__80"

    // $ANTLR start "T__81"
    public final void mT__81() throws RecognitionException {
        try {
            int _type = T__81;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:78:7: ( 'catch' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:78:9: 'catch'
            {
            match("catch"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__81"

    // $ANTLR start "T__82"
    public final void mT__82() throws RecognitionException {
        try {
            int _type = T__82;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:79:7: ( '?' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:79:9: '?'
            {
            match('?'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__82"

    // $ANTLR start "T__83"
    public final void mT__83() throws RecognitionException {
        try {
            int _type = T__83;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:80:7: ( '&' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:80:9: '&'
            {
            match('&'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__83"

    // $ANTLR start "T__84"
    public final void mT__84() throws RecognitionException {
        try {
            int _type = T__84;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:81:7: ( '::' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:81:9: '::'
            {
            match("::"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__84"

    // $ANTLR start "T__85"
    public final void mT__85() throws RecognitionException {
        try {
            int _type = T__85;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:82:7: ( '?.' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:82:9: '?.'
            {
            match("?."); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__85"

    // $ANTLR start "T__86"
    public final void mT__86() throws RecognitionException {
        try {
            int _type = T__86;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:83:7: ( '|' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:83:9: '|'
            {
            match('|'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__86"

    // $ANTLR start "T__87"
    public final void mT__87() throws RecognitionException {
        try {
            int _type = T__87;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:84:7: ( 'var' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:84:9: 'var'
            {
            match("var"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__87"

    // $ANTLR start "T__88"
    public final void mT__88() throws RecognitionException {
        try {
            int _type = T__88;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:85:7: ( 'true' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:85:9: 'true'
            {
            match("true"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__88"

    // $ANTLR start "RULE_LOCALE"
    public final void mRULE_LOCALE() throws RecognitionException {
        try {
            int _type = RULE_LOCALE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19681:13: ( 'a' .. 'z' 'a' .. 'z' ( '-' 'A' .. 'Z' 'A' .. 'Z' )? )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19681:15: 'a' .. 'z' 'a' .. 'z' ( '-' 'A' .. 'Z' 'A' .. 'Z' )?
            {
            matchRange('a','z'); 
            matchRange('a','z'); 
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19681:33: ( '-' 'A' .. 'Z' 'A' .. 'Z' )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0=='-') ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19681:34: '-' 'A' .. 'Z' 'A' .. 'Z'
                    {
                    match('-'); 
                    matchRange('A','Z'); 
                    matchRange('A','Z'); 

                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_LOCALE"

    // $ANTLR start "RULE_HEX"
    public final void mRULE_HEX() throws RecognitionException {
        try {
            int _type = RULE_HEX;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19683:10: ( ( '0x' | '0X' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' | '_' )+ ( '#' ( ( 'b' | 'B' ) ( 'i' | 'I' ) | ( 'l' | 'L' ) ) )? )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19683:12: ( '0x' | '0X' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' | '_' )+ ( '#' ( ( 'b' | 'B' ) ( 'i' | 'I' ) | ( 'l' | 'L' ) ) )?
            {
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19683:12: ( '0x' | '0X' )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0=='0') ) {
                int LA2_1 = input.LA(2);

                if ( (LA2_1=='x') ) {
                    alt2=1;
                }
                else if ( (LA2_1=='X') ) {
                    alt2=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 2, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19683:13: '0x'
                    {
                    match("0x"); 


                    }
                    break;
                case 2 :
                    // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19683:18: '0X'
                    {
                    match("0X"); 


                    }
                    break;

            }

            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19683:24: ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' | '_' )+
            int cnt3=0;
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='0' && LA3_0<='9')||(LA3_0>='A' && LA3_0<='F')||LA3_0=='_'||(LA3_0>='a' && LA3_0<='f')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='F')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='f') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt3 >= 1 ) break loop3;
                        EarlyExitException eee =
                            new EarlyExitException(3, input);
                        throw eee;
                }
                cnt3++;
            } while (true);

            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19683:58: ( '#' ( ( 'b' | 'B' ) ( 'i' | 'I' ) | ( 'l' | 'L' ) ) )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0=='#') ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19683:59: '#' ( ( 'b' | 'B' ) ( 'i' | 'I' ) | ( 'l' | 'L' ) )
                    {
                    match('#'); 
                    // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19683:63: ( ( 'b' | 'B' ) ( 'i' | 'I' ) | ( 'l' | 'L' ) )
                    int alt4=2;
                    int LA4_0 = input.LA(1);

                    if ( (LA4_0=='B'||LA4_0=='b') ) {
                        alt4=1;
                    }
                    else if ( (LA4_0=='L'||LA4_0=='l') ) {
                        alt4=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 4, 0, input);

                        throw nvae;
                    }
                    switch (alt4) {
                        case 1 :
                            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19683:64: ( 'b' | 'B' ) ( 'i' | 'I' )
                            {
                            if ( input.LA(1)=='B'||input.LA(1)=='b' ) {
                                input.consume();

                            }
                            else {
                                MismatchedSetException mse = new MismatchedSetException(null,input);
                                recover(mse);
                                throw mse;}

                            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                                input.consume();

                            }
                            else {
                                MismatchedSetException mse = new MismatchedSetException(null,input);
                                recover(mse);
                                throw mse;}


                            }
                            break;
                        case 2 :
                            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19683:84: ( 'l' | 'L' )
                            {
                            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                                input.consume();

                            }
                            else {
                                MismatchedSetException mse = new MismatchedSetException(null,input);
                                recover(mse);
                                throw mse;}


                            }
                            break;

                    }


                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_HEX"

    // $ANTLR start "RULE_INT"
    public final void mRULE_INT() throws RecognitionException {
        try {
            int _type = RULE_INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19685:10: ( '0' .. '9' ( '0' .. '9' | '_' )* )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19685:12: '0' .. '9' ( '0' .. '9' | '_' )*
            {
            matchRange('0','9'); 
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19685:21: ( '0' .. '9' | '_' )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( ((LA6_0>='0' && LA6_0<='9')||LA6_0=='_') ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||input.LA(1)=='_' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_INT"

    // $ANTLR start "RULE_DECIMAL"
    public final void mRULE_DECIMAL() throws RecognitionException {
        try {
            int _type = RULE_DECIMAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19687:14: ( RULE_INT ( ( 'e' | 'E' ) ( '+' | '-' )? RULE_INT )? ( ( 'b' | 'B' ) ( 'i' | 'I' | 'd' | 'D' ) | ( 'l' | 'L' | 'd' | 'D' | 'f' | 'F' ) )? )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19687:16: RULE_INT ( ( 'e' | 'E' ) ( '+' | '-' )? RULE_INT )? ( ( 'b' | 'B' ) ( 'i' | 'I' | 'd' | 'D' ) | ( 'l' | 'L' | 'd' | 'D' | 'f' | 'F' ) )?
            {
            mRULE_INT(); 
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19687:25: ( ( 'e' | 'E' ) ( '+' | '-' )? RULE_INT )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0=='E'||LA8_0=='e') ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19687:26: ( 'e' | 'E' ) ( '+' | '-' )? RULE_INT
                    {
                    if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;}

                    // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19687:36: ( '+' | '-' )?
                    int alt7=2;
                    int LA7_0 = input.LA(1);

                    if ( (LA7_0=='+'||LA7_0=='-') ) {
                        alt7=1;
                    }
                    switch (alt7) {
                        case 1 :
                            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:
                            {
                            if ( input.LA(1)=='+'||input.LA(1)=='-' ) {
                                input.consume();

                            }
                            else {
                                MismatchedSetException mse = new MismatchedSetException(null,input);
                                recover(mse);
                                throw mse;}


                            }
                            break;

                    }

                    mRULE_INT(); 

                    }
                    break;

            }

            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19687:58: ( ( 'b' | 'B' ) ( 'i' | 'I' | 'd' | 'D' ) | ( 'l' | 'L' | 'd' | 'D' | 'f' | 'F' ) )?
            int alt9=3;
            int LA9_0 = input.LA(1);

            if ( (LA9_0=='B'||LA9_0=='b') ) {
                alt9=1;
            }
            else if ( (LA9_0=='D'||LA9_0=='F'||LA9_0=='L'||LA9_0=='d'||LA9_0=='f'||LA9_0=='l') ) {
                alt9=2;
            }
            switch (alt9) {
                case 1 :
                    // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19687:59: ( 'b' | 'B' ) ( 'i' | 'I' | 'd' | 'D' )
                    {
                    if ( input.LA(1)=='B'||input.LA(1)=='b' ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;}

                    if ( input.LA(1)=='D'||input.LA(1)=='I'||input.LA(1)=='d'||input.LA(1)=='i' ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;}


                    }
                    break;
                case 2 :
                    // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19687:87: ( 'l' | 'L' | 'd' | 'D' | 'f' | 'F' )
                    {
                    if ( input.LA(1)=='D'||input.LA(1)=='F'||input.LA(1)=='L'||input.LA(1)=='d'||input.LA(1)=='f'||input.LA(1)=='l' ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;}


                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_DECIMAL"

    // $ANTLR start "RULE_ID"
    public final void mRULE_ID() throws RecognitionException {
        try {
            int _type = RULE_ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19689:9: ( ( '^' )? ( 'a' .. 'z' | 'A' .. 'Z' | '$' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '$' | '_' | '0' .. '9' )* )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19689:11: ( '^' )? ( 'a' .. 'z' | 'A' .. 'Z' | '$' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '$' | '_' | '0' .. '9' )*
            {
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19689:11: ( '^' )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0=='^') ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19689:11: '^'
                    {
                    match('^'); 

                    }
                    break;

            }

            if ( input.LA(1)=='$'||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19689:44: ( 'a' .. 'z' | 'A' .. 'Z' | '$' | '_' | '0' .. '9' )*
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( (LA11_0=='$'||(LA11_0>='0' && LA11_0<='9')||(LA11_0>='A' && LA11_0<='Z')||LA11_0=='_'||(LA11_0>='a' && LA11_0<='z')) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:
            	    {
            	    if ( input.LA(1)=='$'||(input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop11;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_ID"

    // $ANTLR start "RULE_STRING"
    public final void mRULE_STRING() throws RecognitionException {
        try {
            int _type = RULE_STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19691:13: ( ( '\"' ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )* ( '\"' )? | '\\'' ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )* ( '\\'' )? ) )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19691:15: ( '\"' ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )* ( '\"' )? | '\\'' ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )* ( '\\'' )? )
            {
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19691:15: ( '\"' ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )* ( '\"' )? | '\\'' ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )* ( '\\'' )? )
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0=='\"') ) {
                alt16=1;
            }
            else if ( (LA16_0=='\'') ) {
                alt16=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                throw nvae;
            }
            switch (alt16) {
                case 1 :
                    // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19691:16: '\"' ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )* ( '\"' )?
                    {
                    match('\"'); 
                    // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19691:20: ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )*
                    loop12:
                    do {
                        int alt12=3;
                        int LA12_0 = input.LA(1);

                        if ( (LA12_0=='\\') ) {
                            alt12=1;
                        }
                        else if ( ((LA12_0>='\u0000' && LA12_0<='!')||(LA12_0>='#' && LA12_0<='[')||(LA12_0>=']' && LA12_0<='\uFFFF')) ) {
                            alt12=2;
                        }


                        switch (alt12) {
                    	case 1 :
                    	    // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19691:21: '\\\\' .
                    	    {
                    	    match('\\'); 
                    	    matchAny(); 

                    	    }
                    	    break;
                    	case 2 :
                    	    // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19691:28: ~ ( ( '\\\\' | '\"' ) )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFF') ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;}


                    	    }
                    	    break;

                    	default :
                    	    break loop12;
                        }
                    } while (true);

                    // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19691:44: ( '\"' )?
                    int alt13=2;
                    int LA13_0 = input.LA(1);

                    if ( (LA13_0=='\"') ) {
                        alt13=1;
                    }
                    switch (alt13) {
                        case 1 :
                            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19691:44: '\"'
                            {
                            match('\"'); 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19691:49: '\\'' ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )* ( '\\'' )?
                    {
                    match('\''); 
                    // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19691:54: ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )*
                    loop14:
                    do {
                        int alt14=3;
                        int LA14_0 = input.LA(1);

                        if ( (LA14_0=='\\') ) {
                            alt14=1;
                        }
                        else if ( ((LA14_0>='\u0000' && LA14_0<='&')||(LA14_0>='(' && LA14_0<='[')||(LA14_0>=']' && LA14_0<='\uFFFF')) ) {
                            alt14=2;
                        }


                        switch (alt14) {
                    	case 1 :
                    	    // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19691:55: '\\\\' .
                    	    {
                    	    match('\\'); 
                    	    matchAny(); 

                    	    }
                    	    break;
                    	case 2 :
                    	    // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19691:62: ~ ( ( '\\\\' | '\\'' ) )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='&')||(input.LA(1)>='(' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFF') ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;}


                    	    }
                    	    break;

                    	default :
                    	    break loop14;
                        }
                    } while (true);

                    // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19691:79: ( '\\'' )?
                    int alt15=2;
                    int LA15_0 = input.LA(1);

                    if ( (LA15_0=='\'') ) {
                        alt15=1;
                    }
                    switch (alt15) {
                        case 1 :
                            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19691:79: '\\''
                            {
                            match('\''); 

                            }
                            break;

                    }


                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_STRING"

    // $ANTLR start "RULE_ML_COMMENT"
    public final void mRULE_ML_COMMENT() throws RecognitionException {
        try {
            int _type = RULE_ML_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19693:17: ( '/*' ( options {greedy=false; } : . )* '*/' )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19693:19: '/*' ( options {greedy=false; } : . )* '*/'
            {
            match("/*"); 

            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19693:24: ( options {greedy=false; } : . )*
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( (LA17_0=='*') ) {
                    int LA17_1 = input.LA(2);

                    if ( (LA17_1=='/') ) {
                        alt17=2;
                    }
                    else if ( ((LA17_1>='\u0000' && LA17_1<='.')||(LA17_1>='0' && LA17_1<='\uFFFF')) ) {
                        alt17=1;
                    }


                }
                else if ( ((LA17_0>='\u0000' && LA17_0<=')')||(LA17_0>='+' && LA17_0<='\uFFFF')) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19693:52: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop17;
                }
            } while (true);

            match("*/"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_ML_COMMENT"

    // $ANTLR start "RULE_SL_COMMENT"
    public final void mRULE_SL_COMMENT() throws RecognitionException {
        try {
            int _type = RULE_SL_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19695:17: ( '//' (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )? )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19695:19: '//' (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )?
            {
            match("//"); 

            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19695:24: (~ ( ( '\\n' | '\\r' ) ) )*
            loop18:
            do {
                int alt18=2;
                int LA18_0 = input.LA(1);

                if ( ((LA18_0>='\u0000' && LA18_0<='\t')||(LA18_0>='\u000B' && LA18_0<='\f')||(LA18_0>='\u000E' && LA18_0<='\uFFFF')) ) {
                    alt18=1;
                }


                switch (alt18) {
            	case 1 :
            	    // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19695:24: ~ ( ( '\\n' | '\\r' ) )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop18;
                }
            } while (true);

            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19695:40: ( ( '\\r' )? '\\n' )?
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0=='\n'||LA20_0=='\r') ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19695:41: ( '\\r' )? '\\n'
                    {
                    // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19695:41: ( '\\r' )?
                    int alt19=2;
                    int LA19_0 = input.LA(1);

                    if ( (LA19_0=='\r') ) {
                        alt19=1;
                    }
                    switch (alt19) {
                        case 1 :
                            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19695:41: '\\r'
                            {
                            match('\r'); 

                            }
                            break;

                    }

                    match('\n'); 

                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_SL_COMMENT"

    // $ANTLR start "RULE_WS"
    public final void mRULE_WS() throws RecognitionException {
        try {
            int _type = RULE_WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19697:9: ( ( ' ' | '\\t' | '\\r' | '\\n' )+ )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19697:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
            {
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19697:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
            int cnt21=0;
            loop21:
            do {
                int alt21=2;
                int LA21_0 = input.LA(1);

                if ( ((LA21_0>='\t' && LA21_0<='\n')||LA21_0=='\r'||LA21_0==' ') ) {
                    alt21=1;
                }


                switch (alt21) {
            	case 1 :
            	    // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:
            	    {
            	    if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt21 >= 1 ) break loop21;
                        EarlyExitException eee =
                            new EarlyExitException(21, input);
                        throw eee;
                }
                cnt21++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_WS"

    // $ANTLR start "RULE_ANY_OTHER"
    public final void mRULE_ANY_OTHER() throws RecognitionException {
        try {
            int _type = RULE_ANY_OTHER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19699:16: ( . )
            // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:19699:18: .
            {
            matchAny(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_ANY_OTHER"

    public void mTokens() throws RecognitionException {
        // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:8: ( T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | T__47 | T__48 | T__49 | T__50 | T__51 | T__52 | T__53 | T__54 | T__55 | T__56 | T__57 | T__58 | T__59 | T__60 | T__61 | T__62 | T__63 | T__64 | T__65 | T__66 | T__67 | T__68 | T__69 | T__70 | T__71 | T__72 | T__73 | T__74 | T__75 | T__76 | T__77 | T__78 | T__79 | T__80 | T__81 | T__82 | T__83 | T__84 | T__85 | T__86 | T__87 | T__88 | RULE_LOCALE | RULE_HEX | RULE_INT | RULE_DECIMAL | RULE_ID | RULE_STRING | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS | RULE_ANY_OTHER )
        int alt22=85;
        alt22 = dfa22.predict(input);
        switch (alt22) {
            case 1 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:10: T__14
                {
                mT__14(); 

                }
                break;
            case 2 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:16: T__15
                {
                mT__15(); 

                }
                break;
            case 3 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:22: T__16
                {
                mT__16(); 

                }
                break;
            case 4 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:28: T__17
                {
                mT__17(); 

                }
                break;
            case 5 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:34: T__18
                {
                mT__18(); 

                }
                break;
            case 6 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:40: T__19
                {
                mT__19(); 

                }
                break;
            case 7 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:46: T__20
                {
                mT__20(); 

                }
                break;
            case 8 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:52: T__21
                {
                mT__21(); 

                }
                break;
            case 9 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:58: T__22
                {
                mT__22(); 

                }
                break;
            case 10 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:64: T__23
                {
                mT__23(); 

                }
                break;
            case 11 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:70: T__24
                {
                mT__24(); 

                }
                break;
            case 12 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:76: T__25
                {
                mT__25(); 

                }
                break;
            case 13 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:82: T__26
                {
                mT__26(); 

                }
                break;
            case 14 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:88: T__27
                {
                mT__27(); 

                }
                break;
            case 15 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:94: T__28
                {
                mT__28(); 

                }
                break;
            case 16 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:100: T__29
                {
                mT__29(); 

                }
                break;
            case 17 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:106: T__30
                {
                mT__30(); 

                }
                break;
            case 18 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:112: T__31
                {
                mT__31(); 

                }
                break;
            case 19 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:118: T__32
                {
                mT__32(); 

                }
                break;
            case 20 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:124: T__33
                {
                mT__33(); 

                }
                break;
            case 21 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:130: T__34
                {
                mT__34(); 

                }
                break;
            case 22 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:136: T__35
                {
                mT__35(); 

                }
                break;
            case 23 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:142: T__36
                {
                mT__36(); 

                }
                break;
            case 24 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:148: T__37
                {
                mT__37(); 

                }
                break;
            case 25 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:154: T__38
                {
                mT__38(); 

                }
                break;
            case 26 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:160: T__39
                {
                mT__39(); 

                }
                break;
            case 27 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:166: T__40
                {
                mT__40(); 

                }
                break;
            case 28 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:172: T__41
                {
                mT__41(); 

                }
                break;
            case 29 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:178: T__42
                {
                mT__42(); 

                }
                break;
            case 30 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:184: T__43
                {
                mT__43(); 

                }
                break;
            case 31 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:190: T__44
                {
                mT__44(); 

                }
                break;
            case 32 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:196: T__45
                {
                mT__45(); 

                }
                break;
            case 33 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:202: T__46
                {
                mT__46(); 

                }
                break;
            case 34 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:208: T__47
                {
                mT__47(); 

                }
                break;
            case 35 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:214: T__48
                {
                mT__48(); 

                }
                break;
            case 36 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:220: T__49
                {
                mT__49(); 

                }
                break;
            case 37 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:226: T__50
                {
                mT__50(); 

                }
                break;
            case 38 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:232: T__51
                {
                mT__51(); 

                }
                break;
            case 39 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:238: T__52
                {
                mT__52(); 

                }
                break;
            case 40 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:244: T__53
                {
                mT__53(); 

                }
                break;
            case 41 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:250: T__54
                {
                mT__54(); 

                }
                break;
            case 42 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:256: T__55
                {
                mT__55(); 

                }
                break;
            case 43 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:262: T__56
                {
                mT__56(); 

                }
                break;
            case 44 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:268: T__57
                {
                mT__57(); 

                }
                break;
            case 45 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:274: T__58
                {
                mT__58(); 

                }
                break;
            case 46 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:280: T__59
                {
                mT__59(); 

                }
                break;
            case 47 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:286: T__60
                {
                mT__60(); 

                }
                break;
            case 48 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:292: T__61
                {
                mT__61(); 

                }
                break;
            case 49 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:298: T__62
                {
                mT__62(); 

                }
                break;
            case 50 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:304: T__63
                {
                mT__63(); 

                }
                break;
            case 51 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:310: T__64
                {
                mT__64(); 

                }
                break;
            case 52 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:316: T__65
                {
                mT__65(); 

                }
                break;
            case 53 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:322: T__66
                {
                mT__66(); 

                }
                break;
            case 54 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:328: T__67
                {
                mT__67(); 

                }
                break;
            case 55 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:334: T__68
                {
                mT__68(); 

                }
                break;
            case 56 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:340: T__69
                {
                mT__69(); 

                }
                break;
            case 57 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:346: T__70
                {
                mT__70(); 

                }
                break;
            case 58 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:352: T__71
                {
                mT__71(); 

                }
                break;
            case 59 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:358: T__72
                {
                mT__72(); 

                }
                break;
            case 60 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:364: T__73
                {
                mT__73(); 

                }
                break;
            case 61 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:370: T__74
                {
                mT__74(); 

                }
                break;
            case 62 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:376: T__75
                {
                mT__75(); 

                }
                break;
            case 63 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:382: T__76
                {
                mT__76(); 

                }
                break;
            case 64 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:388: T__77
                {
                mT__77(); 

                }
                break;
            case 65 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:394: T__78
                {
                mT__78(); 

                }
                break;
            case 66 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:400: T__79
                {
                mT__79(); 

                }
                break;
            case 67 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:406: T__80
                {
                mT__80(); 

                }
                break;
            case 68 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:412: T__81
                {
                mT__81(); 

                }
                break;
            case 69 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:418: T__82
                {
                mT__82(); 

                }
                break;
            case 70 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:424: T__83
                {
                mT__83(); 

                }
                break;
            case 71 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:430: T__84
                {
                mT__84(); 

                }
                break;
            case 72 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:436: T__85
                {
                mT__85(); 

                }
                break;
            case 73 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:442: T__86
                {
                mT__86(); 

                }
                break;
            case 74 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:448: T__87
                {
                mT__87(); 

                }
                break;
            case 75 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:454: T__88
                {
                mT__88(); 

                }
                break;
            case 76 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:460: RULE_LOCALE
                {
                mRULE_LOCALE(); 

                }
                break;
            case 77 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:472: RULE_HEX
                {
                mRULE_HEX(); 

                }
                break;
            case 78 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:481: RULE_INT
                {
                mRULE_INT(); 

                }
                break;
            case 79 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:490: RULE_DECIMAL
                {
                mRULE_DECIMAL(); 

                }
                break;
            case 80 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:503: RULE_ID
                {
                mRULE_ID(); 

                }
                break;
            case 81 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:511: RULE_STRING
                {
                mRULE_STRING(); 

                }
                break;
            case 82 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:523: RULE_ML_COMMENT
                {
                mRULE_ML_COMMENT(); 

                }
                break;
            case 83 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:539: RULE_SL_COMMENT
                {
                mRULE_SL_COMMENT(); 

                }
                break;
            case 84 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:555: RULE_WS
                {
                mRULE_WS(); 

                }
                break;
            case 85 :
                // ../org.monet.editor.dsl.ui/src-gen/org/monet/editor/dsl/ui/contentassist/antlr/internal/InternalMonetLocalizationLanguage.g:1:563: RULE_ANY_OTHER
                {
                mRULE_ANY_OTHER(); 

                }
                break;

        }

    }


    protected DFA22 dfa22 = new DFA22(this);
    static final String DFA22_eotS =
        "\1\uffff\1\60\1\62\1\64\1\70\1\73\1\77\1\102\1\106\1\110\1\112\1\114\1\116\1\120\1\123\6\70\2\uffff\1\145\1\uffff\1\70\6\uffff\6\70\2\170\1\55\5\uffff\1\175\6\uffff\1\177\1\u0080\1\177\21\uffff\1\u0082\5\uffff\1\u0084\4\uffff\11\177\1\u008f\4\177\5\uffff\1\u0094\6\uffff\10\177\1\uffff\1\170\6\uffff\1\70\6\uffff\1\u00a0\1\u00a1\10\70\1\uffff\1\70\1\u00ab\2\70\1\uffff\3\70\1\u00b1\3\70\1\u00b5\3\70\2\uffff\1\70\1\u00ba\7\70\1\uffff\2\70\1\u00c4\2\70\1\uffff\1\u00c7\2\70\1\uffff\1\u00ca\3\70\1\uffff\1\70\1\u00d0\4\70\1\u00d5\2\70\1\uffff\1\u00d8\1\u00d9\1\uffff\1\70\1\u00db\1\uffff\4\70\1\u00e0\1\uffff\1\u00e1\1\70\1\u00e3\1\70\1\uffff\2\70\2\uffff\1\u00e7\1\uffff\1\u00e8\1\u00e9\1\u00ea\1\70\2\uffff\1\70\1\uffff\1\70\1\u00ee\1\70\4\uffff\3\70\1\uffff\1\u00f3\1\u00f4\2\70\2\uffff\1\70\1\u00f8\1\70\1\uffff\1\u00fa\1\uffff";
    static final String DFA22_eofS =
        "\u00fb\uffff";
    static final String DFA22_minS =
        "\1\0\1\75\1\174\1\46\1\141\1\53\1\55\2\52\3\75\1\76\2\56\6\141\2\uffff\1\72\1\uffff\1\141\6\uffff\6\141\2\60\1\44\5\uffff\1\75\6\uffff\3\44\21\uffff\1\75\5\uffff\1\74\4\uffff\16\44\5\uffff\1\44\6\uffff\10\44\1\uffff\1\60\6\uffff\1\141\6\uffff\2\44\2\145\1\164\1\145\1\164\1\143\1\157\1\164\1\uffff\1\163\1\44\1\141\1\147\1\uffff\1\145\1\143\1\154\1\44\1\154\1\145\1\157\1\44\1\145\2\165\2\uffff\1\156\1\44\1\151\1\162\1\143\1\150\1\162\1\141\1\145\1\uffff\1\154\1\165\1\44\1\150\1\145\1\uffff\1\44\1\157\1\167\1\uffff\1\44\1\162\1\154\1\144\1\uffff\1\143\1\44\1\150\1\162\1\164\1\156\1\44\1\154\1\141\1\uffff\2\44\1\uffff\1\146\1\44\1\uffff\1\156\1\164\1\163\1\151\1\44\1\uffff\1\44\1\157\1\44\1\143\1\uffff\1\171\1\147\2\uffff\1\44\1\uffff\3\44\1\157\2\uffff\1\156\1\uffff\1\145\1\44\1\145\4\uffff\1\156\1\151\1\157\1\uffff\2\44\1\172\1\146\2\uffff\1\145\1\44\1\144\1\uffff\1\44\1\uffff";
    static final String DFA22_maxS =
        "\1\uffff\1\76\1\174\1\46\1\172\1\75\1\76\5\75\1\76\1\56\1\72\6\172\2\uffff\1\72\1\uffff\1\172\6\uffff\6\172\1\170\1\154\1\172\5\uffff\1\75\6\uffff\3\172\21\uffff\1\75\5\uffff\1\74\4\uffff\16\172\5\uffff\1\172\6\uffff\10\172\1\uffff\1\154\6\uffff\1\141\6\uffff\2\172\2\145\1\164\1\145\1\164\1\143\1\157\1\164\1\uffff\1\163\1\172\1\141\1\147\1\uffff\1\145\1\143\1\154\1\172\1\154\1\145\1\157\1\172\1\145\2\165\2\uffff\1\156\1\172\1\151\1\162\1\143\1\150\1\162\1\141\1\145\1\uffff\1\154\1\165\1\172\1\150\1\145\1\uffff\1\172\1\157\1\167\1\uffff\1\172\1\162\1\154\1\163\1\uffff\1\143\1\172\1\150\1\162\1\164\1\156\1\172\1\154\1\141\1\uffff\2\172\1\uffff\1\146\1\172\1\uffff\1\156\1\164\1\163\1\151\1\172\1\uffff\1\172\1\157\1\172\1\143\1\uffff\1\171\1\147\2\uffff\1\172\1\uffff\3\172\1\157\2\uffff\1\156\1\uffff\1\145\1\172\1\145\4\uffff\1\156\1\151\1\157\1\uffff\3\172\1\146\2\uffff\1\145\1\172\1\144\1\uffff\1\172\1\uffff";
    static final String DFA22_acceptS =
        "\25\uffff\1\51\1\52\1\uffff\1\54\1\uffff\1\57\1\60\1\61\1\62\1\63\1\64\11\uffff\1\120\2\121\1\124\1\125\1\uffff\1\24\1\1\1\2\1\111\1\3\1\106\3\uffff\1\120\1\5\1\36\1\27\1\6\1\21\1\37\1\30\1\7\1\32\1\31\1\10\1\122\1\123\1\33\1\11\1\34\1\uffff\1\35\1\16\1\17\1\25\1\20\1\uffff\1\40\1\26\1\110\1\105\16\uffff\1\51\1\52\1\107\1\53\1\54\1\uffff\1\57\1\60\1\61\1\62\1\63\1\64\10\uffff\1\115\1\uffff\1\116\1\117\1\121\1\124\1\14\1\12\1\uffff\1\114\1\73\1\15\1\13\1\22\1\23\12\uffff\1\65\4\uffff\1\56\13\uffff\1\41\1\112\11\uffff\1\71\5\uffff\1\74\3\uffff\1\101\4\uffff\1\66\11\uffff\1\70\2\uffff\1\75\2\uffff\1\113\5\uffff\1\46\4\uffff\1\47\2\uffff\1\104\1\72\1\uffff\1\77\4\uffff\1\43\1\67\1\uffff\1\44\3\uffff\1\76\1\100\1\4\1\42\3\uffff\1\102\4\uffff\1\50\1\45\3\uffff\1\55\1\uffff\1\103";
    static final String DFA22_specialS =
        "\1\0\u00fa\uffff}>";
    static final String[] DFA22_transitionS = {
            "\11\55\2\54\2\55\1\54\22\55\1\54\1\12\1\52\1\34\1\51\1\11\1\3\1\53\1\37\1\33\1\7\1\5\1\32\1\6\1\15\1\10\1\46\11\47\1\27\1\30\1\14\1\1\1\13\1\16\1\55\32\51\1\35\1\55\1\36\1\50\1\51\1\55\1\31\1\45\1\40\1\4\1\20\1\23\2\45\1\22\2\45\1\24\1\45\1\42\3\45\1\44\1\21\1\43\1\45\1\17\1\41\3\45\1\25\1\2\1\26\uff82\55",
            "\1\56\1\57",
            "\1\61",
            "\1\63",
            "\4\67\1\65\11\67\1\66\13\67",
            "\1\72\21\uffff\1\71",
            "\1\76\17\uffff\1\74\1\75",
            "\1\101\22\uffff\1\100",
            "\1\104\4\uffff\1\105\15\uffff\1\103",
            "\1\107",
            "\1\111",
            "\1\113",
            "\1\115",
            "\1\117",
            "\1\122\13\uffff\1\121",
            "\1\124\31\67",
            "\13\67\1\126\13\67\1\125\2\67",
            "\23\67\1\127\1\130\1\67\1\131\1\67\1\132\1\67",
            "\5\67\1\135\6\67\1\133\1\134\14\67",
            "\1\136\7\67\1\140\5\67\1\137\13\67",
            "\1\141\31\67",
            "",
            "",
            "\1\144",
            "",
            "\22\67\1\147\7\67",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\156\31\67",
            "\7\67\1\157\22\67",
            "\4\67\1\160\17\67\1\161\5\67",
            "\7\67\1\163\11\67\1\164\6\67\1\162\1\67",
            "\4\67\1\165\25\67",
            "\32\67",
            "\12\167\10\uffff\1\171\1\uffff\3\171\5\uffff\1\171\13\uffff\1\166\6\uffff\1\167\2\uffff\1\171\1\uffff\3\171\5\uffff\1\171\13\uffff\1\166",
            "\12\167\10\uffff\1\171\1\uffff\3\171\5\uffff\1\171\22\uffff\1\167\2\uffff\1\171\1\uffff\3\171\5\uffff\1\171",
            "\1\70\34\uffff\32\70\4\uffff\1\70\1\uffff\32\70",
            "",
            "",
            "",
            "",
            "",
            "\1\174",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\5\70\1\176\24\70",
            "\1\70\10\uffff\1\177\2\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\32\70",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\32\70",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\u0081",
            "",
            "",
            "",
            "",
            "",
            "\1\u0083",
            "",
            "",
            "",
            "",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\13\70\1\u0085\5\70\1\u0086\10\70",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\23\70\1\u0087\6\70",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\22\70\1\u0088\7\70",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\1\u0089\31\70",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\17\70\1\u008a\12\70",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\10\70\1\u008b\21\70",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\15\70\1\u008c\14\70",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\17\70\1\u008d\12\70",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\22\70\1\u008e\7\70",
            "\1\70\10\uffff\1\177\2\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\32\70",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\13\70\1\u0090\16\70",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\21\70\1\u0091\10\70",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\15\70\1\u0092\14\70",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\15\70\1\u0093\14\70",
            "",
            "",
            "",
            "",
            "",
            "\1\70\10\uffff\1\177\2\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\32\70",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\22\70\1\u0095\1\u0096\6\70",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\10\70\1\u0097\21\70",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\26\70\1\u0098\3\70",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\13\70\1\u0099\16\70",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\17\70\1\u009a\12\70",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\21\70\1\u009b\10\70",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\24\70\1\u009d\3\70\1\u009c\1\70",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\23\70\1\u009e\6\70",
            "",
            "\12\167\10\uffff\1\171\1\uffff\3\171\5\uffff\1\171\22\uffff\1\167\2\uffff\1\171\1\uffff\3\171\5\uffff\1\171",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\u009f",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\32\70",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\32\70",
            "\1\u00a2",
            "\1\u00a3",
            "\1\u00a4",
            "\1\u00a5",
            "\1\u00a6",
            "\1\u00a7",
            "\1\u00a8",
            "\1\u00a9",
            "",
            "\1\u00aa",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\32\70",
            "\1\u00ac",
            "\1\u00ad",
            "",
            "\1\u00ae",
            "\1\u00af",
            "\1\u00b0",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\32\70",
            "\1\u00b2",
            "\1\u00b3",
            "\1\u00b4",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\32\70",
            "\1\u00b6",
            "\1\u00b7",
            "\1\u00b8",
            "",
            "",
            "\1\u00b9",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\32\70",
            "\1\u00bb",
            "\1\u00bc",
            "\1\u00bd",
            "\1\u00be",
            "\1\u00bf",
            "\1\u00c0",
            "\1\u00c1",
            "",
            "\1\u00c2",
            "\1\u00c3",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\32\70",
            "\1\u00c5",
            "\1\u00c6",
            "",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\32\70",
            "\1\u00c8",
            "\1\u00c9",
            "",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\32\70",
            "\1\u00cb",
            "\1\u00cc",
            "\1\u00cd\16\uffff\1\u00ce",
            "",
            "\1\u00cf",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\32\70",
            "\1\u00d1",
            "\1\u00d2",
            "\1\u00d3",
            "\1\u00d4",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\32\70",
            "\1\u00d6",
            "\1\u00d7",
            "",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\32\70",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\32\70",
            "",
            "\1\u00da",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\32\70",
            "",
            "\1\u00dc",
            "\1\u00dd",
            "\1\u00de",
            "\1\u00df",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\32\70",
            "",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\32\70",
            "\1\u00e2",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\32\70",
            "\1\u00e4",
            "",
            "\1\u00e5",
            "\1\u00e6",
            "",
            "",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\32\70",
            "",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\32\70",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\32\70",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\32\70",
            "\1\u00eb",
            "",
            "",
            "\1\u00ec",
            "",
            "\1\u00ed",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\32\70",
            "\1\u00ef",
            "",
            "",
            "",
            "",
            "\1\u00f0",
            "\1\u00f1",
            "\1\u00f2",
            "",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\32\70",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\32\70",
            "\1\u00f5",
            "\1\u00f6",
            "",
            "",
            "\1\u00f7",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\32\70",
            "\1\u00f9",
            "",
            "\1\70\13\uffff\12\70\7\uffff\32\70\4\uffff\1\70\1\uffff\32\70",
            ""
    };

    static final short[] DFA22_eot = DFA.unpackEncodedString(DFA22_eotS);
    static final short[] DFA22_eof = DFA.unpackEncodedString(DFA22_eofS);
    static final char[] DFA22_min = DFA.unpackEncodedStringToUnsignedChars(DFA22_minS);
    static final char[] DFA22_max = DFA.unpackEncodedStringToUnsignedChars(DFA22_maxS);
    static final short[] DFA22_accept = DFA.unpackEncodedString(DFA22_acceptS);
    static final short[] DFA22_special = DFA.unpackEncodedString(DFA22_specialS);
    static final short[][] DFA22_transition;

    static {
        int numStates = DFA22_transitionS.length;
        DFA22_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA22_transition[i] = DFA.unpackEncodedString(DFA22_transitionS[i]);
        }
    }

    static class DFA22 extends DFA {

        public DFA22(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 22;
            this.eot = DFA22_eot;
            this.eof = DFA22_eof;
            this.min = DFA22_min;
            this.max = DFA22_max;
            this.accept = DFA22_accept;
            this.special = DFA22_special;
            this.transition = DFA22_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | T__47 | T__48 | T__49 | T__50 | T__51 | T__52 | T__53 | T__54 | T__55 | T__56 | T__57 | T__58 | T__59 | T__60 | T__61 | T__62 | T__63 | T__64 | T__65 | T__66 | T__67 | T__68 | T__69 | T__70 | T__71 | T__72 | T__73 | T__74 | T__75 | T__76 | T__77 | T__78 | T__79 | T__80 | T__81 | T__82 | T__83 | T__84 | T__85 | T__86 | T__87 | T__88 | RULE_LOCALE | RULE_HEX | RULE_INT | RULE_DECIMAL | RULE_ID | RULE_STRING | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS | RULE_ANY_OTHER );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA22_0 = input.LA(1);

                        s = -1;
                        if ( (LA22_0=='=') ) {s = 1;}

                        else if ( (LA22_0=='|') ) {s = 2;}

                        else if ( (LA22_0=='&') ) {s = 3;}

                        else if ( (LA22_0=='d') ) {s = 4;}

                        else if ( (LA22_0=='+') ) {s = 5;}

                        else if ( (LA22_0=='-') ) {s = 6;}

                        else if ( (LA22_0=='*') ) {s = 7;}

                        else if ( (LA22_0=='/') ) {s = 8;}

                        else if ( (LA22_0=='%') ) {s = 9;}

                        else if ( (LA22_0=='!') ) {s = 10;}

                        else if ( (LA22_0=='>') ) {s = 11;}

                        else if ( (LA22_0=='<') ) {s = 12;}

                        else if ( (LA22_0=='.') ) {s = 13;}

                        else if ( (LA22_0=='?') ) {s = 14;}

                        else if ( (LA22_0=='v') ) {s = 15;}

                        else if ( (LA22_0=='e') ) {s = 16;}

                        else if ( (LA22_0=='s') ) {s = 17;}

                        else if ( (LA22_0=='i') ) {s = 18;}

                        else if ( (LA22_0=='f') ) {s = 19;}

                        else if ( (LA22_0=='l') ) {s = 20;}

                        else if ( (LA22_0=='{') ) {s = 21;}

                        else if ( (LA22_0=='}') ) {s = 22;}

                        else if ( (LA22_0==':') ) {s = 23;}

                        else if ( (LA22_0==';') ) {s = 24;}

                        else if ( (LA22_0=='a') ) {s = 25;}

                        else if ( (LA22_0==',') ) {s = 26;}

                        else if ( (LA22_0==')') ) {s = 27;}

                        else if ( (LA22_0=='#') ) {s = 28;}

                        else if ( (LA22_0=='[') ) {s = 29;}

                        else if ( (LA22_0==']') ) {s = 30;}

                        else if ( (LA22_0=='(') ) {s = 31;}

                        else if ( (LA22_0=='c') ) {s = 32;}

                        else if ( (LA22_0=='w') ) {s = 33;}

                        else if ( (LA22_0=='n') ) {s = 34;}

                        else if ( (LA22_0=='t') ) {s = 35;}

                        else if ( (LA22_0=='r') ) {s = 36;}

                        else if ( (LA22_0=='b'||(LA22_0>='g' && LA22_0<='h')||(LA22_0>='j' && LA22_0<='k')||LA22_0=='m'||(LA22_0>='o' && LA22_0<='q')||LA22_0=='u'||(LA22_0>='x' && LA22_0<='z')) ) {s = 37;}

                        else if ( (LA22_0=='0') ) {s = 38;}

                        else if ( ((LA22_0>='1' && LA22_0<='9')) ) {s = 39;}

                        else if ( (LA22_0=='^') ) {s = 40;}

                        else if ( (LA22_0=='$'||(LA22_0>='A' && LA22_0<='Z')||LA22_0=='_') ) {s = 41;}

                        else if ( (LA22_0=='\"') ) {s = 42;}

                        else if ( (LA22_0=='\'') ) {s = 43;}

                        else if ( ((LA22_0>='\t' && LA22_0<='\n')||LA22_0=='\r'||LA22_0==' ') ) {s = 44;}

                        else if ( ((LA22_0>='\u0000' && LA22_0<='\b')||(LA22_0>='\u000B' && LA22_0<='\f')||(LA22_0>='\u000E' && LA22_0<='\u001F')||LA22_0=='@'||LA22_0=='\\'||LA22_0=='`'||(LA22_0>='~' && LA22_0<='\uFFFF')) ) {s = 45;}

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 22, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

}