# Prism4j

Simplified Java clone of [Prism4j](https://github.com/PrismJS/prism). No rendering, no themes, no hooks, no plugins. But
still _a_ language parsing. Primary aim of this library is to provide a _tokenization_ strategy of arbitrary syntaxes
for later processing. Works on Android (great with [Markwon](https://github.com/noties/Markwon) - markdown display
library).

This is a fork of [noties/Prism4j](https://github.com/noties/Prism4j) which is unmaintained since 2019. Some features
were removed from the project to make it easier to use it (e.g. the `PrismBundler`).

## Usage

if you are using any Maven-compatible build system, you can get this via
[Jitpack](https://jitpack.io/#org.codeberg.qwerty287/Prism4j).

```java
class Prism4jExample {
    static void example() {
        final Prism4j prism4j = new Prism4j();
        final Grammar grammar = prism4j.grammar("json");
        if (grammar != null) {
            final List<Node> nodes = prism4j.tokenize(code, grammar);
            final AbsVisitor visitor = new AbsVisitor() {
                @Override
                void visitText(@NonNull Prism4j.Text text) {
                    // raw text
                    text.literal();
                }

                @Override
                void visitSyntax(@NonNull Prism4j.Syntax syntax) {
                    // type of the syntax token
                    syntax.type();
                    visit(syntax.children());
                }
            };
            visitor.visit(nodes);
            // instead, you could also use (same effect):
            //prism4j.visit(visitor, code, "json");
        }
    }
}
```

In order to simplify adding language definitions to your project, Prism4j has some available languages:

* `basic`
* `brainf*ck`
* `c`
* `clike`
* `clojure`
* `cpp`
* `csharp` (`dotnet`)
* `css` (+`css-extras`)
* `dart`
* `git`
* `go`
* `groovy` (no string interpolation)
* `haxe`
* `ini`
* `java`
* `javascript` (`js`)
* `json` (`webmanifest`)
* `jsonp`
* `kotlin`
* `latex` (`tex`, `context`)
* `makefile`
* `markdown`
* `markup` (`xml`, `html`, `mathml`, `svg`)
* `python`
* `regex`
* `scala`
* `sql`
* `swift`
* `yaml`

### Adding a language that is not supported

1. The best way is to port the language and create a pull request to this repository so everyone can use your language
   definition. Please see [`Add languages`](#add-languages) section.
2. You can also follow the instructions under [`Add languages`](#add-languages) to add a language directly to your
   project. If you use this approach, you'll have to create a new class which implements `GrammarLocator` and create new
   Prism4j instances with `new Prism4j(new MyGrammarLocator())`. However, I'd really appreciate it if you would create a
   pull request!

### Important

The `DefaultGrammarLocator` will create languages when they are requested (aka _lazy_ loading). Make sure this works for
you by keeping as is or by manually triggering language creation via `prism4j.grammar("my-language");` when convenient
at runtime.

## Add languages

Language definitions are at the `prism4j/src/main/java/io/noties/prism4j/languages/` folder. A new file should follow
simple naming convention: `Prism_{real_language_name}.java`. So, a definition for `json` would be `Prism_json.java`.

You can mostly copy definitions and regular expressions from [PrismJS](https://github.com/PrismJS/prism).

In order to provide meta-information about a language which will be used to generate the `DefaultGrammarLocator` use the
`@Aliases` and `@Modify` annotations:

* `@Aliases` specifies what aliases a language has. For example `markup` language has
  these: `@Aliases({"html", "xml", "mathml", "svg"})`. So when a `GrammarLocator` will be asked for a `svg` language
  the `markup` will be returned.

```java
@Aliases({"html", "xml", "mathml", "svg"})
public class Prism_markup {
}
```

* `@Modify` annotation makes sure that if a language definition modifies another one, modified language will be
  processed before returning to a caller. This does not include a language that is being modified to a project. But if
  it's present, it will be modified. `@Modify` accepts an array of language names and a boolean value which should be
  added if the language is built on top of another language and should replace this language (see `css-extras` as
  example). For example, `css`:

```java
@Modify("markup")
public class Prism_css {
}
```

---

After you are done (haha!) with a language definition please make sure that you also move test cases
from [PrismJS](https://github.com/PrismJS/prism) for the project (for newly added language of course). Thankfully just a
byte of work required here as the test module understands native format of _prism-js_ test cases (that are ending
with `*.test`). Some PrismJS tests have a test description below the actual test data. If you copy any test without
adding this description, Prism4j will throw an `ArrayOutOfBoundsException`. Please inspect the test folder for further
info. In short: copy test cases from PrismJS project (the whole folder for specific language)
into `prism4j/src/test/resources/languages/` folder.

Then, if you run:

```bash
./gradlew :prism4j:test
```

and all tests pass (including your newly added), then it's _safe_ to issue a pull request. **Good job!**

### Important note about regex for contributors

As this project _wants_ to work on Android, your regex's patterns must have `}` symbol escaped (`\\}`). Yes, _an_ IDE
will warn you that this escape is not needed, but do not believe it. Pattern just won't compile at runtime (Android). I
wish this could be unit-**tested** but unfortunately Robolectric compiles just fine (no surprise actually).
