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
            v-if="!isLogined"
            @click="saveDomain()"
            to="/login"
        >
            <span class="mr-2">Login</span>
        </v-btn>
        <v-menu
            left
            bottom
            v-if="isLogined"
            >
            <template v-slot:activator="{ on }">
                <v-btn text v-on="on" mr-5>
                    {{$store.state.userdata['username']}}
                    <v-icon>mdi-dots-vertical</v-icon>
                </v-btn>
            </template>
            <v-expand-transition>
            <v-list>
                <v-list-item
                    v-for="userinfo in userinfos"
                    :key="userinfo.title"
                    :to="userinfo.to"
                >
                    <v-list-item-title>
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
        console.log("authlayout mounted") 
        this.$store.dispatch('verifyLogin')
        console.log(this.$store.getters.GetUserdata)
        console.log(this.$store.getters.isLogined)
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