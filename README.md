# Craftulator

A Minecraft mod that adds a working calculator item, with a keypad that plays a musical note on every button — the way a Chinese musical calculator does, so tunes like 归零 can be played straight off the digits.

Built for Minecraft 26.2 on both **Fabric** and **NeoForge**.

## Features

### The calculator
Right click the Calculator item, or press the keybind, to open it. Everything works with the mouse or the keyboard: digits, `+ − × ÷`, `%`, `±`, `DEL`, `.` and `=`, plus <kbd>Enter</kbd> for equals, <kbd>Backspace</kbd> for delete and <kbd>Delete</kbd> for clear. The display keeps a hint of the pending operation, falls back to scientific notation for long results, and shows an error state for division by zero.

### Crafting

Glass panes for the screen, stone buttons for the keypad, and a note block between two iron ingots for the body:

```
G G G      G = Glass Pane
B B B      B = Stone Button
I N I      I = Iron Ingot, N = Note Block
```

### Musical keypad
The digits follow numbered (jianpu) notation in Bb major, so a tune written in numbers can be typed key for key:

| 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 |
|---|---|---|---|---|---|---|---|---|---|
| Bb3 | Bb4 | C5 | D5 | Eb5 | F5 | G5 | A5 | Bb5 | C6 |

The operators sit an octave or more below that range so they never muddy a melody, `=` plays an ascending chime, and `C` plays the 归零 sound. Each press cuts off the previous note, so quick playing stays in time instead of piling up into chords. The `♪` key below the keypad mutes and unmutes the tones, and stays that way until the game restarts.

### Keybind
<kbd>C</kbd> by default, rebindable under Controls → Craftulator. It opens the calculator when one is in either hand, or worn in an accessory slot.

### Accessory support
The calculator can be worn in a dedicated **Calculator** slot, through either accessory mod:

| Mod | Loader |
| --- | --- |
| [Curios](https://github.com/TheIllusiveC4/Curios) | NeoForge |
| [Trinkets](https://github.com/patbox/trinkets) | Fabric and NeoForge |

Both are optional — with neither installed, the mod behaves exactly as it would without accessory support. Which mod is present is resolved at runtime, so on NeoForge with both installed, the keybind finds the calculator in whichever one holds it.

## Building

```bash
./gradlew build
```

Finished jars land in `fabric/build/libs` and `neoforge/build/libs`. To launch a development client:

```bash
./gradlew :fabric:runClient
```

```bash
./gradlew :neoforge:runClient
```

## Project layout

This is a [MultiLoader](https://github.com/jaredlll08/MultiLoader-Template) style project, so most of the mod lives in `common`.

| Project | Contents |
| --- | --- |
| `common` | The mod itself, compiled against the vanilla game — items, screen, sounds, keybinds, and the platform service interfaces |
| `fabric` | Fabric entrypoints and the Fabric implementations of those services |
| `neoforge` | NeoForge entrypoints, service implementations, and the Curios integration |

Anything a loader does differently goes through a service interface in `common/platform/services`, implemented once per loader and looked up with `ServiceLoader` (`Services.REGISTRY`, `Services.COMPAT`, and so on). The loader projects can see `common`, but never the reverse.

Code that touches an optional mod's classes lives in its own class — `CuriosCompat`, `TrinketsCompat` — and is only ever called behind a mod-loaded check, so those classes are never loaded when the mod is absent.

## Development setup

The project targets **Java 25**. In IntelliJ IDEA, open the folder containing `gradlew` as a project; if your default JDK is older, set `File > Settings > Build, Execution, Deployment > Build Tools > Gradle > Gradle JVM` and the Project SDK to a Java 25 JVM, then reload the Gradle project. Fabric and NeoForge run configurations appear under the `Application` category once it imports.

Eclipse is not supported by the underlying template.

## License

[MIT](LICENSE).
