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
                    name="name"
                    label="password"
                    hint="At least 8 characters"
                    counter="8"
                    v-model="userdata.password"
                ></v-text-field>
            </v-content>
            
            <v-card-actions>
                <v-btn
                    text
                    depressed
                    @click="LoginApply"
                >
                    <span>Login</span>
                </v-btn><v-btn
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
        method: {
            ApplyLogin() {

            },
        }
    }
</script>

<style lang="scss" scoped>

</style>