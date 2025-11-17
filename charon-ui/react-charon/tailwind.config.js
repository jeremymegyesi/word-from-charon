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
        'primary-dull': '#724a66ff',
        'primary-accent': '#833aadff',
        'primary-accent-soft': '#f4eafaff',
        'primary-foreground': '#ffffff',
      },
      width: {
        '13': '3.25rem',
      },
      height: {
        '13': '3.25rem',
      },
      animation: {
        'spin-slow': 'spin 1.5s linear infinite',
      },
    },
  },
  plugins: [],
}
