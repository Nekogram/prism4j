package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.Prism4j;
import org.jetbrains.annotations.NotNull;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

@SuppressWarnings("unused")
public class Prism_python {

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {
        return GrammarUtils.grammar("python",
                GrammarUtils.token("comment", GrammarUtils.pattern(
                        compile("(^|[^\\\\])#.*"),
                        true
                )),
                GrammarUtils.token("triple-quoted-string", GrammarUtils.pattern(
                        compile("(\"\"\"|''')[\\s\\S]+?\\1"),
                        false,
                        true,
                        "string"
                )),
                GrammarUtils.token("string", GrammarUtils.pattern(
                        compile("(\"|')(?:\\\\.|(?!\\1)[^\\\\\\r\\n])*\\1"),
                        false,
                        true
                )),
                GrammarUtils.token("function", GrammarUtils.pattern(
                        compile("((?:^|\\s)def[ \\t]+)[a-zA-Z_]\\w*(?=\\s*\\()"),
                        true
                )),
                GrammarUtils.token("class-name", GrammarUtils.pattern(
                        compile("(\\bclass\\s+)\\w+", CASE_INSENSITIVE),
                        true
                )),
                GrammarUtils.token("keyword", GrammarUtils.pattern(compile("\\b(?:as|assert|async|await|break|class|continue|def|del|elif|else|except|exec|finally|for|from|global|if|import|in|is|lambda|nonlocal|pass|print|raise|return|try|while|with|yield)\\b"))),
                GrammarUtils.token("builtin", GrammarUtils.pattern(compile("\\b(?:__import__|abs|all|any|apply|ascii|basestring|bin|bool|buffer|bytearray|bytes|callable|chr|classmethod|cmp|coerce|compile|complex|delattr|dict|dir|divmod|enumerate|eval|execfile|file|filter|float|format|frozenset|getattr|globals|hasattr|hash|help|hex|id|input|int|intern|isinstance|issubclass|iter|len|list|locals|long|map|max|memoryview|min|next|object|oct|open|ord|pow|property|range|raw_input|reduce|reload|repr|reversed|round|set|setattr|slice|sorted|staticmethod|str|sum|super|tuple|type|unichr|unicode|vars|xrange|zip)\\b"))),
                GrammarUtils.token("boolean", GrammarUtils.pattern(compile("\\b(?:True|False|None)\\b"))),
                GrammarUtils.token("number", GrammarUtils.pattern(
                        compile("(?:\\b(?=\\d)|\\B(?=\\.))(?:0[bo])?(?:(?:\\d|0x[\\da-f])[\\da-f]*\\.?\\d*|\\.\\d+)(?:e[+-]?\\d+)?j?\\b", CASE_INSENSITIVE)
                )),
                GrammarUtils.token("operator", GrammarUtils.pattern(compile("[-+%=]=?|!=|\\*\\*?=?|\\/\\/?=?|<[<=>]?|>[=>]?|[&|^~]|\\b(?:or|and|not)\\b"))),
                GrammarUtils.token("punctuation", GrammarUtils.pattern(compile("[{\\}\\[\\];(),.:]")))
        );
    }
}
