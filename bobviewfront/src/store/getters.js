// https://vuex.vuejs.org/en/getters.html

export default {
    getUserdata(state) {
        return state.userdata;
    },
    isLogined(state) {
        return state.is_logined;
    },
    isOwner(state) {
        return state.is_owner;
    }
}