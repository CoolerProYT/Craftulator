package com.coolerpromc.craftulator.screen;

import com.coolerpromc.craftulator.Constants;
import com.coolerpromc.craftulator.platform.util.RegistryHandler;
import com.coolerpromc.craftulator.sound.ModSounds;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;

import java.math.BigDecimal;
import java.util.Locale;

public class CalculatorScreen extends Screen {
    private static final Identifier TEXTURE = Constants.id("textures/gui/calculator.png");
    private static final int GUI_WIDTH = 176;
    private static final int GUI_HEIGHT = 232;
    private static final int BUTTON_WIDTH = 31;
    private static final int BUTTON_HEIGHT = 24;
    private static final int[] BUTTON_X = {18, 55, 92, 129};
    private static final int[] BUTTON_Y = {68, 98, 128, 158, 188};

    private static final int TOGGLE_X = 18;
    private static final int TOGGLE_Y = 214;
    private static final int TOGGLE_SIZE = 14;

    private static final int NUMBER_TEXT = 0xFF25292D;
    private static final int OPERATOR_TEXT = 0xFFFFFFFF;
    private static final int CLEAR_TEXT = 0xFF8C2020;
    private static final int DISPLAY_TEXT = 0xFF17241D;
    private static final int DISPLAY_HINT_TEXT = 0xFF42574B;
    private static final int MUTED_TEXT = 0xFF5A5A5A;

    private static final int KEY_FACE = 0xFF8B8B8B;
    private static final int KEY_HIGHLIGHT = 0xFFFFFFFF;
    private static final int KEY_SHADOW = 0xFF373737;

    private static final String NOTE_GLYPH = "♪";
    private static final float TONE_VOLUME = 0.8F;

    private static boolean soundEnabled = true;

    private SoundInstance activeTone;
    private String display = "0";
    private double accumulator;
    private String pendingOperator;
    private String lastOperator;
    private double lastOperand;
    private boolean replaceDisplay = true;
    private boolean error;

    public CalculatorScreen() {
        super(Component.translatable("gui.craftulator.calculator"));
    }

    @Override
    protected void init() {
        int left = (this.width - GUI_WIDTH) / 2;
        int top = (this.height - GUI_HEIGHT) / 2;

        addButton(left, top, 0, 0, "C", CLEAR_TEXT, ModSounds.CLEAR, this::clear);
        addButton(left, top, 1, 0, "±", NUMBER_TEXT, ModSounds.SIGN, this::toggleSign);
        addButton(left, top, 2, 0, "%", NUMBER_TEXT, ModSounds.PERCENT, this::percent);
        addButton(left, top, 3, 0, "÷", OPERATOR_TEXT, ModSounds.DIVIDE, () -> chooseOperator("/"));

        addDigitButton(left, top, 0, 1, "7");
        addDigitButton(left, top, 1, 1, "8");
        addDigitButton(left, top, 2, 1, "9");
        addButton(left, top, 3, 1, "×", OPERATOR_TEXT, ModSounds.MULTIPLY, () -> chooseOperator("*"));

        addDigitButton(left, top, 0, 2, "4");
        addDigitButton(left, top, 1, 2, "5");
        addDigitButton(left, top, 2, 2, "6");
        addButton(left, top, 3, 2, "−", OPERATOR_TEXT, ModSounds.MINUS, () -> chooseOperator("-"));

        addDigitButton(left, top, 0, 3, "1");
        addDigitButton(left, top, 1, 3, "2");
        addDigitButton(left, top, 2, 3, "3");
        addButton(left, top, 3, 3, "+", OPERATOR_TEXT, ModSounds.PLUS, () -> chooseOperator("+"));

        addDigitButton(left, top, 0, 4, "0");
        addButton(left, top, 1, 4, ".", NUMBER_TEXT, ModSounds.DECIMAL, this::inputDecimal);
        addButton(left, top, 2, 4, "DEL", NUMBER_TEXT, ModSounds.BACKSPACE, this::backspace);
        addButton(left, top, 3, 4, "=", OPERATOR_TEXT, ModSounds.EQUALS, this::equals);

        this.addRenderableWidget(new SoundToggleButton(left + TOGGLE_X, top + TOGGLE_Y, TOGGLE_SIZE));
    }

