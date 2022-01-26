package io.noties.prism4j.languages;

import io.noties.prism4j.Grammar;
import io.noties.prism4j.Prism4j;
import org.jetbrains.annotations.NotNull;

import static io.noties.prism4j.Prism4j.*;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;


@SuppressWarnings("unused")
public class Prism_swift {

    @NotNull
    public static Grammar create(@NotNull Prism4j prism4j) {

        final Grammar swift = grammar(
                "swift",
                token("comment", pattern(compile("(^|[^\\\\:])(?://.*|/\\*(?:[^/*]|/(?!\\*)|\\*(?!/)|/\\*(?:[^*]|\\*(?!/))*\\*/)*\\*/)"), true, true)),
                token("directive", pattern(compile("#(?:(?:elseif|if)\\b(?:[ \\t]*(?:![ \\t]*)?(?:\\b\\w+\\b(?:[ \\t]*\\((?:[^()]|\\([^()]*\\))*\\))?|\\((?:[^()]|\\([^()]*\\))*\\))(?:[ \\t]*(?:&&|\\|\\|))?)+|(?:else|endif)\\b)"), false, false, "property",
                        grammar("inside",
                                token("directive-name", pattern(compile("^#\\w+"))),
                                token("boolean", pattern(compile("\\b(?:true|false)\\b"))),
                                token("number", pattern(compile("\\b\\d+(?:\\.\\d+)*\\b"))),
                                token("operator", pattern(compile("!|&&|\\|\\||[<>]=?"))),
                                token("punctuation", pattern(compile("[(),]")))
                        )
                )),
                token("literal", pattern(compile("#(?:colorLiteral|column|dsohandle|file(?:ID|Literal|Path)?|function|imageLiteral|line)\\b"), false, false, "constant")),
                token("other-directive", pattern(compile("#\\w+\\b"), false, false, "property")),
                token("attribute", pattern(compile("@\\w+"), false, false, "atrule")),
                token("function-definition", pattern(compile("(\\bfunc\\s+)\\w+"), true, false, "function")),
                token("label", pattern(compile("\\b(break|continue)\\s+\\w+|\\b[a-zA-Z_]\\w*(?=\\s*:\\s*(?:for|repeat|while)\\b)"), true, false, "important")),
                token("keyword", pattern(
                        compile("\\b(?:Any|Protocol|Self|Type|actor|as|assignment|associatedtype|associativity|async|await|break|case|catch|class|continue|convenience|default|defer|deinit|didSet|do|dynamic|else|enum|extension|fallthrough|fileprivate|final|for|func|get|guard|higherThan|if|import|in|indirect|infix|init|inout|internal|is|lazy|left|let|lowerThan|mutating|none|nonisolated|nonmutating|open|operator|optional|override|postfix|precedencegroup|prefix|private|protocol|public|repeat|required|rethrows|return|right|safe|self|set|some|static|struct|subscript|super|switch|throw|throws|try|typealias|unowned|unsafe|var|weak|where|while|willSet)\\b")
                )),
                token("boolean", pattern(compile("\\b(?:true|false)\\b"))),
                token("nil", pattern(compile("\\bnil\\b"), false, false, "constant")),
                token("short-argument", pattern(compile("\\$\\d+\\b"))),
                token("omit", pattern(compile("\\b_\\b"), false, false, "keyword")),
                token("number", pattern(
                        compile("\\b(?:[\\d_]+(?:\\.[\\de_]+)?|0x[a-f0-9_]+(?:\\.[a-f0-9p_]+)?|0b[01_]+|0o[0-7_]+)\\b", CASE_INSENSITIVE)
                )),
                token("class-name", pattern(compile("\\b[A-Z](?:[A-Z_\\d]*[a-z]\\w*)?\\b"))),
                token("function", pattern(compile("\\b[a-z_]\\w*(?=\\s*\\()", CASE_INSENSITIVE))),
                token("constant", pattern(compile("\\b(?:[A-Z_]{2,}|k[A-Z][A-Za-z_]+)\\b"))),
                token("operator", pattern(compile("[-+*/%=!<>&|^~?]+|\\.[.\\-+*/%=!<>&|^~?]+"))),
                token("punctuation", pattern(compile("[{\\}\\[\\]();,.:\\\\]")))
        );

        swift.insertBeforeToken("directive",
                token("string-literal",
                        pattern(
                                compile("(^|[^\"#])(?:\"(?:\\\\(?:\\((?:[^()]|\\([^()]*\\))*\\)|\\r\\n|[^(])|[^\\\\\\r\\n\"])*\"|\"\"\"(?:\\\\(?:\\((?:[^()]|\\([^()]*\\))*\\)|[^(])|[^\\\\\"]|\"(?!\"\"))*\"\"\")(?![\"#])"),
                                true,
                                true,
                                null,
                                grammar("inside",
                                        token("interpolation", pattern(compile("(\\\\\\()(?:[^()]|\\([^()]*\\))*(?=\\))"), true, true, null, swift)),
                                        token("interpolation-punctuation", pattern(compile("^\\)|\\\\\\($"), false, false, "punctuation")),
                                        token("punctuation", pattern(compile("\\\\(?=[\\r\\n])"))),
                                        token("string", pattern(compile("[\\s\\S]+")))
                                )
                        ),
                        pattern(compile("(^|[^\"#])(#+)(?:\"(?:\\\\(?:#+\\((?:[^()]|\\([^()]*\\))*\\)|\\r\\n|[^#])|[^\\\\\\r\\n])*?\"|\"\"\"(?:\\\\(?:#+\\((?:[^()]|\\([^()]*\\))*\\)|[^#])|[^\\\\])*?\"\"\")\\2"), true, true, null,
                                grammar("inside",
                                        token("interpolation", pattern(compile("(\\\\#+\\()(?:[^()]|\\([^()]*\\))*(?=\\))"), true, false, null, swift)),
                                        token("interpolation-punctuation", pattern(compile("^\\)|\\\\#+\\($"), false, false, "punctuation")),
                                        token("string", pattern(compile("[\\s\\S]+")))
                                )
                        )
                )
        );

        return swift;
    }
}
