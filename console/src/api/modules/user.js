import http from '../axios'
export default {
    // 注册
    addUser(data) {
        return http({
            url: '/user',
            method: 'post',
            data
        })
    },
    // 编辑信息
    editUser(data) {
        return http({
            url: '/user',
            method: 'put',
            data
        })
    },
    // 登录
    login(data) {
        return http({
            url: '/user/login',
            method: 'post',
            data
        })
    },
    // 退出登录
    logout(data) {
        return http({
            url: '/user/logout?token=' + data.token + '&userName=' + data.userName,
            method: 'delete'
        })
    },
    // 检查用户名是否可用
    hasUserName(data) {
        return http({
            url: '/user/hasUserName',
            method: 'get',
            params: data
        })
    },
    // 根据用户名查找用户信息
    queryUserInfo(data) {
        return http({
            url: '/user/actual/' + data,
            method: 'get'
        })
    }
}
