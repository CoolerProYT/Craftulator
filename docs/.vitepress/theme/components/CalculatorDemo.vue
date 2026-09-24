<script setup lang="ts">
import { computed, onBeforeUnmount, ref } from 'vue'
import { withBase } from 'vitepress'
import { data, muted } from '../craftulator'

/**
 * The in-game calculator screen, drawn over the mod's own GUI texture and playing the mod's own keypad sounds.
 * The arithmetic mirrors CalculatorScreen.java so results match the game key for key.
 */
const props = withDefaults(defineProps<{ tunes?: boolean; notes?: boolean }>(), { tunes: false, notes: false })

// GUI layout in texture pixels, from CalculatorScreen.
const GUI_WIDTH = 176
const GUI_HEIGHT = 232
const BUTTON_WIDTH = 31
const BUTTON_HEIGHT = 24
const BUTTON_X = [18, 55, 92, 129]
const BUTTON_Y = [68, 98, 128, 158, 188]
const TOGGLE = { x: 18, y: 214, size: 14 }

type Kind = 'number' | 'operator' | 'clear'
interface Key {
  label: string
  kind: Kind
  sound: string
  action: () => void
  note?: string
}

const NOTES = ['B♭3', 'B♭4', 'C5', 'D5', 'E♭5', 'F5', 'G5', 'A5', 'B♭5', 'C6']

const digit = (d: string): Key => ({ label: d, kind: 'number', sound: `calculator.digit.${d}`, action: () => inputDigit(d), note: NOTES[+d] })

const keys: Key[][] = [
  [
    { label: 'C', kind: 'clear', sound: 'calculator.clear', action: clear },
    { label: '±', kind: 'number', sound: 'calculator.sign', action: toggleSign },
    { label: '%', kind: 'number', sound: 'calculator.percent', action: percent },
    { label: '÷', kind: 'operator', sound: 'calculator.divide', action: () => chooseOperator('/') },
  ],
  [digit('7'), digit('8'), digit('9'), { label: '×', kind: 'operator', sound: 'calculator.multiply', action: () => chooseOperator('*') }],
  [digit('4'), digit('5'), digit('6'), { label: '−', kind: 'operator', sound: 'calculator.minus', action: () => chooseOperator('-') }],
  [digit('1'), digit('2'), digit('3'), { label: '+', kind: 'operator', sound: 'calculator.plus', action: () => chooseOperator('+') }],
  [
    digit('0'),
    { label: '.', kind: 'number', sound: 'calculator.decimal', action: inputDecimal },
    { label: 'DEL', kind: 'number', sound: 'calculator.backspace', action: backspace },
    { label: '=', kind: 'operator', sound: 'calculator.equals', action: equals },
  ],
]

const byLabel = Object.fromEntries(keys.flat().map((key) => [key.label, key]))

// Calculator state, as in CalculatorScreen.
const display = ref('0')
let accumulator = 0
const pendingOperator = ref<string | null>(null)
const hintValue = ref(0)
let lastOperator: string | null = null
let lastOperand = 0
let replaceDisplay = true
let error = false

const GLYPHS: Record<string, string> = { '*': '×', '/': '÷', '-': '−', '+': '+' }
const hint = computed(() => (pendingOperator.value === null ? 'READY' : `${formatValue(hintValue.value)} ${GLYPHS[pendingOperator.value]}`))

function setAccumulator(value: number) {
  accumulator = value
  hintValue.value = value
}

function inputDigit(d: string) {
  recoverFromError()
  if (replaceDisplay) {
    if (pendingOperator.value === null) lastOperator = null
    display.value = d
    replaceDisplay = false
    return
  }
  if (display.value.replace('-', '').replace('.', '').length >= 15) return
  display.value = display.value === '0' ? d : display.value + d
}

function inputDecimal() {
  recoverFromError()
  if (replaceDisplay) {
    if (pendingOperator.value === null) lastOperator = null
    display.value = '0.'
    replaceDisplay = false
  } else if (!display.value.includes('.')) {
    display.value += '.'
  }
}

function toggleSign() {
  if (error || display.value === '0') return
  display.value = display.value.startsWith('-') ? display.value.slice(1) : `-${display.value}`
}

function percent() {
  if (error) return
  display.value = formatValue(currentValue() / 100)
  replaceDisplay = true
}

function backspace() {
  if (error) return clear()
  if (replaceDisplay) return
  const value = display.value
  display.value = value.length <= 1 || (value.length === 2 && value.startsWith('-')) ? '0' : value.slice(0, -1)
}

function chooseOperator(operator: string) {
  if (error) return
  if (pendingOperator.value !== null && !replaceDisplay) {
    if (!apply(pendingOperator.value, accumulator, currentValue())) return
  } else if (!replaceDisplay || pendingOperator.value === null) {
    setAccumulator(currentValue())
  }
  pendingOperator.value = operator
  lastOperator = null
  replaceDisplay = true
}

