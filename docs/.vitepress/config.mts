import { defineConfig } from 'vitepress'

// GitHub Pages serves a project site from /<repository>/. For a custom domain or a user site, build with DOCS_BASE=/.
const base = process.env.DOCS_BASE ?? '/Craftulator/'
const CALCULATOR_ICON = 'https://storage.googleapis.com/coolerpromc/textures/craftulator/calculator.png'

export default defineConfig({
  title: 'Craftulator',
  description: 'A working calculator item for Minecraft 26.3 whose keypad plays music.',
  base,
  cleanUrls: true,
  srcExclude: ['README.md', 'scripts/**'],
  head: [
    ['link', { rel: 'icon', type: 'image/png', href: CALCULATOR_ICON }],
    // Pixel font for the calculator's keys and display.
    ['link', { rel: 'preconnect', href: 'https://fonts.googleapis.com' }],
    ['link', { rel: 'preconnect', href: 'https://fonts.gstatic.com', crossorigin: '' }],
    ['link', { rel: 'stylesheet', href: 'https://fonts.googleapis.com/css2?family=Pixelify+Sans:wght@400;600&display=swap' }],
  ],
  themeConfig: {
    logo: { src: CALCULATOR_ICON, alt: '' },
    nav: [
      { text: 'Guide', link: '/guide/getting-started' },
      { text: 'Musical keypad', link: '/guide/music' },
      { text: 'FAQ', link: '/faq' },
    ],
    sidebar: [
      {
        text: 'Guide',
        items: [
          { text: 'Getting started', link: '/guide/getting-started' },
          { text: 'Using the calculator', link: '/guide/calculator' },
          { text: 'Musical keypad', link: '/guide/music' },
          { text: 'Keybind', link: '/guide/keybind' },
          { text: 'Curios & Trinkets', link: '/guide/accessories' },
        ],
      },
      { text: 'FAQ', link: '/faq' },
    ],
    search: { provider: 'local' },
    outline: { level: [2, 3] },
    socialLinks: [{ icon: 'github', link: 'https://github.com/CoolerProYT/Craftulator' }],
    footer: { message: 'Released under the MIT License.' },
  },
})
