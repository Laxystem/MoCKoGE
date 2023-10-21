# MoCKoGE Versioning Guidelines

## Semantic Versioning

Please read the [SemVer 2.0.0 Specification](https://semver.org) before reading this document.

## Wordings

- Releasing—publishing a non-staged version.
- Publishing—uploading binaries to maven.
- Staged—a version that has the [`STAGE`](#stage) element.
- Source compatible—source code does not need to be changed.
- Binary compatible—code does not need to recompile.
- Feature—a class, function, or even an entire API.
- Dependency—a library exposed as an API.

## Usage

```
    MAJOR.MINOR.PATCH-STAGE
```

### Stage

```
    TYPE.BUILD
```

A release must go through three stages:

1. [`alpha`](#alpha) - development of the API, inherently unstable.
2. [`beta`](#beta) - perfection of final details and bug fixing.
3. [`rc`](#release-candidate) - release candidate.
4. Final release, not staged.

During a stage, an unstable build may be published.
For every build published, increase the `BUILD` element.
When changing the stage, the `COUNT` resets.

The `COUNT` starts at one.

#### Alpha

Used for quick prototyping. Nothing is guaranteed.

#### Beta

Used for perfecting and bug-fixing. The current API is likely to be the final release.

Beta stages might devolve into alpha stages.
In that case, return to the previous alpha count.

```
1.7.2 -> 1.7.3-alpha.1 -> 1.7.3-beta.1 -> 1.7.3-alpha.2 -> 1.7.3-beta.2
```

#### Release Candidate

Before releasing, a build must first be published as a release candidate.
If no bugs are found, it may be fully released after a set time:

- Major releases may release after two weeks,
- Minor releases after two days, and
- Patch releases after twenty minutes.

Additional release candidates must be candidated for the entire period, except for major release candidates,
that may be candidated for one week instead of two.

Release candidates may be converted

### Patch

Patch releases are binary- and source-compatible, and do not affect public API at all.
This includes bugfixes that change relied-upon behaviour.

Patch releases may not update dependencies.

### Minor

Minor releases change public API, preserving source- and binary-compatibility, and may [deprecate](#deprecation) features.

Minor releases may update dependencies that haven't broken source compatibility.

#### Deprecation

Deprecated features have a replacement available or in development.

- Deprecated features with a stable replacement, aka _deprecated for removal_,
  will be removed in the next [major](#major) release, and issue a compilation warning when used.
- Features whose replacement isn't stable do not issue a warning—they're deprecated solely to indicate that a
  replacement is being developed.

### Major

Major releases break backwards compatibility completely.

Do not release more than one major release per three months.

Major releases _must_ update all dependencies to the latest possible.

<!-- use some kind of special way to mark them, e.g. september 2023 update to 2309.X.Y? -->