    private void addDigitButton(int left, int top, int column, int row, String digit) {
        addButton(left, top, column, row, digit, NUMBER_TEXT, ModSounds.digit(digit.charAt(0)), () -> inputDigit(digit));
    }

    private void addButton(int left, int top, int column, int row, String label, int color, RegistryHandler.Sounds tone, Runnable action) {
        this.addRenderableWidget(new CalculatorButton(left + BUTTON_X[column], top + BUTTON_Y[row], BUTTON_WIDTH, BUTTON_HEIGHT, Component.literal(label), color, tone, action));
    }

    private void playTone(RegistryHandler.Sounds tone) {
        if (!soundEnabled) {
            return;
        }

        SoundManager soundManager = Minecraft.getInstance().getSoundManager();
        if (this.activeTone != null) {
            soundManager.stop(this.activeTone);
        }

        SoundInstance instance = SimpleSoundInstance.forUI(tone.get(), 1.0F, TONE_VOLUME);
        this.activeTone = instance;
        soundManager.play(instance);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        int left = (this.width - GUI_WIDTH) / 2;
        int top = (this.height - GUI_HEIGHT) / 2;

        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, left, top, 0, 0, GUI_WIDTH, GUI_HEIGHT, GUI_WIDTH, GUI_HEIGHT);

        String operationHint = pendingOperator == null ? Component.translatable("gui.craftulator.ready").getString() : formatValue(accumulator) + " " + operatorGlyph(pendingOperator);
        graphics.text(this.font, operationHint, left + 22, top + 20, DISPLAY_HINT_TEXT, false);

