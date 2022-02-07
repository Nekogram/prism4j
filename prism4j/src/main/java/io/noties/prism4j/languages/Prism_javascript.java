package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.Prism4j;
import io.noties.prism4j.Token;
import io.noties.prism4j.annotations.Aliases;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static java.util.regex.Pattern.*;

@SuppressWarnings("unused")
@Aliases("js")
public class Prism_javascript {

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {

        final Grammar js = prism4j.requireGrammar("clike").extend("javascript",
                GrammarUtils.token("class-name", GrammarUtils.pattern(
                        compile("(\\b(?:class|interface|extends|implements|trait|instanceof|new)\\s+)[\\w.\\\\]+"),
                        true,
                        false,
                        null,
                        GrammarUtils.grammar("inside", GrammarUtils.token("punctuation", GrammarUtils.pattern(compile("[.\\\\]"))))
                ), GrammarUtils.pattern(compile("(^|[^$\\w\\xA0-\\uFFFF])(?!\\s)[_$A-Z\\xA0-\\uFFFF](?:(?!\\s)[$\\w\\xA0-\\uFFFF])*(?=\\.(?:prototype|constructor))"), true)),
                GrammarUtils.token("keyword", GrammarUtils.pattern(compile("((?:^|\\})\\s*)catch\\b"), true), GrammarUtils.pattern(compile("(^|[^.]|\\.\\.\\.\\s*)\\b(?:as|assert(?=\\s*\\{)|async(?=\\s*(?:function\\b|\\(|[$\\w\\xA0-\\uFFFF]|$))|await|break|case|class|const|continue|debugger|default|delete|do|else|enum|export|extends|finally(?=\\s*(?:\\{|$))|for|from(?=\\s*(?:['\"]|$))|function|(?:get|set)(?=\\s*(?:[#\\[$\\w\\xA0-\\uFFFF]|$))|if|implements|import|in|instanceof|interface|let|new|null|of|package|private|protected|public|return|static|super|switch|this|throw|try|typeof|undefined|var|void|while|with|yield)\\b"), true)),
                GrammarUtils.token("function", GrammarUtils.pattern(compile("#?(?!\\s)[_$a-zA-Z\\xA0-\\uFFFF](?:(?!\\s)[$\\w\\xA0-\\uFFFF])*(?=\\s*(?:\\.\\s*(?:apply|bind|call)\\s*)?\\()"))),
                GrammarUtils.token("number", GrammarUtils.pattern(compile("(^|[^\\w$])(?:NaN|Infinity|0[bB][01]+(?:_[01]+)*n?|0[oO][0-7]+(?:_[0-7]+)*n?|0[xX][\\dA-Fa-f]+(?:_[\\dA-Fa-f]+)*n?|\\d+(?:_\\d+)*n|(?:\\d+(?:_\\d+)*(?:\\.(?:\\d+(?:_\\d+)*)?)?|\\.\\d+(?:_\\d+)*)(?:[Ee][+-]?\\d+(?:_\\d+)*)?)(?![\\w$])"), true)),
                GrammarUtils.token("operator", GrammarUtils.pattern(compile("--|\\+\\+|\\*\\*=?|=>|&&=?|\\|\\|=?|[!=]==|<<=?|>>>?=?|[-+*/%&|^!=<>]=?|\\.{3}|\\?\\?=?|\\?\\.?|[~:]")))
        );

        js.insertBeforeToken("keyword",
                GrammarUtils.token("regex", GrammarUtils.pattern(
                        compile("((?:^|[^$\\w\\xA0-\\uFFFF.\"'\\])\\s]|\\b(?:return|yield))\\s*)/(?:\\[(?:[^\\]\\\\\r\n]|\\\\.)*]|\\\\.|[^/\\\\\\[\\r\\n])+/[dgimyus]{0,7}(?=(?:\\s|\\/\\*(?:[^*]|\\*(?!\\/))*\\*\\/)*(?:$|[\\r\\n,.;:\\})\\]]|//))"),
                        true,
                        true,
                        null,
                        GrammarUtils.grammar("inside",
                                GrammarUtils.token("regex-source", GrammarUtils.pattern(compile("^(/)[\\s\\S]+(?=/[a-z]*$)"), true, false, "language-regex", prism4j.requireGrammar("regex"))),
                                GrammarUtils.token("regex-delimiter", GrammarUtils.pattern(compile("^/|/$"))),
                                GrammarUtils.token("regex-flags", GrammarUtils.pattern(compile("[a-z]+$")))
                        )
                )),
                GrammarUtils.token(
                        "function-variable",
                        GrammarUtils.pattern(
                                compile("#?(?!\\s)[_$a-zA-Z\\xA0-\\uFFFF](?:(?!\\s)[$\\w\\xA0-\\uFFFF])*(?=\\s*[=:]\\s*(?:async\\s*)?(?:\\bfunction\\b|(?:\\((?:[^()]|\\([^()]*\\))*\\)|(?!\\s)[_$a-zA-Z\\xA0-\\uFFFF](?:(?!\\s)[$\\w\\xA0-\\uFFFF])*)\\s*=>))", CASE_INSENSITIVE),
                                false,
                                false,
                                "function"
                        )
                ),
                GrammarUtils.token("parameter",
                        GrammarUtils.pattern(compile("(function(?:\\s+(?!\\s)[_?(a-zA-Z\\xA0-\\uFFFF](?:(?!\\s)[$\\w\\xA0-\\uFFFF])*)?\\s*\\(\\s*)(?!\\s)(?:[^()\\s]|\\s+(?![\\s)])|\\([^()]*\\))+(?=\\s*\\))"), true, false, null, js),
                        GrammarUtils.pattern(compile("(^|[^$\\w\\xA0-\\uFFFF])(?!\\s)[_$a-zA-Z\\xA0-\\uFFFF](?:(?!\\s)[$\\w\\xA0-\\uFFFF])*(?=\\s*=>)", CASE_INSENSITIVE), true, false, null, js),
                        GrammarUtils.pattern(compile("(\\(\\s*)(?!\\s)(?:[^()\\s]|\\s+(?![\\s)])|\\([^()]*\\))+(?=\\s*\\)\\s*=>)"), true, false, null, js),
                        GrammarUtils.pattern(compile("((?:\\b|\\s|^)(?!(?:as|async|await|break|case|catch|class|const|continue|debugger|default|delete|do|else|enum|export|extends|finally|for|from|function|get|if|implements|import|in|instanceof|interface|let|new|null|of|package|private|protected|public|return|set|static|super|switch|this|throw|try|typeof|undefined|var|void|while|with|yield)(?![$\\w\\xA0-\\uFFFF]))(?:(?!\\s)[_$a-zA-Z\\xA0-\\uFFFF](?:(?!\\s)[$\\w\\xA0-\\uFFFF])*\\s*)\\(\\s*|\\]\\s*\\(\\s*)(?!\\s)(?:[^()\\s]|\\s+(?![\\s)])|\\([^()]*\\))+?(?=\\s*\\)\\s*\\{)"), true, false, null, js)
                ),
                GrammarUtils.token("constant", GrammarUtils.pattern(compile("\\b[A-Z](?:[A-Z_]|\\dx?)*\\b")))
        );

        final Token interpolation = GrammarUtils.token("interpolation");

        js.insertBeforeToken("string",
                GrammarUtils.token("hashbang", GrammarUtils.pattern(compile("^#!.*"), false, true, "comment")),
                GrammarUtils.token(
                        "template-string",
                        GrammarUtils.pattern(
                                compile("`(?:\\\\[\\s\\S]|\\$\\{(?:[^{\\}]|\\{(?:[^{\\}]|\\{[^\\}]*\\})*\\})+\\}|(?!\\$\\{)[^\\\\`])*`"),
                                false,
                                true,
                                null,
                                GrammarUtils.grammar(
                                        "inside",
                                        GrammarUtils.token("template-punctuation", GrammarUtils.pattern(compile("^`|`$"), false, false, "string")),
                                        interpolation,
                                        GrammarUtils.token("string", GrammarUtils.pattern(compile("[\\s\\S]+")))
                                )
                        )
                ),
                GrammarUtils.token("string-property", GrammarUtils.pattern(compile("((?:^|[,{])[ \\t]*)([\"'])(?:\\\\(?:\\r\\n|[\\s\\S])|(?!\\2)[^\\\\\\r\\n])*\\2(?=\\s*:)", MULTILINE), true, true, "property"))
        );

        js.insertBeforeToken("operator", GrammarUtils.token("literal-property",
                GrammarUtils.pattern(compile("((?:^|[,{])[ \\t]*)(?!\\s)[_$a-zA-Z\\xA0-\\uFFFF](?:(?!\\s)[$\\w\\xA0-\\uFFFF])*(?=\\s*:)", MULTILINE), true, false, "property")));

        final Grammar insideInterpolation;
        {
            final List<Token> tokens = new ArrayList<>(js.tokens().size() + 1);
            tokens.add(GrammarUtils.token("interpolation-punctuation", GrammarUtils.pattern(compile("^\\$\\{|\\}$"), false, false, "punctuation")));
            tokens.addAll(js.tokens());
            insideInterpolation = GrammarUtils.grammar("inside", tokens);
        }

        interpolation.patterns().add(
                GrammarUtils.pattern(compile("((?:^|[^\\\\])(?:\\\\{2})*)\\$\\{(?:[^{\\}]|\\{(?:[^{\\}]|\\{[^\\}]*\\})*\\})+\\}"), true, false, null, insideInterpolation)
        );

        return js;
    }
}
