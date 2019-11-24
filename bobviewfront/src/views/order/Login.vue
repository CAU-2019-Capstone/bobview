<template>
    <v-container grid-list-xs>
        <v-card>
            
            <v-card-title primary-title>
                <div>
                    <h3 class="headline mb-0">Login</h3>
                </div>
            </v-card-title>

            <v-content class="px-5 py-2">
                <v-text-field  label="ID" v-model="userdata.id"></v-text-field>
                <v-text-field
                    label="password"
                    hint="At least 8 characters"
                    counter="8"
                    :type="'password'"
                    v-model="userdata.password"
                ></v-text-field>
                <p v-if="loginerror">{{message}}</p>
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
export default {
    name :'login',
    data() {
        return{
            userdata: {
                id:'',
                password:'',
            },
            message:'',
            loginerror : false
        }
    },
    mounted() { 
        console.log("login mounted")
        console.log(this.$store.getters.isLogined)
        if(this.$store.getters.isLogined){
            this.$router.push("/order/main/?r="+this.$store.getters.RestaurantName+"&t="+this.$store.getters.TableNumber+"/")
        }
    },
    methods: {
        ApplyLogin() {
            let currentObj = this
            console.log(currentObj.userdata.id + "  " + currentObj.userdata.password)
            axios
            .post('localhost:8000/api/login/apply/', {
                username: currentObj.userdata.id,
                password: currentObj.userdata.password,
            })
            .then(function(response) {
                console.log(response.data)
                //save userdata
                if(response.data['result'] == 'success'){
                    currentObj.$store.commit('saveUserdata',{
                        username:currentObj.userdata.id,
                        logintoken:response.data['token']
                    })
                    currentObj.$store.commit('setLogin')
                    currentObj.$store.commit('setIsowner', {
                        is_owner:response.data['is_owner']
                    })
                    currentObj.$store.dispatch('verifyLogin')
                    console.log(currentObj.$store.getters.GetUserdata)
                    console.log(currentObj.$store.getters.RedirectDomain)
                    currentObj.$router.push(currentObj.$store.getters.RedirectDomain);
                } else {
                    currentObj.loginerror=true
                    currentObj.message=response.data['message']
                }
                console.log(response.data['message'])
            })
            .catch(function(error) {
                console.log("senserver error")
                console.log(error)
                currentObj.loginerror=true
                currentObj.message='server error'
            });
        },
    },
    computed: {
        GetUserdata () {
            return this.$store.getters.GetUserdata
        }
    }
}
</script>

<style lang="scss" scoped>

</style>