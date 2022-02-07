package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.Prism4j;
import org.jetbrains.annotations.NotNull;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;


@SuppressWarnings("unused")
public class Prism_swift {

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {

        final Grammar swift = GrammarUtils.grammar(
                "swift",
                GrammarUtils.token("comment", GrammarUtils.pattern(compile("(^|[^\\\\:])(?://.*|/\\*(?:[^/*]|/(?!\\*)|\\*(?!/)|/\\*(?:[^*]|\\*(?!/))*\\*/)*\\*/)"), true, true)),
                GrammarUtils.token("directive", GrammarUtils.pattern(compile("#(?:(?:elseif|if)\\b(?:[ \\t]*(?:![ \\t]*)?(?:\\b\\w+\\b(?:[ \\t]*\\((?:[^()]|\\([^()]*\\))*\\))?|\\((?:[^()]|\\([^()]*\\))*\\))(?:[ \\t]*(?:&&|\\|\\|))?)+|(?:else|endif)\\b)"), false, false, "property",
                        GrammarUtils.grammar("inside",
                                GrammarUtils.token("directive-name", GrammarUtils.pattern(compile("^#\\w+"))),
                                GrammarUtils.token("boolean", GrammarUtils.pattern(compile("\\b(?:true|false)\\b"))),
                                GrammarUtils.token("number", GrammarUtils.pattern(compile("\\b\\d+(?:\\.\\d+)*\\b"))),
                                GrammarUtils.token("operator", GrammarUtils.pattern(compile("!|&&|\\|\\||[<>]=?"))),
                                GrammarUtils.token("punctuation", GrammarUtils.pattern(compile("[(),]")))
                        )
                )),
                GrammarUtils.token("literal", GrammarUtils.pattern(compile("#(?:colorLiteral|column|dsohandle|file(?:ID|Literal|Path)?|function|imageLiteral|line)\\b"), false, false, "constant")),
                GrammarUtils.token("other-directive", GrammarUtils.pattern(compile("#\\w+\\b"), false, false, "property")),
                GrammarUtils.token("attribute", GrammarUtils.pattern(compile("@\\w+"), false, false, "atrule")),
                GrammarUtils.token("function-definition", GrammarUtils.pattern(compile("(\\bfunc\\s+)\\w+"), true, false, "function")),
                GrammarUtils.token("label", GrammarUtils.pattern(compile("\\b(break|continue)\\s+\\w+|\\b[a-zA-Z_]\\w*(?=\\s*:\\s*(?:for|repeat|while)\\b)"), true, false, "important")),
                GrammarUtils.token("keyword", GrammarUtils.pattern(
                        compile("\\b(?:Any|Protocol|Self|Type|actor|as|assignment|associatedtype|associativity|async|await|break|case|catch|class|continue|convenience|default|defer|deinit|didSet|do|dynamic|else|enum|extension|fallthrough|fileprivate|final|for|func|get|guard|higherThan|if|import|in|indirect|infix|init|inout|internal|is|isolated|lazy|left|let|lowerThan|mutating|none|nonisolated|nonmutating|open|operator|optional|override|postfix|precedencegroup|prefix|private|protocol|public|repeat|required|rethrows|return|right|safe|self|set|some|static|struct|subscript|super|switch|throw|throws|try|typealias|unowned|unsafe|var|weak|where|while|willSet)\\b")
                )),
                GrammarUtils.token("boolean", GrammarUtils.pattern(compile("\\b(?:true|false)\\b"))),
                GrammarUtils.token("nil", GrammarUtils.pattern(compile("\\bnil\\b"), false, false, "constant")),
                GrammarUtils.token("short-argument", GrammarUtils.pattern(compile("\\$\\d+\\b"))),
                GrammarUtils.token("omit", GrammarUtils.pattern(compile("\\b_\\b"), false, false, "keyword")),
                GrammarUtils.token("number", GrammarUtils.pattern(
                        compile("\\b(?:[\\d_]+(?:\\.[\\de_]+)?|0x[a-f0-9_]+(?:\\.[a-f0-9p_]+)?|0b[01_]+|0o[0-7_]+)\\b", CASE_INSENSITIVE)
                )),
                GrammarUtils.token("class-name", GrammarUtils.pattern(compile("\\b[A-Z](?:[A-Z_\\d]*[a-z]\\w*)?\\b"))),
                GrammarUtils.token("function", GrammarUtils.pattern(compile("\\b[a-z_]\\w*(?=\\s*\\()", CASE_INSENSITIVE))),
                GrammarUtils.token("constant", GrammarUtils.pattern(compile("\\b(?:[A-Z_]{2,}|k[A-Z][A-Za-z_]+)\\b"))),
                GrammarUtils.token("operator", GrammarUtils.pattern(compile("[-+*/%=!<>&|^~?]+|\\.[.\\-+*/%=!<>&|^~?]+"))),
                GrammarUtils.token("punctuation", GrammarUtils.pattern(compile("[{\\}\\[\\]();,.:\\\\]")))
        );

        swift.insertBeforeToken("directive",
                GrammarUtils.token("string-literal",
                        GrammarUtils.pattern(
                                compile("(^|[^\"#])(?:\"(?:\\\\(?:\\((?:[^()]|\\([^()]*\\))*\\)|\\r\\n|[^(])|[^\\\\\\r\\n\"])*\"|\"\"\"(?:\\\\(?:\\((?:[^()]|\\([^()]*\\))*\\)|[^(])|[^\\\\\"]|\"(?!\"\"))*\"\"\")(?![\"#])"),
                                true,
                                true,
                                null,
                                GrammarUtils.grammar("inside",
                                        GrammarUtils.token("interpolation", GrammarUtils.pattern(compile("(\\\\\\()(?:[^()]|\\([^()]*\\))*(?=\\))"), true, true, null, swift)),
                                        GrammarUtils.token("interpolation-punctuation", GrammarUtils.pattern(compile("^\\)|\\\\\\($"), false, false, "punctuation")),
                                        GrammarUtils.token("punctuation", GrammarUtils.pattern(compile("\\\\(?=[\\r\\n])"))),
                                        GrammarUtils.token("string", GrammarUtils.pattern(compile("[\\s\\S]+")))
                                )
                        ),
                        GrammarUtils.pattern(compile("(^|[^\"#])(#+)(?:\"(?:\\\\(?:#+\\((?:[^()]|\\([^()]*\\))*\\)|\\r\\n|[^#])|[^\\\\\\r\\n])*?\"|\"\"\"(?:\\\\(?:#+\\((?:[^()]|\\([^()]*\\))*\\)|[^#])|[^\\\\])*?\"\"\")\\2"), true, true, null,
                                GrammarUtils.grammar("inside",
                                        GrammarUtils.token("interpolation", GrammarUtils.pattern(compile("(\\\\#+\\()(?:[^()]|\\([^()]*\\))*(?=\\))"), true, false, null, swift)),
                                        GrammarUtils.token("interpolation-punctuation", GrammarUtils.pattern(compile("^\\)|\\\\#+\\($"), false, false, "punctuation")),
                                        GrammarUtils.token("string", GrammarUtils.pattern(compile("[\\s\\S]+")))
                                )
                        )
                )
        );

        return swift;
    }
}
