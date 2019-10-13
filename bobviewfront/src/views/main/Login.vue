<template>
    <v-container grid-list-xs>
        <v-card>
            
            <v-card-title primary-title>
                <div>
                    <h3 class="headline mb-0">Login</h3>
                </div>
            </v-card-title>

            <v-content>
                <v-text-field  label="ID" v-model="userdata.id"></v-text-field>
                <v-text-field
                    label="password"
                    hint="At least 8 characters"
                    counter="8"
                    :type="'password'"
                    v-model="userdata.password"
                ></v-text-field>
            </v-content>
            
            <v-card-actions>
                <v-btn
                    text
                    depressed
                    @click="ApplyLogin"
                >
                    <span>Login</span>
                </v-btn>
                <v-btn
                    text
                    depressed
                    to="/"
                >
                    <span>cancel</span>
                </v-btn>
                <v-spacer/>
                <v-btn
                    text
                    depressed
                    to="/register"
                >
                    <span>register</span>
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-container>
</template>

<script>
import axios from 'axios'
import router from "@/router"
export default {
    name :'login',
    data() {
        return{
            userdata: {
                id:'',
                password:'',
            }
        }
    },
    mounted() { 
        console.log("login mounted") 
        console.log(this.$store.getters.isLogined)
        if(this.$store.getters.isLogined){
            this.$router.push("/")
        }
    },
    methods: {
        ApplyLogin() {
            let currentObj = this
            console.log(currentObj.userdata.id + "  " + currentObj.userdata.password)
            axios
            .post('http://127.0.0.1:8000/api/login/apply/', {
                username: currentObj.userdata.id,
                password: currentObj.userdata.password,
            })
            .then(function(response) {
                console.log(response.data)
                //save userdata
                currentObj.$store.commit('saveUserdata',{
                    username:currentObj.userdata.id,
                    logintoken:response.data['token']
                })
                currentObj.$store.commit('setLogin')
                console.log(currentObj.$store.getters.getUserdata)
                if(response.data['result'] == 'success'){
                    currentObj.$store.dispatch('verifyLogin')
                    router.push("/")
                }
                console.log(response.data['message'])
            })
            .catch(function(error) {
                console.log("senserver error")
                console.log(error)
            });
        },
    },
    computed: {
        getUserdata () {
            return this.$store.getters.getUserdata
        }
    }
}
</script>

<style lang="scss" scoped>

</style>