        String visibleDisplay = fitDisplay(display, 137);
        int displayX = left + 156 - this.font.width(visibleDisplay);
        graphics.text(this.font, visibleDisplay, displayX, top + 39, DISPLAY_TEXT, false);

        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
    }

    private String fitDisplay(String value, int availableWidth) {
        if (this.font.width(value) <= availableWidth) {
            return value;
        }

        String clipped = value;
        while (clipped.length() > 1 && this.font.width("…" + clipped) > availableWidth) {
            clipped = clipped.substring(1);
        }
        return "…" + clipped;
    }

    @Override
    public Component getNarrationMessage() {
        return Component.translatable("gui.craftulator.calculator.narration", display);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (this.minecraft.options.keyInventory.matches(event)) {
            this.onClose();
            return true;
        }

        return switch (event.key()) {
            case InputConstants.KEY_RETURN, InputConstants.KEY_NUMPADENTER -> {
                playTone(ModSounds.EQUALS);
                equals();
                yield true;
            }
            case InputConstants.KEY_BACKSPACE -> {
                playTone(ModSounds.BACKSPACE);
                backspace();
                yield true;
            }
            case InputConstants.KEY_DELETE -> {
                playTone(ModSounds.CLEAR);
                clear();
                yield true;
            }
            default -> super.keyPressed(event);
        };
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        String character = event.codepointAsString();
        if (character.length() != 1) {
            return super.charTyped(event);
        }

        char typed = character.charAt(0);
        if (typed >= '0' && typed <= '9') {
            playTone(ModSounds.digit(typed));
            inputDigit(character);
            return true;
        }

        return switch (typed) {
            case '.', ',' -> {
                playTone(ModSounds.DECIMAL);
                inputDecimal();
                yield true;
            }
            case '+' -> {
                playTone(ModSounds.PLUS);
                chooseOperator("+");
                yield true;
            }
            case '-' -> {
                playTone(ModSounds.MINUS);
                chooseOperator("-");
                yield true;
            }
            case '*', 'x', 'X' -> {
                playTone(ModSounds.MULTIPLY);
                chooseOperator("*");
                yield true;
            }
            case '/' -> {
                playTone(ModSounds.DIVIDE);
                chooseOperator("/");
                yield true;
            }
            case '%' -> {
                playTone(ModSounds.PERCENT);
                percent();
                yield true;
            }
            case '=', '\r', '\n' -> {
                playTone(ModSounds.EQUALS);
                equals();
                yield true;
            }
            case 'c', 'C' -> {
                playTone(ModSounds.CLEAR);
                clear();
                yield true;
            }
            default -> super.charTyped(event);
        };
    }

    private void inputDigit(String digit) {
        recoverFromError();
        if (replaceDisplay) {
            if (pendingOperator == null) {
                lastOperator = null;
            }
            display = digit;
            replaceDisplay = false;
            return;
        }

        int digitCount = display.replace("-", "").replace(".", "").length();
        if (digitCount >= 15) {
            return;
        }

        display = display.equals("0") ? digit : display + digit;
    }

    private void inputDecimal() {
        recoverFromError();
        if (replaceDisplay) {
            if (pendingOperator == null) {
                lastOperator = null;
            }
            display = "0.";
            replaceDisplay = false;
        } else if (!display.contains(".")) {
            display += ".";
        }
    }

    private void toggleSign() {
        if (error || display.equals("0")) {
            return;
        }
        display = display.startsWith("-") ? display.substring(1) : "-" + display;
    }

    private void percent() {
        if (error) {
            return;
        }
        display = formatValue(currentValue() / 100.0D);
        replaceDisplay = true;
    }

    private void backspace() {
        if (error) {
            clear();
            return;
        }
        if (replaceDisplay) {
            return;
        }

        display = display.length() <= 1 || (display.length() == 2 && display.startsWith("-")) ? "0" : display.substring(0, display.length() - 1);
    }

    private void chooseOperator(String operator) {
        if (error) {
            return;
        }

        if (pendingOperator != null && !replaceDisplay) {
            if (!apply(pendingOperator, accumulator, currentValue())) {
                return;
            }
        } else if (!replaceDisplay || pendingOperator == null) {
            accumulator = currentValue();
        }

        pendingOperator = operator;
        lastOperator = null;
        replaceDisplay = true;
    }

    private void equals() {
        if (error) {
            return;
        }

        if (pendingOperator != null) {
            double operand = replaceDisplay ? accumulator : currentValue();
            String operation = pendingOperator;
            if (apply(operation, accumulator, operand)) {
                lastOperator = operation;
                lastOperand = operand;
                pendingOperator = null;
                replaceDisplay = true;
            }
        } else if (lastOperator != null) {
            apply(lastOperator, currentValue(), lastOperand);
            replaceDisplay = true;
        }
    }

    private boolean apply(String operator, double left, double right) {
        if (operator.equals("/") && right == 0.0D) {
            showError();
            return false;
        }

        double result = switch (operator) {
            case "+" -> left + right;
            case "-" -> left - right;
            case "*" -> left * right;
            case "/" -> left / right;
            default -> throw new IllegalArgumentException("Unknown calculator operator: " + operator);
        };

        if (!Double.isFinite(result)) {
            showError();
            return false;
        }

        accumulator = result;
        display = formatValue(result);
        return true;
    }

    private double currentValue() {
        return Double.parseDouble(display);
    }

    private void recoverFromError() {
        if (error) {
            clear();
        }
    }

    private void showError() {
        display = Component.translatable("gui.craftulator.error").getString();
        accumulator = 0.0D;
        pendingOperator = null;
        lastOperator = null;
        replaceDisplay = true;
        error = true;
    }

    private void clear() {
        display = "0";
        accumulator = 0.0D;
        pendingOperator = null;
        lastOperator = null;
        lastOperand = 0.0D;
        replaceDisplay = true;
        error = false;
    }

    private static String operatorGlyph(String operator) {
        return switch (operator) {
            case "*" -> "×";
            case "/" -> "÷";
            case "-" -> "−";
            default -> operator;
        };
    }

    private static String formatValue(double value) {
        if (Math.abs(value) < 1.0E-12D) {
            return "0";
        }

        String plain = BigDecimal.valueOf(value).stripTrailingZeros().toPlainString();
        if (plain.length() <= 16) {
            return plain;
        }

        String scientific = String.format(Locale.ROOT, "%.8e", value);
        return scientific.replace("e+", "e").replaceAll("e(-?)0+", "e$1");
    }

    private final class CalculatorButton extends AbstractButton {
        private final int textColor;
        private final RegistryHandler.Sounds tone;
        private final Runnable action;

        private CalculatorButton(int x, int y, int width, int height, Component message, int textColor, RegistryHandler.Sounds tone, Runnable action) {
            super(x, y, width, height, message);
            this.textColor = textColor;
            this.tone = tone;
            this.action = action;
        }

        @Override
        public void onPress(InputWithModifiers input) {
            action.run();
        }

        @Override
        public void playDownSound(SoundManager soundManager) {
            CalculatorScreen.this.playTone(this.tone);
        }

        @Override
        protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
            if (this.isHovered()) {
                graphics.fill(this.getX(), this.getY(), this.getRight(), this.getBottom(), 0x45FFFFFF);
            }

            this.centeredText(graphics, CalculatorScreen.this.font, this.getMessage(), this.getX() + this.getWidth() / 2, this.getY() + (this.getHeight() - CalculatorScreen.this.font.lineHeight) / 2, this.textColor, false);
        }

        public void centeredText(GuiGraphicsExtractor graphics, Font font, Component text, int x, int y, int color, boolean dropShadow) {
            FormattedCharSequence toRender = text.getVisualOrderText();
            graphics.text(font, toRender, x - font.width(toRender) / 2 + 1, y + 1, color, dropShadow);
        }

        @Override
        public void updateWidgetNarration(NarrationElementOutput output) {
            this.defaultButtonNarrationText(output);
        }
    }

    /** Mutes and unmutes the keypad tones, drawn as a small extra key below the keypad. */
    private final class SoundToggleButton extends AbstractButton {
        private SoundToggleButton(int x, int y, int size) {
            super(x, y, size, size, Component.empty());
            refreshLabel();
        }

        @Override
        public void onPress(InputWithModifiers input) {
            soundEnabled = !soundEnabled;
            refreshLabel();

            if (soundEnabled) {
                CalculatorScreen.this.playTone(ModSounds.EQUALS);
            }
        }

        private void refreshLabel() {
            Component label = Component.translatable(soundEnabled ? "gui.craftulator.sound.on" : "gui.craftulator.sound.off");
            this.setMessage(label);
            this.setTooltip(Tooltip.create(label));
        }

        @Override
        public void playDownSound(SoundManager soundManager) {
            // The toggle plays its own confirmation in onPress instead.
        }

        @Override
        protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
            int x = this.getX();
            int y = this.getY();
            int right = this.getRight();
            int bottom = this.getBottom();

            graphics.fill(x, y, right, bottom, KEY_FACE);
            graphics.fill(x, y, right, y + 1, KEY_HIGHLIGHT);
            graphics.fill(x, y, x + 1, bottom, KEY_HIGHLIGHT);
            graphics.fill(x, bottom - 1, right, bottom, KEY_SHADOW);
            graphics.fill(right - 1, y, right, bottom, KEY_SHADOW);

            if (this.isHovered()) {
                graphics.fill(x + 1, y + 1, right - 1, bottom - 1, 0x45FFFFFF);
            }

            Font font = CalculatorScreen.this.font;
            int color = soundEnabled ? NUMBER_TEXT : MUTED_TEXT;
            graphics.text(font, NOTE_GLYPH, x + (this.getWidth() - font.width(NOTE_GLYPH)) / 2 + 1, y + (this.getHeight() - font.lineHeight) / 2 + 1, color, false);

            if (!soundEnabled) {
                int middle = y + this.getHeight() / 2;
                graphics.fill(x + 3, middle, right - 3, middle + 1, MUTED_TEXT);
            }
        }

        @Override
        public void updateWidgetNarration(NarrationElementOutput output) {
            this.defaultButtonNarrationText(output);
        }
    }
}