function equals() {
  if (error) return
  if (pendingOperator.value !== null) {
    const operand = replaceDisplay ? accumulator : currentValue()
    const operation = pendingOperator.value
    if (apply(operation, accumulator, operand)) {
      lastOperator = operation
      lastOperand = operand
      pendingOperator.value = null
      replaceDisplay = true
    }
  } else if (lastOperator !== null) {
    apply(lastOperator, currentValue(), lastOperand)
    replaceDisplay = true
  }
}

function apply(operator: string, left: number, right: number): boolean {
  if (operator === '/' && right === 0) {
    showError()
    return false
  }
  const result = operator === '+' ? left + right : operator === '-' ? left - right : operator === '*' ? left * right : left / right
  if (!Number.isFinite(result)) {
    showError()
    return false
  }
  setAccumulator(result)
  display.value = formatValue(result)
  return true
}

const currentValue = () => parseFloat(display.value)

function recoverFromError() {
  if (error) clear()
}

function showError() {
  display.value = 'Error'
  setAccumulator(0)
  pendingOperator.value = null
  lastOperator = null
  replaceDisplay = true
  error = true
}

function clear() {
  display.value = '0'
  setAccumulator(0)
  pendingOperator.value = null
  lastOperator = null
  lastOperand = 0
  replaceDisplay = true
  error = false
}

/** Java's BigDecimal.valueOf(value).stripTrailingZeros().toPlainString(). */
function toPlain(value: number): string {
  const text = String(value)
  if (!text.includes('e')) return text
  const [mantissa, exponent] = text.split('e')
  const negative = mantissa.startsWith('-')
  const unsigned = mantissa.replace('-', '')
  const digits = unsigned.replace('.', '')
  const point = unsigned.split('.')[0].length + Number(exponent)
  const plain =
    point <= 0 ? `0.${'0'.repeat(-point)}${digits}`
    : point >= digits.length ? digits + '0'.repeat(point - digits.length)
    : `${digits.slice(0, point)}.${digits.slice(point)}`
  return (negative ? '-' : '') + plain
}

function formatValue(value: number): string {
  if (Math.abs(value) < 1e-12) return '0'
  const plain = toPlain(value)
  if (plain.length <= 16) return plain
  return value.toExponential(8).replace('e+', 'e').replace(/e(-?)0+/, 'e$1')
}

// Sounds: each press cuts off the previous tone, like the game.
let activeTone: HTMLAudioElement | null = null

function playTone(event: string) {
  if (muted.value || typeof Audio === 'undefined') return
  const file = data.sounds[event]
  if (!file) return
  activeTone?.pause()
  activeTone = new Audio(withBase(file))
  activeTone.volume = 0.8
  activeTone.play().catch(() => {})
}

function toggleSound() {
  muted.value = !muted.value
  if (!muted.value) playTone('calculator.equals')
}

const pressed = ref<string | null>(null)
let releaseTimer: ReturnType<typeof setTimeout> | undefined

function press(key: Key) {
  playTone(key.sound)
  key.action()
  pressed.value = key.label
  clearTimeout(releaseTimer)
  releaseTimer = setTimeout(() => (pressed.value = null), 120)
}

function onKeydown(event: KeyboardEvent) {
  if (event.ctrlKey || event.metaKey || event.altKey) return
  const label =
    event.key === 'Enter' || event.key === '=' ? '='
    : event.key === 'Backspace' ? 'DEL'
    : event.key === 'Delete' || event.key === 'c' || event.key === 'C' ? 'C'
    : event.key === ',' ? '.'
    : event.key === '*' || event.key === 'x' || event.key === 'X' ? '×'
    : event.key === '/' ? '÷'
    : event.key === '-' ? '−'
    : event.key
  const key = byLabel[label]
  if (!key) return
  // Keeps keys like "/" from also opening the site search.
  event.preventDefault()
  event.stopPropagation()
  press(key)
}

// Tunes written in numbered notation. "-" holds the previous note for a beat.
const TUNES = [
  { name: 'Twinkle, Twinkle, Little Star', notes: '1 1 5 5 6 6 5 - 4 4 3 3 2 2 1 -' },
  { name: 'Ode to Joy', notes: '3 3 4 5 5 4 3 2 1 1 2 3 3 - 2 2 -' },
  { name: 'Frère Jacques', notes: '1 2 3 1 1 2 3 1 3 4 5 - 3 4 5 -' },
]
const playing = ref<string | null>(null)
let tuneTimer: ReturnType<typeof setTimeout> | undefined

function stopTune() {
  clearTimeout(tuneTimer)
  playing.value = null
}

function playTune(tune: { name: string; notes: string }) {
  stopTune()
  clear()
  playing.value = tune.name
  const steps = tune.notes.split(' ')
  let i = 0
  const next = () => {
    if (i >= steps.length) return stopTune()
    const step = steps[i++]
    if (step !== '-') press(byLabel[step])
    tuneTimer = setTimeout(next, 380)
  }
  next()
}

onBeforeUnmount(() => {
  stopTune()
  clearTimeout(releaseTimer)
  activeTone?.pause()
})

