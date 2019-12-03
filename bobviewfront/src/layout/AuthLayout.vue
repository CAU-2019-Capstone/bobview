<template>
<v-app>
    <v-app-bar app class="blue lighten-2">
        <v-btn
            depressed
            fab
            to="/"
            width="50"
            height="50"
            class="blue lighten-2"
        >
            <v-img
            height="50"
            width="50"
            src="../assets/logo.png"
            >
            </v-img>
        </v-btn>
        <v-spacer></v-spacer>
        
        <v-btn
            depressed
            mall
            v-if="!isLogined"
            @click="saveDomain()"
            to="/login"
            class="blue lighten-4"
        >
            <span class="mr-2">Login</span>
        </v-btn>
        <v-menu
            left
            bottom
            v-if="isLogined"
            class="blue darken-2"
            >
            <template v-slot:activator="{ on }" class="blue darken-2">
                <v-btn text v-on="on" mr-5 class="blue accent-1">
                    {{$store.state.userdata['username']}}
                    <v-icon>mdi-dots-vertical</v-icon>
                </v-btn>
            </template>
            <v-expand-transition>
            <v-list >
                <v-list-item
                    v-for="userinfo in userinfos"
                    :key="userinfo.title"
                    :to="userinfo.to"
                    class="blue darken-2"
                >
                    <v-list-item-title class="blue darken-2">
                        <v-icon>{{ userinfo.icon }}</v-icon>
                        {{ userinfo.title }}
                    </v-list-item-title>
                </v-list-item>
            </v-list>
            </v-expand-transition>
        </v-menu>
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
    mounted() { 
        //console.log("authlayout mounted") 
        this.$store.dispatch('verifyLogin')
        //console.log(this.$store.getters.GetUserdata)
        //console.log(this.$store.getters.isLogined)
        this.isLogined = this.$store.getters.isLogined
    },
    updated() {
        this.isLogined = this.$store.getters.isLogined
    },
    data() {
        return{
            isLogined:{
                type:Boolean,
                default:false,
                description:'is'
            },
            userinfos:[
                {
                    title:'user_info',
                    icon:'mdi-account-circle',
                    to: '/dashboard/user_info'
                },
                {
                    title:'logout',
                    icon:'mdi-logout',
                    to: 'logout'
                }
            ],
            redirect : this.$router.fullPath
        }
    },
    methods: {
        user_info() {
            this.$router.push("/dashboard/user_info")
        },
        logout() {
            this.$store.commit('saveUserdata',{
                username:'',
                logintoken:''
            })
            this.$store.commit('setLogout')
            this.$router.push("/")
        },
        saveDomain() {
            this.$store.commit('setRedirectDomain',{
                redirectDomain: "/"
            })
        }
    }
};
</script>

<style>

</style>