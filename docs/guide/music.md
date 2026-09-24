# Musical keypad

Every key plays a sound, like a Chinese musical calculator. The digits are tuned so that songs written in numbered notation (jianpu) can be played straight from the keypad, and `C` plays the 归零 ("back to zero") sound.

<CalculatorDemo tunes notes />

## Notes

The digits play the B♭ major scale, with `0` one octave below `1`:

| Key | Note | Numbered notation |
| --- | --- | --- |
| `0` | B♭3 | low 1 |
| `1` | B♭4 | 1 |
| `2` | C5 | 2 |
| `3` | D5 | 3 |
| `4` | E♭5 | 4 |
| `5` | F5 | 5 |
| `6` | G5 | 6 |
| `7` | A5 | 7 |
| `8` | B♭5 | high 1 |
| `9` | C6 | high 2 |

Numbered notation writes the scale as 1 to 7. A dot below a number means one octave lower and a dot above means one octave higher, so low 1 is `0`, high 1 is `8` and high 2 is `9`.

The other keys stay out of the way of a melody:

- `+`, `−`, `×`, `÷` and `%` play an octave or more below the digits.
- `=`, `±` and `DEL` play short figures of a few notes. `=` is a rising chime.
- `C` plays the 归零 sound. No other key uses its pitch.

## Playing tunes

Each press cuts off the note before it, so fast playing stays in time instead of blurring into chords. Typing on the keyboard is easier than clicking for anything quick.

After 15 digits the display is full. Digits still play, but are no longer added. Press `C` or <kbd>Delete</kbd> to clear it, which plays the 归零 sound.

## Muting

The `♪` key below the keypad mutes the keypad. The keys still work, just silently. Unmuting plays the `=` chime.

In game, the setting lasts until you quit the game and applies to every calculator.
