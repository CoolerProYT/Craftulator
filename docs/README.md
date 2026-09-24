# Craftulator wiki

VitePress site for the mod. Item names, the recipe, the calculator GUI and the keypad sounds are read from the mod's resources, so the wiki follows the mod when they change.

```bash
cd docs
npm install
npm run dev                   # syncs data, then serves http://localhost:5173
npm run build                 # syncs data, then builds to .vitepress/dist
```

`npm run sync` (run automatically by `dev` and `build`) writes `.vitepress/data/data.json`, copies the calculator GUI and slot icon to `public/gui/`, and copies the keypad sounds to `public/sounds/`. All of them are git-ignored.

Item icons are hosted at `https://storage.googleapis.com/coolerpromc/textures/`: vanilla items under `minecraft/`, the calculator at `craftulator/calculator.png`. The calculator is a 3D model whose texture is a UV atlas, so its icon is a 1024x1024 render of the model in its inventory pose. Upload a new render there when the model changes.

The interactive calculator (`.vitepress/theme/components/CalculatorDemo.vue`) copies the arithmetic from `CalculatorScreen.java`. Change both together.

The `Docs` workflow (`.github/workflows/docs.yml`) builds the site and publishes it to GitHub Pages on every push to the default branch. It is served at https://craftulator.coolerpromc.com/.
