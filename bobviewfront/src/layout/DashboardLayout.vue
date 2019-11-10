<template>
  <v-app>
    <core-app-bar />

    <core-drawer />

    <core-view />

    <core-footer />
  </v-app>
</template>

<script>
  export default {
    mounted() { 
        let currentObj = this
        console.log("dashboard layout mounted") 
        currentObj.$store.dispatch('verifyLogin')
        console.log(currentObj.$store.getters.GetUserdata)
        console.log(currentObj.$store.getters.isLogined)
        currentObj.isLogined = currentObj.$store.getters.isLogined
        if(!currentObj.isLogined){
          currentObj.$router.push("/")
        }
    },
    updated() { 
        let currentObj = this
        console.log("dashboard layout updated")
        currentObj.isLogined = currentObj.$store.getters.isLogined
        if(!currentObj.isLogined){
          currentObj.$router.push("/")
        }
    },
    components: {
      CoreDrawer: () => import('@/components/dashboard/NavDrawer'),
      CoreFooter: () => import('@/components/dashboard/Footer'),
      CoreAppBar: () => import('@/components/dashboard/AppBar'),
      CoreView: () => import('@/components/dashboard/View')
    },
    data() {
      return {
        lsLogined :{
          type :Boolean,
          default:false,
          description : 'accoutn is logined?'
        }
      }
    }
  }
</script>
