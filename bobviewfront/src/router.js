import Vue from 'vue';
import VueRouter from 'vue-router';

import AuthLayout from "@/layout/AuthLayout"
import DashboardLayout from "@/layout/DashboardLayout"
import OrderLayout from "@/layout/OrderLayout"
import tested from "@/views/Testing"
Vue.use(VueRouter);

export default new VueRouter({
    mode: "history",
    linkExactActiveClass: "active",
    routes: [{
            path: "/",
            redirect: "default",
            component: AuthLayout,
            children: [{
                    path: "/",
                    name: "main",
                    component: () =>
                        import ( /* webpackChunkName: "demo" */ "./views/main/Main.vue")
                },
                {
                    path: "/login",
                    name: "login",
                    component: () =>
                        import ( /* webpackChunkName: "demo" */ "./views/main/Login.vue")
                },
                {
                    path: "/register",
                    name: "register",
                    component: () =>
                        import ( /* webpackChunkName: "demo" */ "./views/main/Register.vue")
                },
                {
                    path: "/logout",
                    name: "logout",
                    component: () =>
                        import ( /* webpackChunkName: "demo" */ "./views/main/Logout.vue")
                },
                {
                    path: "/preview",
                    name: "preview",
                    component: () =>
                        import ( /* webpackChunkName: "demo" */ "./views/main/MenuPreview.vue")
                },
            ]
        },
        {
            path: "/order",
            redirect: "order/main",
            component: OrderLayout,
            children: [{
                    path: "/order/menu",
                    name: "ordermenu",
                    component: () =>
                        import ( /* webpackChunkName: "demo" */ "./views/order/OrderMenu.vue")
                },
                {
                    path: "/order/main",
                    name: "ordermain",
                    component: () =>
                        import ( /* webpackChunkName: "demo" */ "./views/order/OrderMain.vue")
                },
                {
                    path: "/order/basket",
                    name: "basket",
                    component: () =>
                        import ( /* webpackChunkName: "demo" */ "./views/order/Basket.vue")
                },
                {
                    path: "/order/payment",
                    name: "payment",
                    component: () =>
                        import ( /* webpackChunkName: "demo" */ "./views/order/Payment.vue")
                },
                {
                    path: "/order/call",
                    name: "call",
                    component: () =>
                        import ( /* webpackChunkName: "demo" */ "./views/order/Call.vue")
                },
                {
                    path: "/order/login",
                    name: "orderlogin",
                    component: () =>
                        import ( /* webpackChunkName: "demo" */ "./views/order/Login.vue")
                },
                {
                    path: "/order/complete",
                    name: "ordercomplete",
                    component: () =>
                        import ( /* webpackChunkName: "demo" */ "./views/order/Complete.vue")
                },
            ]
        },
        {
            path: "/",
            component: DashboardLayout,
            children: [{
                    path: "/dashboard",
                    name: "dashboard",
                    // route level code-splitting
                    // this generates a separate chunk (about.[hash].js) for this route
                    // which is lazy-loaded when the route is visited.
                    component: () =>
                        import ( /* webpackChunkName: "demo" */ "./views/dashboard/Dashboard.vue")
                },
                {
                    path: "/user_info",
                    name: "user_info",
                    // route level code-splitting
                    // this generates a separate chunk (about.[hash].js) for this route
                    // which is lazy-loaded when the route is visited.
                    component: () =>
                        import ( /* webpackChunkName: "demo" */ "./views/dashboard/UserInfo.vue")
                },
                {
                    path: "/restaurant_info",
                    name: "restaurant_info",
                    // route level code-splitting
                    // this generates a separate chunk (about.[hash].js) for this route
                    // which is lazy-loaded when the route is visited.
                    component: () =>
                        import ( /* webpackChunkName: "demo" */ "./views/dashboard/RestaurantInfo.vue")
                },
                {
                    path: "/menu_info",
                    name: "menu_info",
                    // route level code-splitting
                    // this generates a separate chunk (about.[hash].js) for this route
                    // which is lazy-loaded when the route is visited.
                    component: () =>
                        import ( /* webpackChunkName: "demo" */ "./views/dashboard/MenuInfo.vue")
                },
            ]
        },
        {
            path: "/testing",
            component: tested,
        },
    ]
})