const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  configureWebpack: {
    plugins: [
      new (require('webpack').DefinePlugin)({
        __VUE_PROD_HYDRATION_MISMATCH_DETAILS__: 'false',
        __VUE_PROD_DEVTOOLS__: 'false'
      })
    ]
  }
})

// module.exports = {
//   devServer: {
//     proxy: {
//       '/api': { // 匹配 /api 开头的请求
//         target: 'http://localhost:8080', // 转发到后端端口
//         changeOrigin: true, // 开启跨域
//         pathRewrite: { '^/api': '' } // 去掉 /api 前缀（如果后端接口没有 /api ）
//       }
//     }
//   }
// };