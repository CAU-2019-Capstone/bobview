<template>
    <v-container grid-list-xs>

        <v-content
            v-if="selectingOwner"
        >
            <v-card
            class="d-flex justify-space-around mb-5 ma-5"
            >
                <v-btn
                    text
                    class="mb-5 ma-5"
                    @click="setOwner(true)"
                >
                    <span>Owner</span>
                </v-btn>
                <v-btn
                    text
                    class="mb-5 ma-5"
                    @click="setOwner(false)"
                >
                    <span>Customer</span>
                </v-btn>
            </v-card>
        </v-content>
        <v-content
            v-if="!selectingOwner"
        >
            <v-card
                v-if="beforesending"
                class = "px-5 py-2"
            >
                <v-card-title primary-title>
                    Register
                </v-card-title>
                <v-content>
                    <v-text-field  label="Name" v-model="PersonalData.name"></v-text-field>
                    <v-text-field  label="ID" v-model="PersonalData.id"></v-text-field>
                    <v-text-field
                        name="name"
                        label="password"
                        hint="At least 8 characters"
                        counter="8"
                        v-model="PersonalData.password"
                        :type="passwordVisiable ? 'password' : 'text'"
                    ></v-text-field>
                    <v-text-field
                        label="E-mail"
                        v-model="PersonalData.email"
                    ></v-text-field>
                </v-content>
                <v-card-actions>
                    <v-btn
                        text
                        @click="RegisterOnClick"
                    >
                        <span>Register</span>
                    </v-btn>
                    <v-btn
                        text
                        to="/"
                    >
                        <span>cancel</span>
                    </v-btn>
                </v-card-actions>
                <v-content v-if="mailSending">
                    <v-progress-linear  :indeterminate="true"></v-progress-linear>
                    <p>메일이 보내졌습니다. 확인해주세요</p>
                </v-content>
                
            </v-card>

            <v-card
                v-if="!beforesending"
            >
                <p>{{res_message}}</p>
            </v-card>
        </v-content>
    </v-container>

</template>

<script>
import axios from 'axios'
export default {
    name :'register',
    mounted() { 
        console.log(this.name+" mounted") 
        console.log(this.$store.getters.isLogined)
        if(this.$store.getters.isLogined){
            this.$router.push("/")
        }
    },
    data() {
        return {
            mailSending:false,
            PersonalData :[
            {
                name:'',
                id:'',
                password:'',
                email:'',
            }],
            is_owner: {
                type: Boolean,
                default: false,
                description: 'person is owner?'
            },
            passwordVisiable: {
                type: Boolean,
                default : true,
                description : 'password visibility'
            },
            beforesending: {
                type: Boolean,
                default: true,
                desciprtion : 'before sending request'
            },
            selectingOwner: {
                type: Boolean,
                default: false,
                desciprtion : 'before selecting owner'
            },
            res_message: ''
        }
    },
    methods: {
        RegisterOnClick () {
            this.mailSending = true
            let currentObj = this
            var postData =[{
                is_owner: currentObj.is_owner,
                username: currentObj.PersonalData.name,
                id: currentObj.PersonalData.id,
                password: currentObj.PersonalData.password,
                email: currentObj.PersonalData.email  
            }]
            console.log(postData)

            //axios transmission
            axios
            .post('http://127.0.0.1:8000/api/signup/add/', {
                is_owner: currentObj.is_owner,
                first_name: currentObj.PersonalData.name,
                username: currentObj.PersonalData.id,
                password: currentObj.PersonalData.password,
                email: currentObj.PersonalData.email 
            })
            .then(function(response) {
                currentObj.res_message = response.data['message']
                console.log(response.data['message'])
                currentObj.beforesending=false
                this.mailSending = true
                this.$router.push("/")
            })
            .catch(function(error) {
                console.log("senserver error")
                console.log(error)
            });
            
        },
        //visibility of password
        onVisiableClicked () {
            if(this.passwordVisiable){
                this.passwordVisiable = false
            }
            else{
                this.passwordVisiable = true
            }
        },

        setOwner(value) {
            this.is_owner = value
            this.selectingOwner = !this.selectingOwner
        },
    }
}
</script>

<style lang="scss" scoped>

</style>