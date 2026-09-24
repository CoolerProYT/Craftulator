import DefaultTheme from 'vitepress/theme'
import type { Theme } from 'vitepress'
import CalculatorDemo from './components/CalculatorDemo.vue'
import ItemSlot from './components/ItemSlot.vue'
import RecipeCard from './components/RecipeCard.vue'
import './style.css'

export default {
  extends: DefaultTheme,
  enhanceApp({ app }) {
    app.component('CalculatorDemo', CalculatorDemo)
    app.component('ItemSlot', ItemSlot)
    app.component('RecipeCard', RecipeCard)
  },
} satisfies Theme
