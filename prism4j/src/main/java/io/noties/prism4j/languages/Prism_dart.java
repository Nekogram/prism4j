package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.Pattern;
import io.noties.prism4j.Prism4j;
import org.jetbrains.annotations.NotNull;

import static java.util.regex.Pattern.compile;

@SuppressWarnings("unused")
public class Prism_dart {

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {

        final Pattern[] keywords = {
                GrammarUtils.pattern(compile("\\b(?:async|sync|yield)\\*")),
                GrammarUtils.pattern(compile("\\b(?:abstract|assert|async|await|break|case|catch|class|const|continue|covariant|default|deferred|do|dynamic|else|enum|export|extension|external|extends|factory|final|finally|for|get|hide|if|implements|interface|import|in|library|mixin|new|null|on|operator|part|rethrow|return|set|show|static|super|switch|sync|this|throw|try|typedef|var|void|while|with|yield)\\b"))
        };
        final Grammar classNameInside = GrammarUtils.grammar("inside", GrammarUtils.token("namespace", GrammarUtils.pattern(compile("^[a-z]\\w*(?:\\s*\\.\\s*[a-z]\\w*)*(?:\\s*\\.)?"), false, false, null, GrammarUtils.grammar("inside", GrammarUtils.token("punctuation", GrammarUtils.pattern(compile("\\.")))))));
        final Pattern className = GrammarUtils.pattern(compile("(^|[^\\w.])(?:[a-z]\\w*\\s*\\.\\s*)*(?:[A-Z]\\w*\\s*\\.\\s*)*[A-Z](?:[\\d_A-Z]*[a-z]\\w*)?\\b"), true, false, null, classNameInside);
        final Grammar dart = prism4j.requireGrammar("clike").extend(
                "dart",
                GrammarUtils.token("class-name",
                        className,
                        GrammarUtils.pattern(compile("(^|[^\\w.])(?:[a-z]\\w*\\s*\\.\\s*)*(?:[A-Z]\\w*\\s*\\.\\s*)*[A-Z]\\w*(?=\\s+\\w+\\s*[;,=()])"), true, false, null, classNameInside)
                ),
                GrammarUtils.token("keyword", keywords),
                GrammarUtils.token("operator", GrammarUtils.pattern(compile("\\bis!|\\b(?:as|is)\\b|\\+\\+|--|&&|\\|\\||<<=?|>>=?|~(?:\\/=?)?|[+\\-*\\/%&^|=!<>]=?|\\?")))
        );

        dart.insertBeforeToken("string",
                GrammarUtils.token("string-literal", GrammarUtils.pattern(compile("r?(?:(\"\"\"|''')[\\s\\S]*?\\1|([\"'])(?:\\\\.|(?!\\2)[^\\\\\\r\\n])*\\2(?!\\2))"), false, true, null, GrammarUtils.grammar("inside",
                        GrammarUtils.token("interpolation", GrammarUtils.pattern(compile("((?:^|[^\\\\])(?:\\\\{2})*)\\$(?:\\w+|\\{(?:[^{\\}]|\\{[^{}]*\\})*\\})"), true, false, null,
                                GrammarUtils.grammar("inside", GrammarUtils.token("punctuation", GrammarUtils.pattern(compile("^\\$\\{?|\\}$"))),
                                        GrammarUtils.token("expression", GrammarUtils.pattern(compile("[\\s\\S]+"), false, false, null, dart))
                                ))), GrammarUtils.token("string", GrammarUtils.pattern(compile("[\\s\\S]+"))))))
                /*token("string",
                        pattern(compile("r?(\"\"\"|''')[\\s\\S]*?\\1"), false, true),
                        pattern(compile("r?(\"|')(?:\\\\.|(?!\\1)[^\\\\\\r\\n])*\\1"), false, true)
                )*/
        );

        dart.insertBeforeToken("class-name",
                GrammarUtils.token("metadata", GrammarUtils.pattern(compile("@\\w+"), false, false, "function"))
        );

        dart.insertBeforeToken("class-name", GrammarUtils.token("generics", GrammarUtils.pattern(compile("<(?:[\\w\\s,.&?]|<(?:[\\w\\s,.&?]|<(?:[\\w\\s,.&?]|<[\\w\\s,.&?]*>)*>)*>)*>"), false, false, null, GrammarUtils.grammar("inside",
                GrammarUtils.token("class-name", className),
                GrammarUtils.token("keyword", keywords),
                GrammarUtils.token("punctuation", GrammarUtils.pattern(compile("[<>(),.:]"))),
                GrammarUtils.token("operator", GrammarUtils.pattern(compile("[?&|]")))
        ))));

        return dart;
    }
}
