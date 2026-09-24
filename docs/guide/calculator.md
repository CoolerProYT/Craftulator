# Using the calculator

<CalculatorDemo />

The top line of the display shows **READY**, or the pending operation, like `12 ×`. The bottom line shows the number you are typing or the latest result.

## Keys

| Key | What it does | Keyboard |
| --- | --- | --- |
| `0`–`9` | Types a digit. Numbers can be up to 15 digits long. | <kbd>0</kbd>–<kbd>9</kbd> |
| `.` | Adds a decimal point. Starts a new number as `0.` | <kbd>.</kbd> or <kbd>,</kbd> |
| `+` `−` `×` `÷` | Chooses the operation. Pressing another one right after replaces it. | <kbd>+</kbd> <kbd>-</kbd> <kbd>*</kbd> or <kbd>x</kbd> <kbd>/</kbd> |
| `=` | Shows the result. | <kbd>Enter</kbd> or <kbd>=</kbd> |
| `%` | Divides the shown number by 100. | <kbd>%</kbd> |
| `±` | Flips the sign of the shown number. | |
| `DEL` | Deletes the last digit you typed. | <kbd>Backspace</kbd> |
| `C` | Clears everything. | <kbd>Delete</kbd> or <kbd>C</kbd> |
| `♪` | Mutes or unmutes the keypad. See [Muting](./music#muting). | |

## How it calculates

It works like a basic pocket calculator, one step at a time from left to right. There is no operator precedence: `2 + 3 × 4 =` gives `20`, not `14`.

- **Chained operations.** Pressing an operator after a second number works out the result so far: `2 + 3 ×` shows `5`, then waits for the next number.
- **Repeat equals.** Pressing `=` again repeats the last operation: `5 + 2 = = =` gives `7`, `9`, `11`.
- **Reuse the number.** Pressing `=` straight after an operator uses the shown number twice: `6 × =` gives `36`.

## How results are shown

- Trailing zeros are dropped, so `0.5 × 4 =` shows `2`.
- Results longer than 16 characters switch to scientific notation, like `1.23456789e20`.
- Values closer to zero than 0.000000000001 show as `0`.

## Errors

Dividing by zero, or a result too large to store, shows **Error**. Typing a digit or `.` starts a fresh calculation, and `DEL` or `C` clears it. Other keys do nothing until then.
