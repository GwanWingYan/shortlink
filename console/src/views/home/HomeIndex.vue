<template>
    <div class="common-layout">
        <el-container>
            <el-header height="54px" style="padding: 0">
                <div class="header">
                    <div @click="toMySpace" class="logo">Shortlink短链接</div>
                    <div style="display: flex; align-items: center">
                        <a class="link-span" style="text-decoration: none" target="_blank"
                            href="https://github.com/GwanWingYan/shortlink">⭐Github仓库</a>
                        <el-dropdown>
                            <div class="block">
                                <span class="name-span" style="text-decoration: none">{{ userName }}</span>
                            </div>
                            <template #dropdown>
                                <el-dropdown-menu>
                                    <el-dropdown-item @click="toMine">个人信息</el-dropdown-item>
                                    <el-dropdown-item divided @click="logout">退出</el-dropdown-item>
                                </el-dropdown-menu>
                            </template>
                        </el-dropdown>
                    </div>
                </div>
            </el-header>
            <el-main style="padding: 0">
                <div class="content-box">
                    <RouterView class="content-space" />
                </div>
            </el-main>
        </el-container>
    </div>
</template>

<script setup>
import { ref, getCurrentInstance, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { removeKey, removeUserName, getToken, getUserName } from '@/core/auth.js'
import { ElMessage } from 'element-plus'
const { proxy } = getCurrentInstance()
const API = proxy.$API
// 当当前路径和菜单不匹配时，菜单不会被选中
const router = useRouter()
const toMine = () => {
    router.push('/home' + '/account')
}
// 登出
const logout = async () => {
    const token = getToken()
    const userName = getUserName()
    // 请求登出的接口
    await API.user.logout({ token, userName })
    // 删除cookies中的token和userName
    removeUserName()
    removeKey()
    localStorage.removeItem('token')
    localStorage.removeItem('userName')
    router.push('/login')
    ElMessage.success('成功退出！')
}
// 点击左上方的图片跳转到我的空间
const toMySpace = () => {
    router.push('/home')
}
const userName = ref('')
onMounted(async () => {
    userName.value = truncateText(getUserName(), 8)
})


// 辅助函数，用于截断文本
const truncateText = (text, maxLength) => {
    return text.length > maxLength ? text.slice(0, maxLength) + '...' : text
}
</script>

<style lang="scss" scoped>
.el-container {
    height: 100vh;

    .el-aside {
        border: 0;
        background-color: #0e5782;

        ul {
            border: 0px;
        }
    }

    .el-main {
        background-color: #e8e8e8;
    }
}

.header {
    background-color: #333333;
    padding: 0 0 0 20px;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;

    .block {
        cursor: pointer;
        display: flex;
        align-items: center;
        border: 0px;
    }
}

.content-box {
    height: calc(100vh - 50px);
    background-color: white;
}

:deep(.el-tooltip__trigger:focus-visible) {
    outline: unset;
}

.logo {
    font-size: 15px;
    font-weight: 600;
    color: #e8e8e8;
    font-family: Helvetica, Tahoma, Arial, 'PingFang SC', 'Hiragino Sans GB', 'Heiti SC',
        'Microsoft YaHei', 'WenQuanYi Micro Hei';
    // font-family: 'Helvetica Neue', Helvetica, STHeiTi, Arial, sans-serif;
    cursor: pointer;
}

.logo:hover {
    color: #fff;
}

.link-span {
    color: #fff;
    opacity: .6;
    margin-right: 30px;
    font-size: 16px;
    font-family: 'Helvetica Neue', Helvetica, STHeiTi, Arial, sans-serif;
    cursor: pointer;
    text-decoration: none;
}

.link-span:hover {
    text-decoration: underline !important;
    opacity: 1;
    color: #fff;
}

.name-span {
    color: #fff;
    opacity: .6;
    margin-right: 30px;
    font-size: 16px;
    font-family: 'Helvetica Neue', Helvetica, STHeiTi, Arial, sans-serif;
    cursor: pointer;
    text-decoration: none;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
}

.avatar {
    transform: translateY(-2px);
}
</style>
