<template>
    <v-container grid-list-xs>
        <v-card
            v-if="beforesending"
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
                    :append-icon="passwordVisiable ? 'fas fa-eye' : 'fas fa-eye-slash'"
                    :append-icon-cb="onVisiableClicked"
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
        </v-card>

        <v-card
            v-if="!beforesending"
        >
            <p>{{res_message}}</p>
        </v-card>
    </v-container>
    
</template>

<script>
import axios from 'axios'
export default {
    name :'register',
    data() {
        return {
            PersonalData :[
            {
                name:'',
                id:'',
                password:'',
                email:'',
            }],
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
            res_message: ''
        }
    },
    methods: {
        RegisterOnClick () {
            let currentObj = this
            var postData =[{
                is_owner: true,
                username: currentObj.PersonalData.name,
                id: currentObj.PersonalData.id,
                password: currentObj.PersonalData.password,
                email: currentObj.PersonalData.email  
            }]
            console.log(postData)
            axios
            .post('http://127.0.0.1:8000/api/signup/', {
                is_owner: false,
                first_name: currentObj.PersonalData.name,
                username: currentObj.PersonalData.id,
                password: currentObj.PersonalData.password,
                email: currentObj.PersonalData.email 
            })
            .then(function(response) {
                currentObj.res_message = response.data['message']
                console.log(response.data['message'])
                currentObj.beforesending=false
            })
            .catch(function(error) {
                console.log("senserver error")
                console.log(error)
            });
        },
        onVisiableClicked () {
            if(this.passwordVisiable){
                this.passwordVisiable = false
            }
            else{
                this.passwordVisiable = true
            }
        },
    }
}
</script>

<style lang="scss" scoped>

</style>