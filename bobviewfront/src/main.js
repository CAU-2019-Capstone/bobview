import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify'
import 'roboto-fontface/css/roboto/roboto-fontface.css'
import '@mdi/font/css/materialdesignicons.css'
import router from "./router"
import store from "./store"
import { sync } from 'vuex-router-sync'

//add axios
import axios from "axios"
import VueAxios from "vue-axios"

Vue.prototype.$http = axios

sync(store, router)

Vue.use(VueAxios, axios);

Vue.config.productionTip = false

new Vue({
    vuetify,
    router,
    store,
    axios,
    render: h => h(App)
}).$mount('#app')