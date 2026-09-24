// Pulls wiki data straight from the mod so the docs never drift from the game:
// lang, recipes and accessory slots from the mod's resources, plus the calculator GUI and keypad sounds.
import { copyFileSync, existsSync, mkdirSync, readdirSync, readFileSync, writeFileSync } from 'node:fs'
import { basename, dirname, join } from 'node:path'
import { fileURLToPath } from 'node:url'

const docs = join(dirname(fileURLToPath(import.meta.url)), '..')
const root = join(docs, '..')
const resources = join(root, 'common/src/main/resources')
const assets = join(resources, 'assets/craftulator')
const data = join(resources, 'data/craftulator')

if (!existsSync(assets)) {
  console.error(`No mod assets at ${assets}.`)
  process.exit(1)
}

const readJson = (file) => JSON.parse(readFileSync(file, 'utf8'))
const jsonFiles = (dir) => (existsSync(dir) ? readdirSync(dir).filter((f) => f.endsWith('.json')).sort() : [])

const lang = readJson(join(assets, 'lang/en_us.json'))

// Item names for mod items; vanilla names are prettified on the page.
const names = {}
for (const [key, value] of Object.entries(lang)) {
  const match = key.match(/^(item|block)\.craftulator\.([a-z0-9_]+)$/)
  if (match) names[`craftulator:${match[2]}`] = value
}

// The calculator is a 3D model whose texture is a UV atlas, so its icon is a render of the model hosted at
// 1024x1024 alongside the vanilla renders. Upload a new render there when the model changes.
const HOSTED_TEXTURES = 'https://storage.googleapis.com/coolerpromc/textures/craftulator'
const textures = Object.fromEntries(Object.keys(names).map((id) => [id, `${HOSTED_TEXTURES}/${id.split(':')[1]}.png`]))

mkdirSync(join(docs, 'public/gui'), { recursive: true })
copyFileSync(join(assets, 'textures/gui/calculator.png'), join(docs, 'public/gui/calculator.png'))
copyFileSync(join(assets, 'textures/gui/sprites/slot/calculator.png'), join(docs, 'public/gui/slot_calculator.png'))

// Keypad tones for the interactive calculator: sound event -> file under public/sounds/.
const soundEvents = readJson(join(assets, 'sounds.json'))
const sounds = {}
mkdirSync(join(docs, 'public/sounds'), { recursive: true })
for (const [event, definition] of Object.entries(soundEvents)) {
  const entry = definition.sounds[0]
  const name = (typeof entry === 'string' ? entry : entry.name).split(':')[1]
  copyFileSync(join(assets, `sounds/${name}.ogg`), join(docs, `public/sounds/${name}.ogg`))
  sounds[event] = `/sounds/${name}.ogg`
}

const ingredient = (value) => {
  if (typeof value === 'string') return value
  if (Array.isArray(value)) return ingredient(value[0])
  if (value?.item) return value.item
  if (value?.tag) return `#${value.tag}`
  return '?'
}

const recipes = jsonFiles(join(data, 'recipe')).map((file) => {
  const json = readJson(join(data, 'recipe', file))
  const recipe = { id: `craftulator:${basename(file, '.json')}`, type: json.type, result: { id: json.result?.id, count: json.result?.count ?? 1 } }
  if (json.type === 'minecraft:crafting_shaped') {
    recipe.pattern = json.pattern
    recipe.key = Object.fromEntries(Object.entries(json.key).map(([symbol, value]) => [symbol, ingredient(value)]))
  } else if (json.type === 'minecraft:crafting_shapeless') {
    recipe.ingredients = json.ingredients.map(ingredient)
  }
  return recipe
})

mkdirSync(join(docs, '.vitepress/data'), { recursive: true })
writeFileSync(join(docs, '.vitepress/data/data.json'), JSON.stringify({ names, textures, sounds, recipes }, null, 2))
console.log(`Synced ${recipes.length} recipes, ${Object.keys(sounds).length} sounds, ${Object.keys(textures).length} textures.`)
