const { defineConfig } = require("@vue/cli-service");

module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 3000,
    proxy: {
      "/api": {
        target: "http://localhost",
        changeOrigin: true,
        pathRewrite: {
          "^/api": "",
        },
      },
    },
  },
  publicPath: "/mydream/",
  outputDir: "../mydream/src/main/resources/static", // 빌드된 파일의 출력 디렉토리
});
