import { createRouter, createWebHistory } from 'vue-router'
import { isNotEmpty } from '@/utils/plugins'
import { getToken, setToken, setUserName } from '@/core/auth' // 验权

const router = createRouter({
    history: createWebHistory(import.meta.env.VITE_BASE_URL),
    routes: [
        {
            path: '/',
            redirect: '/home'
        },
        {
            path: '/login',
            name: 'LoginIndex',
            component: () => import('@/views/login/LoginIndex.vue')
        },
        {
            path: '/home',
            name: 'LayoutIndex',
            redirect: '/home/space',
            component: () => import('@/views/home/HomeIndex.vue'),
            children: [
                {
                    // 前面不能加/
                    path: 'space',
                    name: 'MySpace',
                    component: () => import('@/views/mySpace/MySpaceIndex.vue'),
                    meta: { title: '我的空间' }
                },
                {
                    path: 'recycleBin',
                    name: 'RecycleBin',
                    component: () => import('@/views/recycleBin/RecycleBinIndex.vue'),
                    meta: { title: '回收站' }
                },
                {
                    path: 'account',
                    name: 'Mine',
                    component: () => import('@/views/mine/MineIndex.vue'),
                    meta: { title: '我的账户' }
                }
            ]
        }
    ]
})

// eslint-disable-next-line no-unused-vars
router.beforeEach(async (to, from, next) => {
    // 从localstorage中先获取token，并赋给cookies，如果还存在token，而且还处于正常登录状态就直接将token和userName赋给cookies
    setToken(localStorage.getItem('token'))
    setUserName(localStorage.getItem('userName'))
    if (to.path === '/login') {
        next()
    }
    const token = getToken()
    if (isNotEmpty(token)) {
        next()
    } else {
        next('/login')
    }
})

export default router
