package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.GrammarUtils;
import io.noties.prism4j.Prism4j;
import io.noties.prism4j.Token;
import io.noties.prism4j.annotations.Aliases;
import io.noties.prism4j.annotations.Modify;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static io.noties.prism4j.Prism4j.*;
import static java.util.regex.Pattern.*;

@SuppressWarnings("unused")
@Aliases("js")
@Modify("markup")
public class Prism_javascript {

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {

        final Grammar js = GrammarUtils.extend(GrammarUtils.require(prism4j, "clike"), "javascript",
                token("class-name", pattern(
                        compile("(\\b(?:class|interface|extends|implements|trait|instanceof|new)\\s+)[\\w.\\\\]+"),
                        true,
                        false,
                        null,
                        grammar("inside", token("punctuation", pattern(compile("[.\\\\]"))))
                ), pattern(compile("(^|[^$\\w\\xA0-\\uFFFF])(?!\\s)[_$A-Z\\xA0-\\uFFFF](?:(?!\\s)[$\\w\\xA0-\\uFFFF])*(?=\\.(?:prototype|constructor))"), true)),
                token("keyword", pattern(compile("((?:^|\\})\\s*)catch\\b"), true), pattern(compile("(^|[^.]|\\.\\.\\.\\s*)\\b(?:as|assert(?=\\s*\\{)|async(?=\\s*(?:function\\b|\\(|[$\\w\\xA0-\\uFFFF]|$))|await|break|case|class|const|continue|debugger|default|delete|do|else|enum|export|extends|finally(?=\\s*(?:\\{|$))|for|from(?=\\s*(?:['\"]|$))|function|(?:get|set)(?=\\s*(?:[#\\[$\\w\\xA0-\\uFFFF]|$))|if|implements|import|in|instanceof|interface|let|new|null|of|package|private|protected|public|return|static|super|switch|this|throw|try|typeof|undefined|var|void|while|with|yield)\\b"), true)),
                token("function", pattern(compile("#?(?!\\s)[_$a-zA-Z\\xA0-\\uFFFF](?:(?!\\s)[$\\w\\xA0-\\uFFFF])*(?=\\s*(?:\\.\\s*(?:apply|bind|call)\\s*)?\\()"))),
                token("number", pattern(compile("(^|[^\\w$])(?:NaN|Infinity|0[bB][01]+(?:_[01]+)*n?|0[oO][0-7]+(?:_[0-7]+)*n?|0[xX][\\dA-Fa-f]+(?:_[\\dA-Fa-f]+)*n?|\\d+(?:_\\d+)*n|(?:\\d+(?:_\\d+)*(?:\\.(?:\\d+(?:_\\d+)*)?)?|\\.\\d+(?:_\\d+)*)(?:[Ee][+-]?\\d+(?:_\\d+)*)?)(?![\\w$])"), true)),
                token("operator", pattern(compile("--|\\+\\+|\\*\\*=?|=>|&&=?|\\|\\|=?|[!=]==|<<=?|>>>?=?|[-+*/%&|^!=<>]=?|\\.{3}|\\?\\?=?|\\?\\.?|[~:]")))
        );

        GrammarUtils.insertBeforeToken(js, "keyword",
                token("regex", pattern(
                        compile("((?:^|[^$\\w\\xA0-\\uFFFF.\"'\\])\\s]|\\b(?:return|yield))\\s*)/(?:\\[(?:[^\\]\\\\\r\n]|\\\\.)*]|\\\\.|[^/\\\\\\[\\r\\n])+/[dgimyus]{0,7}(?=(?:\\s|\\/\\*(?:[^*]|\\*(?!\\/))*\\*\\/)*(?:$|[\\r\\n,.;:\\})\\]]|//))"),
                        true,
                        true,
                        null,
                        grammar("inside",
                                token("regex-source", pattern(compile("^(/)[\\s\\S]+(?=/[a-z]*$)"), true, false, "language-regex", GrammarUtils.require(prism4j, "regex"))),
                                token("regex-delimiter", pattern(compile("^/|/$"))),
                                token("regex-flags", pattern(compile("[a-z]+$")))
                        )
                )),
                token(
                        "function-variable",
                        pattern(
                                compile("#?(?!\\s)[_$a-zA-Z\\xA0-\\uFFFF](?:(?!\\s)[$\\w\\xA0-\\uFFFF])*(?=\\s*[=:]\\s*(?:async\\s*)?(?:\\bfunction\\b|(?:\\((?:[^()]|\\([^()]*\\))*\\)|(?!\\s)[_$a-zA-Z\\xA0-\\uFFFF](?:(?!\\s)[$\\w\\xA0-\\uFFFF])*)\\s*=>))", CASE_INSENSITIVE),
                                false,
                                false,
                                "function"
                        )
                ),
                token("parameter",
                        pattern(compile("(function(?:\\s+(?!\\s)[_?(a-zA-Z\\xA0-\\uFFFF](?:(?!\\s)[$\\w\\xA0-\\uFFFF])*)?\\s*\\(\\s*)(?!\\s)(?:[^()\\s]|\\s+(?![\\s)])|\\([^()]*\\))+(?=\\s*\\))"), true, false, null, js),
                        pattern(compile("(^|[^$\\w\\xA0-\\uFFFF])(?!\\s)[_$a-zA-Z\\xA0-\\uFFFF](?:(?!\\s)[$\\w\\xA0-\\uFFFF])*(?=\\s*=>)", CASE_INSENSITIVE), true, false, null, js),
                        pattern(compile("(\\(\\s*)(?!\\s)(?:[^()\\s]|\\s+(?![\\s)])|\\([^()]*\\))+(?=\\s*\\)\\s*=>)"), true, false, null, js),
                        pattern(compile("((?:\\b|\\s|^)(?!(?:as|async|await|break|case|catch|class|const|continue|debugger|default|delete|do|else|enum|export|extends|finally|for|from|function|get|if|implements|import|in|instanceof|interface|let|new|null|of|package|private|protected|public|return|set|static|super|switch|this|throw|try|typeof|undefined|var|void|while|with|yield)(?![$\\w\\xA0-\\uFFFF]))(?:(?!\\s)[_$a-zA-Z\\xA0-\\uFFFF](?:(?!\\s)[$\\w\\xA0-\\uFFFF])*\\s*)\\(\\s*|\\]\\s*\\(\\s*)(?!\\s)(?:[^()\\s]|\\s+(?![\\s)])|\\([^()]*\\))+?(?=\\s*\\)\\s*\\{)"), true, false, null, js)
                ),
                token("constant", pattern(compile("\\b[A-Z](?:[A-Z_]|\\dx?)*\\b")))
        );

        final Token interpolation = token("interpolation");

        GrammarUtils.insertBeforeToken(js, "string",
                token("hashbang", pattern(compile("^#!.*"), false, true, "comment")),
                token(
                        "template-string",
                        pattern(
                                compile("`(?:\\\\[\\s\\S]|\\$\\{(?:[^{\\}]|\\{(?:[^{\\}]|\\{[^\\}]*\\})*\\})+\\}|(?!\\$\\{)[^\\\\`])*`"),
                                false,
                                true,
                                null,
                                grammar(
                                        "inside",
                                        token("template-punctuation", pattern(compile("^`|`$"), false, false, "string")),
                                        interpolation,
                                        token("string", pattern(compile("[\\s\\S]+")))
                                )
                        )
                ),
                token("string-property", pattern(compile("((?:^|[,{])[ \\t]*)([\"'])(?:\\\\(?:\\r\\n|[\\s\\S])|(?!\\2)[^\\\\\\r\\n])*\\2(?=\\s*:)", MULTILINE), true, true, "property"))
        );

        GrammarUtils.insertBeforeToken(js, "operator", token("literal-property",
                pattern(compile("((?:^|[,{])[ \\t]*)(?!\\s)[_$a-zA-Z\\xA0-\\uFFFF](?:(?!\\s)[$\\w\\xA0-\\uFFFF])*(?=\\s*:)", MULTILINE), true, false, "property")));

        final Grammar insideInterpolation;
        {
            final List<Token> tokens = new ArrayList<>(js.tokens().size() + 1);
            tokens.add(token("interpolation-punctuation", pattern(compile("^\\$\\{|\\}$"), false, false, "punctuation")));
            tokens.addAll(js.tokens());
            insideInterpolation = grammar("inside", tokens);
        }

        interpolation.patterns().add(
                pattern(compile("((?:^|[^\\\\])(?:\\\\{2})*)\\$\\{(?:[^{\\}]|\\{(?:[^{\\}]|\\{[^\\}]*\\})*\\})+\\}"), true, false, null, insideInterpolation)
        );

        final Grammar markup = prism4j.grammar("markup");
        if (markup != null) {
            GrammarUtils.insertBeforeToken(markup, "tag",
                    token(
                            "script", pattern(
                                    compile("(<script[\\s\\S]*?>)[\\s\\S]*?(?=</script>)", CASE_INSENSITIVE),
                                    true,
                                    true,
                                    "language-javascript",
                                    js
                            )
                    )
            );
        }

        return js;
    }
}
