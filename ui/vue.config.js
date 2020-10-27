module.exports = {
  devServer: {
    proxy: {
      '^/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
    },
    port: 4545
  },
  "transpileDependencies": [
    "vuetify"
  ]
}