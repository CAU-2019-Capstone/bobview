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

import 'expose-loader?$!expose-loader?jQuery!jquery'

import VueGeolocation from 'vue-browser-geolocation';
Vue.use(VueGeolocation);

import jQuery from 'jquery'
window.jQuery = jQuery
window.$ = jQuery

import * as VueGoogleMaps from "vue2-google-maps";
Vue.use(VueGoogleMaps, {
    load: {
        key: "AIzaSyDwJCSsCMEIPyqxgbe8VposVomREIMYs8M",
        libraries: "places" // necessary for places input
    }
});

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