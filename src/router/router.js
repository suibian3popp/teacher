//创建路由对象 并配置 把路由对象导出
import {createRouter,createWebHistory} from "vue-router";

const router = createRouter({
    //路由历史
    history: createWebHistory(),
    //配置路由（可以是多个，所以是数组）
    routes: [
        {
            path: '/',
            component: () => import('@/views/UploadResource.vue')
        },
        {
            //动态路由,id是变量
            path: '/resourcesList',
            component: () => import('@/views/ResourcesList.vue')
        }
    ]
})
//导出路由对象
export default router;
