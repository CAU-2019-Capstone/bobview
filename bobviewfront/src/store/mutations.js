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
    },
    SetOrderInfo(state, payload) {
        state.restaurant_name = payload.restaurant_name
        state.table_number = payload.table_number
        console.log("commit order_info : " + state.restaurant_name + ' ' + state.table_number)
    },
    setRedirectDomain(state, payload) {
        state.redirectDomain = payload.redirectDomain
        console.log("commit redirectDomain : " + state.redirectDomain)
    },
    initBasket(state) {
        state.basketCount = 0
        state.basketMenus = []
    },
    addBasketCount(state) {
        state.basketCount += 1
    },
    subBasketCount(state) {
        if (state.basketCount > 0) {
            state.basketCount -= 1
        }
    },
    addItemCount(state, payload) {
        state.basketMenus[payload.index]['count'] += 1
        console.log("commit add basket count : " + state.basketMenus)
    },
    subItemCount(state, payload) {
        state.basketMenus[payload.index]['count'] -= 1
        if (state.basketMenus[payload.index]['count'] == 0) {
            state.basketMenus.splice(payload.index, 1)
        }
        console.log("commit add basket count : " + state.basketMenus)
    },
    addItem(state, payload) {
        payload.item['count'] = 1
        if (state.basketMenus == []) {
            state.basketMenus = [payload.item]
        } else {
            state.basketMenus.push(payload.item)
        }
        console.log("commit add basket count : " + state.basketMenus)
    },
    setOrderId(state, payload) {
        state.orderId = payload.orderId
        console.log("commit orderId id : " + state.orderId)
    }
}