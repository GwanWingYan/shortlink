import Cookies from 'js-cookie'

const TokenKey = 'token'

export function getToken() {
    return Cookies.get(TokenKey)
}
export function getUserName() {
    return Cookies.get('userName')
}

export function setToken(token) {
    return Cookies.set(TokenKey, token)
}

export function setUserName(userName) {
    return Cookies.set('userName', userName)
}

export function removeKey() {
    return Cookies.remove(TokenKey)
}

export function removeUserName() {
    return Cookies.remove('userName')
}


