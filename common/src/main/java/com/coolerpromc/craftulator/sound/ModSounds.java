package com.coolerpromc.craftulator.sound;

import com.coolerpromc.craftulator.Constants;
import com.coolerpromc.craftulator.platform.Services;
import com.coolerpromc.craftulator.platform.util.RegistryHandler;

/**
 * Button tones for the calculator, laid out like a Chinese musical calculator.
 * <p>
 * The digits follow numbered (jianpu) notation in Bb major so tunes can be
 * played straight off the keypad, with 0 an octave below 1:
 * <pre>
 *   0 Bb3  1 Bb4  2 C5   3 D5   4 Eb5
 *   5 F5   6 G5   7 A5   8 Bb5  9 C6
 * </pre>
 * Operators sit an octave or more below that range so they never muddy a
 * melody, while =, ± and ⌫ play short multi note figures. C plays the 归零
 * sound, and nothing else sits on its pitch.
 */
public class ModSounds {
    public static final RegistryHandler.Sounds[] DIGITS = {
            register("calculator.digit.0"),
            register("calculator.digit.1"),
            register("calculator.digit.2"),
            register("calculator.digit.3"),
            register("calculator.digit.4"),
            register("calculator.digit.5"),
            register("calculator.digit.6"),
            register("calculator.digit.7"),
            register("calculator.digit.8"),
            register("calculator.digit.9")
    };

    public static final RegistryHandler.Sounds DECIMAL = register("calculator.decimal");
    public static final RegistryHandler.Sounds SIGN = register("calculator.sign");
    public static final RegistryHandler.Sounds PERCENT = register("calculator.percent");
    public static final RegistryHandler.Sounds BACKSPACE = register("calculator.backspace");
    public static final RegistryHandler.Sounds PLUS = register("calculator.plus");
    public static final RegistryHandler.Sounds MINUS = register("calculator.minus");
    public static final RegistryHandler.Sounds MULTIPLY = register("calculator.multiply");
    public static final RegistryHandler.Sounds DIVIDE = register("calculator.divide");
    public static final RegistryHandler.Sounds EQUALS = register("calculator.equals");
    public static final RegistryHandler.Sounds CLEAR = register("calculator.clear");

    public static RegistryHandler.Sounds digit(char digit) {
        return DIGITS[digit - '0'];
    }

    public static RegistryHandler.Sounds operator(String operator) {
        return switch (operator) {
            case "+" -> PLUS;
            case "-" -> MINUS;
            case "*" -> MULTIPLY;
            case "/" -> DIVIDE;
            default -> throw new IllegalArgumentException("Unknown calculator operator: " + operator);
        };
    }

    private static RegistryHandler.Sounds register(String name) {
        return Services.REGISTRY.registerSoundEvent(name);
    }

    public static void init() {
        Constants.LOG.info("Registering sounds.");
    }
}
