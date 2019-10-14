// https://vuex.vuejs.org/en/mutations.html

export default {
    saveUserdata(state, payload) {
        console.log("commit set userdata : " + payload.username + payload.logintoken)
        state.userdata['username'] = payload.username
        state.userdata['logintoken'] = payload.logintoken
    },
    setLogin(state) {
        state.is_logined = true
        console.log("commit set login : " + state.is_logined)
    },
    setLogout(state) {
        state.is_logined = false
        console.log("commit set login : " + state.is_logined)
    },
    logout(state) {
        state.is_logined = false
        state.userdata['username'] = ''
        state.userdata['logintoken'] = ''
        console.log("commit logout : " + state.is_logined)
    },
    setIsowner(state, payload) {
        state.is_owner = payload.is_owner
        console.log("commit is_owner : " + state.is_owner)
    }
}