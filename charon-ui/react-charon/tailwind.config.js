/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './index.html',
    './src/**/*.{js,ts,jsx,tsx}'
  ],
  theme: {
    extend: {
      colors: {
        primary: '#790f54',
        'primary-foreground': '#ffffff',
      },
      width: {
        '13': '3.25rem',
      },
      height: {
        '13': '3.25rem',
      },
    },
  },
  plugins: [],
}
