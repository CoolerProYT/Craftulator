import { ref } from 'vue'
// @ts-ignore
import raw from '../data/data.json'

export interface Recipe {
  id: string
  type: string
  result: { id: string; count: number }
  pattern?: string[]
  key?: Record<string, string>
  ingredients?: string[]
}

export const data = raw as unknown as {
  names: Record<string, string>
  textures: Record<string, string>
  /** Sound event (e.g. calculator.digit.1) to its file, relative to the site base. */
  sounds: Record<string, string>
  recipes: Recipe[]
}

/** Mod items use their in-game name; vanilla ids are turned into readable names. */
export function itemName(id: string): string {
  if (data.names[id]) return data.names[id]
  const path = id.replace(/^#/, '').split(':').pop() ?? id
  return path
    .split('_') // @ts-ignore
    .map((word) => (['of', 'the'].includes(word) ? word : word.charAt(0).toUpperCase() + word.slice(1)))
    .join(' ')
}

/** Hosted renders of vanilla items, one PNG per item id. Mojang's textures are not bundled here. */
const VANILLA_ICONS = 'https://storage.googleapis.com/coolerpromc/textures'

/** Where to load an item's icon from: the hosted mod render listed by the sync script, or the hosted vanilla render. */
export function itemIcon(id: string): string | null {
  if (data.textures[id]) return data.textures[id]
  // @ts-ignore
  const [namespace, path] = id.includes(':') ? id.split(':') : ['minecraft', id]
  if (namespace !== 'minecraft') return null
  return `${VANILLA_ICONS}/${namespace}/${path}.png`
}

/** Keypad mute, shared by every calculator on the site like the game's single setting. */
export const muted = ref(false)
