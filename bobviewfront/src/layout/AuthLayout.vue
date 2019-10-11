<template>
<v-app>
    <v-app-bar app>
        <v-btn
            text
            to="/"
        >
        <v-toolbar-title class="headline text-uppercase">
            <span>Bobview</span>
        </v-toolbar-title>
        </v-btn>
        <v-spacer></v-spacer>
        <v-btn
            depressed
            mall
            to="/login"
        >
            <span class="mr-2">Login</span>
        </v-btn>
    </v-app-bar>
    <v-content>
        <router-view></router-view>
    </v-content>

    
    <v-footer>
        <div class="flex-grow-1">
            <v-btn 
            text
            depressed 
            small
            href="https://github.com/CAU-2019-Capstone/BobViewFront"
            >
                <v-icon>fab fa-github</v-icon>
                <span class="ml-2">github</span>
            </v-btn>
            
        </div>
        <v-spacer></v-spacer>
        <div>&copy; {{ new Date().getFullYear() }} BobView Project</div>
    </v-footer>
</v-app>


</template>

<script>
export default {
  name: 'authlayout',
  components: {
  },
  data: () => ({
  }),
};
</script>

<style>

</style>