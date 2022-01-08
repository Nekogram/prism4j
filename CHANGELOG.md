# Changelog

## 2.0.0

* package name migration: `ru.noties.prism` -&gt; `io.noties.prism`
* maven artifact group-id change: `ru.noties` -&gt; `io.noties`
* removed android suppport annotations, a light-weight alternative from Intellij is used (support annotations were added
  to generated code which jetifier cannot process)

# 1.1.0

## Core

* Fix issue when cloning a grammar was causing stack overflow (recursive copying)
* Fix issue with recursive toString in Grammar, Token adn Pattern
* Add `includeAll` option for `@PrismBundle`
* Rename `name` -> `grammarLocatorClassName` in `@PrismBundle`
* Allow `include` option to be empty in `@PrismBundle` in case of `includeAll` is set
* add `languages()` method to `GrammarLocator` to return all included languages

## Bundler

* Add `languages()` method to generated `GrammarLocator` class
* Allow `includeAll` option (include all available languages)
* Add language support:
    * `groovy` (no string interpolation)
    * `markdown`
    * `scala`
    * `swift`

# 1.0.1

## Bundler

* Fix for `javascript` and `yaml` grammar definitions to work on Android (escape `}` char in regex's)