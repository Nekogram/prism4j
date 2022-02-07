package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.Prism4j;
import io.noties.prism4j.Token;
import io.noties.prism4j.annotations.Aliases;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;


@SuppressWarnings("unused")
@Aliases({"kt", "kts"})
public class Prism_kotlin {

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {

        final Grammar kotlin = prism4j.requireGrammar("clike").extend(
                "kotlin",
                token -> !"class-name".equals(token.name()),
                GrammarUtils.token(
                        "keyword",
                        GrammarUtils.pattern(compile("(^|[^.])\\b(?:abstract|actual|annotation|as|break|by|catch|class|companion|const|constructor|continue|crossinline|data|do|dynamic|else|enum|expect|external|final|finally|for|fun|get|if|import|in|infix|init|inline|inner|interface|internal|is|lateinit|noinline|null|object|open|operator|out|override|package|private|protected|public|reified|return|sealed|set|super|suspend|tailrec|this|throw|to|try|typealias|val|var|vararg|when|where|while)\\b"), true)
                ),
                GrammarUtils.token(
                        "function",
                        GrammarUtils.pattern(compile("(?:`[^\\r\\n`]+`|\\b\\w+)(?=\\s*\\()"), false, true),
                        GrammarUtils.pattern(compile("(\\.)(?:`[^\\r\\n`]+`|\\w+)(?=\\s*\\{)"), true, true)
                ),
                GrammarUtils.token(
                        "number",
                        GrammarUtils.pattern(compile("\\b(?:0[xX][\\da-fA-F]+(?:_[\\da-fA-F]+)*|0[bB][01]+(?:_[01]+)*|\\d+(?:_\\d+)*(?:\\.\\d+(?:_\\d+)*)?(?:[eE][+-]?\\d+(?:_\\d+)*)?[fFL]?)\\b"))
                ),
                GrammarUtils.token(
                        "operator",
                        GrammarUtils.pattern(compile("\\+[+=]?|-[-=>]?|==?=?|!(?:!|==?)?|[\\/*%<>]=?|[?:]:?|\\.\\.|&&|\\|\\||\\b(?:and|inv|or|shl|shr|ushr|xor)\\b"))
                )
        );

        // this grammar has 1 token: interpolation, which has 2 patterns
        final Grammar interpolationInside;
        {

            // okay, I was cloning the tokens of kotlin grammar (so there is no recursive chain of calls),
            // but it looks like it wants to have recursive calls
            // I did this because interpolation test was failing due to the fact that `string`
            // `raw-string` tokens didn't have `inside`, so there were not tokenized
            // I still find that it has potential to fall with stackoverflow (in some cases)
            final List<Token> tokens = new ArrayList<>(kotlin.tokens().size() + 1);
            tokens.add(GrammarUtils.token("delimiter", GrammarUtils.pattern(compile("^\\$\\{|\\}$"), false, false, "variable")));
            tokens.addAll(kotlin.tokens());

            interpolationInside = GrammarUtils.grammar(
                    "inside",
                    GrammarUtils.token("interpolation-punctuation", GrammarUtils.pattern(compile("^\\$\\{?|\\}$"), false, false, "punctuation")),
                    GrammarUtils.token("expression", GrammarUtils.pattern(compile("[\\s\\S]+"), false, false, null, kotlin))
            );
        }

        kotlin.insertBeforeToken("string",
                GrammarUtils.token("string-literal",
                        GrammarUtils.pattern(compile("\"\"\"(?:[^$]|\\$(?:(?!\\{)|\\{[^{\\}]*\\}))*?\"\"\""), false, false, "multiline", GrammarUtils.grammar("inside", GrammarUtils.token("interpolation", GrammarUtils.pattern(compile("\\$(?:[a-z_]\\w*|\\{[^{\\}]*\\})", Pattern.CASE_INSENSITIVE), false, false, null, interpolationInside)), GrammarUtils.token("string", GrammarUtils.pattern(compile("[\\s\\S]+"))))),
                        GrammarUtils.pattern(compile("\"(?:[^\"\\\\\\r\\n$]|\\\\.|\\$(?:(?!\\{)|\\{[^{\\}]*\\}))*\""), false, false, "singleline", GrammarUtils.grammar("inside", GrammarUtils.token("interpolation", GrammarUtils.pattern(compile("((?:^|[^\\\\])(?:\\\\{2})*)\\$(?:[a-z_]\\w*|\\{[^{\\}]*\\})", Pattern.CASE_INSENSITIVE), true, false, null, interpolationInside)), GrammarUtils.token("string", GrammarUtils.pattern(compile("[\\s\\S]+")))))
                ),
                GrammarUtils.token("char", GrammarUtils.pattern(compile("'(?:[^'\\\\\\r\\n]|\\\\(?:.|u[a-fA-F0-9]{0,4}))'"), false, true))
        );

        kotlin.tokens().remove(kotlin.findToken("string"));

        kotlin.insertBeforeToken("keyword",
                GrammarUtils.token("annotation", GrammarUtils.pattern(compile("\\B@(?:\\w+:)?(?:[A-Z]\\w*|\\[[^\\]]+\\])"), false, false, "builtin"))
        );

        kotlin.insertBeforeToken("function",
                GrammarUtils.token("label", GrammarUtils.pattern(compile("\\b\\w+@|@\\w+\\b"), false, false, "symbol"))
        );

        return kotlin;
    }
}
