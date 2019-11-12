// https://vuex.vuejs.org/en/getters.html

export default {
    GetUserdata(state) {
        return state.userdata;
    },
    isLogined(state) {
        return state.is_logined;
    },
    isOwner(state) {
        return state.is_owner;
    },
    RestaurantName(state) {
        return state.restaurant_name;
    },
    TableNumber(state) {
        return state.table_number;
    },
    RedirectDomain(state) {
        return state.redirectDomain
    },
    GetBasketMenus(state) {
        return state.basketMenus
    },
    GetBasketCount(state) {
        return state.basketCount
    },
    GetTotalPrice(state) {
        let price = 0
        for (let [index] in state.basketMenus) {
            price = price + state.basketMenus[index]['menu_price'] * state.basketMenus[index]['count']
        }
        return price
    },
    GetOrderId(state) {
        return state.orderId
    },
}