const pct = (value: number, total: number) => `${(value / total) * 100}%`
const keyStyle = (column: number, row: number) => ({
  left: pct(BUTTON_X[column], GUI_WIDTH),
  top: pct(BUTTON_Y[row], GUI_HEIGHT),
  width: pct(BUTTON_WIDTH, GUI_WIDTH),
  height: pct(BUTTON_HEIGHT, GUI_HEIGHT),
})
const toggleStyle = {
  left: pct(TOGGLE.x, GUI_WIDTH),
  top: pct(TOGGLE.y, GUI_HEIGHT),
  width: pct(TOGGLE.size, GUI_WIDTH),
  height: pct(TOGGLE.size, GUI_HEIGHT),
}
</script>

<template>
  <div class="cc-demo">
    <div
      class="cc-calculator"
      tabindex="0"
      role="group"
      aria-label="Calculator. Click it, then type to use the keyboard."
      @keydown="onKeydown"
    >
      <img class="pixelated background" :src="withBase('/gui/calculator.png')" alt="" draggable="false" />
      <div class="screen" aria-live="polite">
        <span class="hint">{{ hint }}</span>
        <span class="value">{{ display }}</span>
      </div>
      <template v-for="(row, r) in keys" :key="r">
        <button
          v-for="(key, c) in row"
          :key="key.label"
          type="button"
          class="key"
          :class="[key.kind, { pressed: pressed === key.label }]"
          :style="keyStyle(c, r)"
          :aria-label="key.label === 'DEL' ? 'Delete' : key.label"
          @mousedown.prevent
          @click="press(key)"
        >
          {{ key.label }}
          <span v-if="props.notes && key.note" class="note">{{ key.note }}</span>
        </button>
      </template>
      <button
        type="button"
        class="toggle"
        :class="{ muted }"
        :style="toggleStyle"
        :title="muted ? 'Sound: Off' : 'Sound: On'"
        :aria-label="muted ? 'Sound: Off' : 'Sound: On'"
        @mousedown.prevent
        @click="toggleSound"
      >
        ♪
      </button>
    </div>

    <div v-if="props.tunes" class="tunes">
      <span class="cc-muted">Play a tune:</span>
      <button
        v-for="tune in TUNES"
        :key="tune.name"
        type="button"
        class="tune"
        :class="{ active: playing === tune.name }"
        @click="playing === tune.name ? stopTune() : playTune(tune)"
      >
        {{ playing === tune.name ? '■︎ ' : '▶︎ ' }}{{ tune.name }}
      </button>
    </div>
  </div>
</template>

<style scoped>
.cc-demo {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 14px;
  margin: 20px 0;
}

.cc-calculator {
  position: relative;
  width: min(352px, 100%);
  aspect-ratio: 176 / 232;
  container-type: inline-size;
  user-select: none;
  outline: none;
  border-radius: 4px;
}

.cc-calculator:focus-visible {
  box-shadow: 0 0 0 3px var(--vp-c-brand-1);
}

.background {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
}

/* Font sizes are in container units so the whole screen scales with the image. 1 texture pixel = 100cqw / 176. */
.screen {
  position: absolute;
  left: calc(100cqw * 22 / 176);
  right: calc(100cqw * 20 / 176);
  top: calc(100cqw * 18 / 176);
  display: flex;
  flex-direction: column;
  font-family: 'Pixelify Sans', var(--vp-font-family-mono);
  line-height: 1;
}

.hint {
  font-size: calc(100cqw * 7 / 176);
  color: #42574b;
}

.value {
  margin-top: calc(100cqw * 9 / 176);
  font-size: calc(100cqw * 11 / 176);
  font-weight: 600;
  color: #17241d;
  text-align: right;
  white-space: nowrap;
  overflow: hidden;
}

.key,
.toggle {
  position: absolute;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0;
  font-family: 'Pixelify Sans', var(--vp-font-family-mono);
  font-size: calc(100cqw * 9 / 176);
  font-weight: 600;
  line-height: 1;
  cursor: pointer;
}

.key {
  background: transparent;
  border: none;
}

.key:hover,
.toggle:hover {
  background-color: rgba(255, 255, 255, 0.27);
}

.key.pressed {
  background-color: rgba(0, 0, 0, 0.2);
}

.key.number {
  color: #25292d;
}

.key.operator {
  color: #ffffff;
}

.key.clear {
  color: #8c2020;
}

.note {
  position: absolute;
  right: 6%;
  bottom: 8%;
  font-size: calc(100cqw * 5 / 176);
  font-weight: 500;
  opacity: 0.75;
}

.toggle {
  font-size: calc(100cqw * 8 / 176);
  color: #25292d;
  background-color: #8b8b8b;
  border: none;
  box-shadow:
    inset 1px 1px 0 #ffffff,
    inset -1px -1px 0 #373737;
}

.toggle.muted {
  color: #5a5a5a;
  text-decoration: line-through;
}

.tunes {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  align-items: center;
  gap: 8px;
}

.tune {
  padding: 4px 12px;
  border: 1px solid var(--vp-c-divider);
  border-radius: 999px;
  font-size: 13px;
  background: var(--vp-c-bg-soft);
  transition: border-color 0.2s;
}

.tune:hover,
.tune.active {
  border-color: var(--vp-c-brand-1);
  color: var(--vp-c-brand-1);
}
</style>
