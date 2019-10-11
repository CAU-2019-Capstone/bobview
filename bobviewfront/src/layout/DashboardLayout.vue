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
    components: {
      CoreDrawer: () => import('@/components/dashboard/NavDrawer'),
      CoreFooter: () => import('@/components/dashboard/Footer'),
      CoreAppBar: () => import('@/components/dashboard/AppBar'),
      CoreView: () => import('@/components/dashboard/View')
    }
  }
</script>
