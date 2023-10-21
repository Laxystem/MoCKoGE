# MoCKoGE contribution guidelines

## Wordings
See the [versioning guide](VERSIONING.md#wordings).

- Code Element—a function, property, constructor, class, etc.

## Versioning
Before contributing, read the [versioning guide](VERSIONING.md).
It applies to _all_ MoCKoGE modules.

## Code Style

See the [official kotlin article about coding conventions](https://kotlinlang.org/docs/coding-conventions.html).

Don't use fake constructors.

```kotlin
data class Identifier(val namespace: String, val path: String) {
    companion object
}

operator fun Identifier.Companion.invoke(identifier: String): Identifier = TODO() // bad
fun Identifier(identifier: String): Identifier = TODO() // bad

fun String.toIdentifier(): Identifier = TODO() // good
```

Prefer `object`s and `const`s to properties.

```kotlin
val property = "myString" // bad
object Object : CharSequence by "myString" // fine, try to avoid
const val const = "myString" // good
```

Always try to create interfaces when possible.
Registries should only be created for interfaces.

```kotlin
interface Activator { // good
    fun activate()
}

class SimpleActivator : Activator { // good
    override fun activate() = TODO()
}

class HardcodedActivator { // try to avoid
    fun activate(): Unit = TODO()
}

object HardcodedActivatorRegistry // bad
object ActivatorRegistry // good
```

Use proper naming.

```kotlin
interface IActivator // bad
interface Activator // good

class ActivatorImpl : IActivator // bad
class SimpleActivator: Activator // good

object ActivatorSinglet : IActivator // bad
object DefaultActivator : Activator // good
```

Prefer inner classes.

```kotlin
class Entity {
    inner class Component { // good
        /**
         * Allows external access to the outer class. Not mandatory.
         */
        val entity get() = this@Entity
    }
}

class Component(entity: Entity) // bad
```

Avoid nested implementation classes inside interfaces.
The default implementation should be a delegated companion object.

```kotlin
interface Activator { // good
    class SimpleActivator : Activator // bad

    object DefaultActivator : Activator // bad, should be companion
    companion object Default : Activator by SimpleActivator() // good
}

class SimpleActivator : Activator // good

object DefaultActivator : Activator // bad, should be companion
```

For default implementations of sealed interface hierarchies, delegate from bottom to top.

```kotlin
sealed interface Activator {
    interface Dynamic : Activator {
        companion object Default : Dynamic by SimpleDynamicActivator()
    }
    
    interface Static : Activator {
        companion object Default : Static by SimpleStaticActivator()
    }
    
    companion object Default : Activator by Static.Default
}

class SimpleDynamicActivator : Activator.Dynamic
class SimpleStaticActivator : Activator.Static
```

### Backwards Compatibility
When modifying the signature of a code element in a source- or binary-incompatible way,
create a new function, and deprecate the old one (its functionality, however, should remain the same).

#### Binary backwards compatibility

Always preserve binary backwards compatibility,
excluding [major](VERSIONING.md#major) releases.

This includes changing annotations such as `@JvmName`, `@JvmStatic`, `@JvmField`, etc.

##### Nth component operator

Don't use nor declare the `componentN` operators,
with an exception for tuples (`Pair`, `Triple`, etc).

```kotlin
operator fun component1(): Any = error("Don't use these!")
```

##### Handling Data Classes

Always annotate primary constructor of data classes with `@JvmOverloads`.
Don't remove, modify, or reorder properties.

Only add new properties to the end of the `constructor`,
and always give them default values.

```kotlin
data class MyDataClass @JvmOverloads constructor(
    val iWasHereFromTheBeginning: Int,
    val iAmNew: Int = 43
)
```

#### Source backwards compatibility

-- TODO --

### Documentation

Always document. Prefer not using the following tags:
 - `@param`
 - `@reciever`
 - `@return`

<details>
<summary>Example</summary>

#### Do

```kotlin
/**
 * Formats an int into an hexadecimal string.
 * 
 * If the resulted string is below the [minimumLength],
 * adds [filler]s to the beginning of the string to match it.
 */
expect fun Int.toHexString(minimumLength: Int? = null, filler: Char = '0'): String
```

#### Don't Do

```kotlin
/**
 * @receiver the [Int] to be formatted
 * @param minimumLength the minimum length of the output
 * @param filler the character to be inserted to match the minimum length
 * @return a hexadecimal string
 */
expect fun Int.toHexString(minimumLength: Int? = null, filler: Char = '0'): String
```

</details>

However, _always_ document the following tags:
- `@property`

### Interop with other languages

Assume Kotlin is the sole language interacting with MoCKoGE directly.
Don't optimise code for other languages.
Use Kotlin whenever possible.

#### Always use Kotlin conventions

- Convert Java `Optional<T>` to `T?`
- etc.

## Packages, modules and subproject

### Subprojects

All subprojects that depend on `mockoge` (`:core`) must declare a _bundle_, even if it's empty—to allow dependency
relations.

An official bundle's subproject is determined by its namespace.

- `mockoge/networking/host` *becomes* `:networking:host`
- `mockoge/input/clocal` *becomes* `:input:local`

If a bundle has _extension bundles_, add `:core`.

- `mockoge/graphics/vulkan` *becomes* `:graphics:vulkan`
- `mockoge/graphics` *becomes* `:graphics:core`

Always create the subproject in `...:core` if extension bundles are planned or likely.

#### Special Subprojects

- `:...core` subprojects' bundles must be of type `core`
- `:...local` subprojects' bundles must be of type `local`
- `:...host` subprojects' bundles must be of type `host`

#### Referencing subprojects

When talking about a subproject, use its namespace whenever possible.

### Packages

An official bundle's package is determined by its namespace. For example:

- `mockoge` *becomes* `quest.laxla.mockoge`
- `mockoge/graphics/vulkan` *becomes* `quest.laxla.mockoge.graphics.vulkan`

#### Special Packages

All packages named below have special effects applied on them and their subpackages.

- `quest.laxla.mockoge...internal` packages ,ist not declare _anything_ as `public`.
- `quest.laxla.mockoge...experimental` packages are always empty of declarations.
  Their subpackages all declare an `@ExperimentalMockoge<FeatureName>Api` annotation (annotated with `@RequiresOptIn`),
  and all their declarations must be annotated with said annotation. Backward compatibility rules do not apply here.
- `quest.laxla.mockoge...generated` packages contain only code-genned declarations,
  and `expect` declarations (note non-code-genned `actual`s aren't allowed)

#### Referencing Packages

Whenever possible, use the [subproject's naming](#referencing-subprojects),
followed by the package (excluding `quest.laxla.mockoge`)

- `quest.laxla.mockoge.graphics.vulkan` of `mockoge/graphics/mockoge` *stays* `mockoge/graphics/vulkan`
- `quest.laxla.mockoge` of `mockoge` *stays* `mockoge`
- `quest.laxla.mockoge.loader` of `mockoge` *becomes* `mockoge.loader`
- `quest.laxla.mockoge.networking.codec.encoder` of `mockoge/networking` *becomes* `mockoge/networking.codec.encoder`

### Source Sets & Targets

#### Source set specific bundles

Do not use `expect` and `actual` for bundles - they'll be ignored. Instead - create a bundle for `commonMain`,
and create extension bundles for each source set that needs it.

#### Referencing Source Sets & Targets

Whenever possible, use the [package](#referencing-packages) or [subproject](#referencing-subprojects) naming, followed
by a hashtag, then the target/source set name

- `quest.laxla.mockoge.graphics.vulkan` of `mockoge/graphics/vulkan` on target `jvm`
  _becomes_ `mockoge/graphics/vulkan#jvm`
- `quest.laxla.mockoge.networking.codec.encoder` of `mockoge/networking` on source set `javaMain`
  *becomes* `mockoge/networking.codec.encoder#javaMain`

## GitHub [Project](https://github.com/users/LaylaMeower/projects/1/views/1), PRs & Issues

### Respect PR and issue topics

Comments on Pull/Push Requests (referenced to as "PR") are for discussion about implementation of the feature,
mainly (but not limited to) code reviews.

Meanwhile, the issue is more about the feature itself.

### Only create a PR for an existing issue

Never create a PR before creating an issue.
Always link the PR to the issue.

### For tasks, create the fewest issues as possible

However, always turn a task into an issue in the following cases:

- The task can (and should) be released separately from before the rest of the issue
- The task is of a different subproject than the rest of the issue
- The task creates a new subproject.
- The task is implemented in its own PR (see *[Respect PR and issue topics](#respect-pr-and-issue-topics)*)

A task may turn into an issue if any of the above requirements have been fulfilled after its creation.
However, Issues cannot be turned back into tasks.

<details>
<summary>Example</summary>

In the example below, **Task #3** is implemented in the same PR,
cannot be released separately, and belongs to the same module.

#### Do

> ##### Issue #1
> - [ ] Task 1
> - [ ] Task 2
> - [ ] Task 3
>   - [ ] Task 3.1
>   - [ ] Task 3.2
> - [ ] Task 4

#### Don't Do

> ##### Issue #1
> - [ ] Task 1
> - [ ] Task 2
> - [ ] Issue #3
> - [ ] Task 4
>
> ##### Issue #3
>
> - [ ] Task 3.1
> - [ ] Task 3.2

</details>

### Use project drafts properly

Never create issues directly—first create a draft, then, when you feel it is ready for discussion,
convert it to an issue.
Make sure to assign it to a _module_ (if applicable), and give it the _Under Discussion_ status beforehand.

You may convert a draft to an issue earlier if it needs to be declared as a task of another issue,
or declare an issue as a task of its own.
