`X` before something = it's finished

# Quick Notes

## Next Update

## Bugs

# Releases

## X 2.1
- Added cooldowns to `Command`s
- Added a class `DurationFormatter` in base API
- Removed `@NotLoaded` as loaded classes require `@AutoRegistered` since 2.0, making it obsolete
- `OnteyPlugin` now only registers serializables that extend `ontey.api.serialization.ConfigSerializable` which connect OnteyAPI and Bukkit serializables by extending both.
  Also made serializables require `@AutoLoaded` to be ... autoloaded.
- Added more registerSerializable and made them understandable: `registerSerializable` for `ontey.api.serialization.ConfigSerializable`, `registerBukkitSerializable` and `registerOnteySerializable`.
- Deprecated `OnteyPlugin#registerSerializable(ConfigurationSerializable)`
- Added a check for `@AutoLoaded` in `Loaders#createSubclassLoader`.

## X 2.0.4
- `CommandConfig#deserialize(ConfigSection)`
  - Added a null-check (and `throws IllegalStateException`) for the `name`
  - Made `enabled` default to `true` instead of `false` (which didn't make sense)
  - Added documentation
- Added `getEnum` and `isEnum` methods to `ConfigSection`
- Added a getter for `context` in `Javascript`
- Minor code cleanup

## X 2.0.1
- Changed static methods `getJavaPlugin` and `getOnteyPlugin` in `OnteyPlugin` to be public instead of
  protected (old convention) and show the full class name instead of the simple name in the `Exception`.
- Updated README.md - Removed `PluginLoader` dependency example and added an explanation as to why.
- Made JitPack also create fat jars

## X Version 2.0
- X Fix Command API. Commands don't register at all.
- X Fix Loader API. When OnteyAPI loads its Loaders, an Exception is thrown.
- Gui API, add builder API in the `Ease-Up-Date`.
- X Fix that commands registered by the CommandAPI override the first literal's `requirement` Predicate
- X Make `Command` and its subclasses directly extendable and remove the registerer interfaces
- X Loosen use of using `OnteyPlugin` and replace with `Plugin` or `JavaPlugin` or the direct resources that are needed
  of the `OnteyPlugin`
- X Documentation for the base OnteyAPI

## X 1.3.3
- Added `Loader`, `SubclassLoader` and `SingleClassLoader` class
- Added `@NotLoaded` which prevents classes from being loaded by the `SubclassLoader`
- Added `@PreferredConstructor` that makes the annotated constructor gain precedence over the others
- Set `@CommandName`'s target to `TYPE`
- Added ThrowableFunction which is a ThrowingFunction that might throw `Throwable`
- Added `loaders` field to `OnteyPlugin`

# Update Ideas

## Maths - not important
- add var/func/op definition in Strings, ex. `var x = 1`, `var y = (x + 1) * 2`

## Enchantment Update
- Add an Enchantment API.
  Do research, dive into the minecraft code and use NMS.
  Look in 21.6 enchantment plugin's sources (if any available)
  Also, look if it's even needed. Maybe Paper/NMS API is already good.

## Command API update
- Enhance `Arg` class OR create own ArgumentBuilder implementations.
  Current: `.executes(ctx -> Arg.something(ctx, ...))`.
  Goal: `.executes(Arg.something(...))`. Make it create a `Command` lambda to make this work.
- Cooldowns and Warmups

## MiniMessage color and Placeholder update
- Add a placeholder system for mini-message (Components)
- Add the PlaceholderAPI resolver in MiniMessageColor there

## PluginLoader test
- Test if downloading the API with PluginLoader works, update README.md accordingly.

## ActionSection update
- Make ActionSection.txt a class again.
- Add JavaScript field
- ...

## Menu API
- Player inventories, Anvils...
- Look at current Dialog API.
  Maybe create a Dialog Builder.

## Config API update
- make deserialization in Configs more pleasant.
  Replace `Map` with either `ConfigSection` or an adjusted helper class that wraps `Map` and has simple getter methods for all native ConfigSection types.

## Ease-Up-Date - Builders and potentially other simplification helpers
- Scoreboard builder
- Recipe Builder
- GUI Builders (for existing GUI API)

## Loader API
- Loader API with SubclassLoader

## Discord integration into paper
- Add a class (maybe in a separate module) that simplifies this.