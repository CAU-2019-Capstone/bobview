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

import VueGeolocation from 'vue-browser-geolocation';
Vue.use(VueGeolocation);

import IMP from 'vue-iamport'

import jQuery from 'jquery'
window.jQuery = jQuery
window.$ = jQuery

//Vue.use(IMP, '가맹점식별코드')
Vue.use(IMP, 'imp33886024') //아임포트 회원가입 후 발급된 가맹점 고유 코드를 사용해주세요. 예시는 KCP공식 아임포트 데모 계정입니다.
Vue.IMP().load()

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