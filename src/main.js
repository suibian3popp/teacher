//从vue框架导入createApp函数功能
import { createApp } from 'vue'
//从当前目录下导入App组件
import App from './App.vue'
import store from './store'
//引入element plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

//导入图标
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

//导入路由
import router from './router/router'

//创建vue应用，挂载到app下
createApp(App).use(store).use(router).use(ElementPlus).mount('#app')
