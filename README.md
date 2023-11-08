# MoCKoGE

> *The **Mo**dular **C**oncurrent **Ko**tlin **G**ame **E**ngine*

## Creating a bundle script

The first step in using MoCKoGE is creating your bundle script.
Bundle scripts are kotlin files declaring your bundle's:
- Version
- Dependencies, other bundles that you depend on
- Registry definitions
- Registrations, things that you register into registries.
- [Extensions](#extensions), other bundles made by you that depend on this bundle and extend it

Bundle scripts are extremely adaptable—if you change a bundle's namespace,
its bundle script will remain unmodified.
All you'll have to do is rename it.

### Create your bundle script file

First, create your bundle script file.
It should be named `<namespace>.bundle.kts`,
and placed in the `resources` directory of your module.

### Macros and bundle version

Second, declare your bundle's version and the mockoge version that you use:
```kotlin
version = "$projectVersion"
mockoge = "$mockogeVersion"
```

In the bundle script, you have some macros available:
- `projectVersion`, your project's `version` variable declared in the gradle build script
- `mockogeVersion`, the version of the bundle gradle plugin you're using
- More macros may be added in the future.

Macros can only be used as template expressions:
```kotlin
version = "$projectVersion" // good, will work
version = projectVersion // runtime error
```

Your IDE might complain about an unnecessary template expression.
Add the following line to the top of your bundle script.
```kotlin
@file:Suppress(UnnecessaryStringTemplate)
```

### Dependencies

Next, you'll want to declare your dependencies.
There are four types of dependencies available:
- `Required`
- `Optional`
- `Incompatible`, does not support registration
- [`Extension`](#extensions), does not support registration

Their namings should be self-explanatory (extensions will be explained thoroughly below).

The `namespace` block is not required, and might be unsupported by the dependency type.

If you don't explicitly state a dependency's version, it'll be ignored.

```kotlin
// type             namespace            version
TODO("Choose a dependency type") dependency "my_library" version any namespace {
    // register to my_library's registries
    "registry_path"["entry_path"] = TODO("registry entry here!")
}
```

For more information about `version`, see [below](#versions-and-constraints).

#### Official Dependencies

If you wish to use an official mockoge module,
use the `module` function instead of `dependency` and `version`.
For example, to use [`mockoge/graphics`](graphics/README.md) as a required dependency:

```kotlin
Required module "graphics"
// or
Required module "graphics" namespace {
    // etc...
}
```

#### Registering to a core mockoge registry

To register something to a registry inside the [`mockoge`](core/README.md) bundle,
use the `mockoge` block.
Its syntax is identical to the `namespace` block.

```kotlin
mockoge {
    // etc...
}
```

> [!NOTE]
> Do not use this to register your own registry to `mockoge:root` manually,
> see [below](#create-your-own-registries) instead.

### Create your own registries

It's as simple as naming it and listing its built-in contents.
The `contains` block is optional.

```kotlin
TODO("Create a registry") aka "registry_path" contains {
    TODO("Create a registry entry") at "entry_path"
}
```

### Extensions

Extensions are a special dependency type.
They allow _other_ bundles to use this bundle's namespace!

An extension's namespace is added on top of its base bundle using a slash: `base/extension`

Bundles may be both an extension and a base of one or more extensions at the same time.

Because a bundle's namespace is decided by its script's file name,
and operating systems already use the `/` to denote folder hierarchy,
use dots instead, writing the namespaces in opposite order.

Example: `base/base_and_extension/extension`'s bundle script is named `extension.base_and_extension.base.bundle.kts`.

That way, the base bundle's namespace becomes a part of the extension's file extension.

Do note this behavior may be changed,
as Windows doesn't like file names longer than 255 characters,
and this can get long fast.

To form an extension, the base bundle must declare an extension dependency.
Make sure _not_ to include the base bundle's namespace.

Extensions are required to also specify what version of their base they require,
using a syntax similar to specifying the MoCKoGE version the bundle requires.

```kotlin
base = TODO("version...")
```

### Versions and constraints

MoCKoGE uses the amazing [Kotlin Semver](https://github.com/z4kn4fein/kotlin-semver) library by [Peter Csajtai](https://github.com/z4kn4fein),
with some additions.

It supports two modes of parsing:
- Strict parsing, that parses the version _exactly_ accordingly to the [SemVer Specification](https://semver.org).
- Loose parsing, that allows some common irregularities.

Strings now have the `toVersion` and `toConstraint` extension methods.
For more information about how they are parsed,
see [Kotlin Semver's Documentation](https://github.com/z4kn4fein/kotlin-semver).

#### MoCKoGE Semver Extensions
_MoCKoGE is FOSS, and [source code](core/src/commonMain/kotlin/quest/laxla/mockoge/util/VersionDSL.kt) is available!_

MoCKoGE adds special syntax for creating constraints.
Some functions have an overload that accepts it,
but otherwise, you can use the `quest.laxla.mockoge.util.constraint { }` function.

Inside the constraint block,
multiple constraints inside a single string are treated as _and_,
And you can use the `or` infix function to create an _inclusive or_.
The quotation marks are treated like parentheses in math.

And lastly, you can use `Version.toApproximateConstraint()` and `toExactConstraint()`,
which are equivalent to `~$version` and `=$version`.

## Contributing
See the guides about [contributing](CONTRIBUTING.md) and [versioning](VERSIONING).

## Project Structure

MoCKoGE is separated to subprojects:
- [Core](core/README.md)
- [Graphics](graphics/README.md)
