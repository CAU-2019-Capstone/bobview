<template>
    <v-app>
        <v-container>
            <v-data-table
                :headers="headers"
                :items="userdatas"
                class="elevation-1"
                hide-default-footer
                v-if="getAPI"
            >
                <template v-slot:top>
                    <v-toolbar flat color="white">
                        <v-toolbar-title>User Info</v-toolbar-title>
                        <v-divider
                        class="mx-4"
                        inset
                        vertical
                        ></v-divider>
                    <div class="flex-grow-1"></div>
                    <v-dialog v-model="dialog" max-width="500px">
                    <template v-slot:activator="{ on }">
                        <v-btn color="blue-grey lighten-1" dark class="mb-2" v-on="on" @click="LoadData">Modify</v-btn>
                    </template>
                    <v-card>
                        <v-card-title>
                        <span class="headline">Edit Info</span>
                        </v-card-title>

                        <v-card-text>
                        <v-container>
                            <v-row>
                                <p>ID : {{userdatas[0]['data']}}</p>
                            </v-row>
                            <v-row>
                                <p>Email : {{userdatas[2]['data']}}</p>
                            </v-row>
                            <v-row>
                                <v-text-field v-model="editedItem.name" label="name"></v-text-field>
                            </v-row>
                            <v-row>
                                <v-text-field v-model="editedItem.password" label="Password" :type="'password'" counter="8"></v-text-field>
                            </v-row>
                            <v-row v-if="passwordErr">
                                <p>password Error</p>
                            </v-row>
                        </v-container>
                        </v-card-text>

                        <v-card-actions>
                        <div class="flex-grow-1"></div>
                        <v-btn color="blue darken-1" text @click="close">Cancel</v-btn>
                        <v-btn color="blue darken-1" text @click="save">Save</v-btn>
                        </v-card-actions>
                    </v-card>
                    </v-dialog>
                    </v-toolbar>
                </template>
            </v-data-table>
        </v-container>
    </v-app>
</template>

<script>
export default {
    name :'user_info',
    watch: {
      dialog (val) {
        val || this.close()
      },
    },
    created () {
        this.initialize()
    },
    data() {
        return {
            dialog: false,
            passwordErr: false,
            getAPI: false,
            headers: [
                {
                text: 'Category',
                align: 'left',
                sortable: false,
                value: 'name',
                },
                { text: 'value', value: 'data', sortable: false},
            ],
            editedItem: {
                name: '',
                password:'',
            },
            userdatas : [],
            is_owner: false,
            userinfoResponse : [],
        }
    },
    methods: {
        initialize () {
            let currentObj = this
            console.log(currentObj.$store.state.userdata['username'])
            this.axios
            .get('localhost:8000/api/userinfo/0/?username='+currentObj.$store.state.userdata['username'])
            .then((result) => {
                console.log(result.data)
                currentObj.userinfoResponse = result.data
                currentObj.userdatas =[
                    {
                        name: 'ID',
                        data: result.data['username'],
                    },
                    {
                        name: 'name',
                        data: result.data['first_name'],
                    },
                    {
                        name: 'email',
                        data: result.data['email'],
                    },
                    {
                        name: 'last_login',
                        data: result.data['last_login'],
                    },
                    {
                        name: 'date_joined',
                        data: result.data['date_joined'],
                    },
                    {
                        name: 'is_owner',
                        data: String(result.data['is_owner']),
                    }
                ]
                currentObj.getAPI = true
                
            })
            .catch(function(error) {
                console.log("senserver error")
                console.log(error)
            });
        },
        LoadData() {
            this.editedItem = {
                name: this.userdatas[1]['data'],
                password:'',
            }
        },
        close () {
            this.dialog = false
            this.passwordErr = false
        },
        save () {
            let currentObj = this
            currentObj.axios
            .post('localhost:8000/api/signup/modify/', {
                username: currentObj.$store.state.userdata['username'],
                token : currentObj.$store.state.userdata['logintoken'],
                password: currentObj.editedItem.password,
                first_name: currentObj.editedItem.name
            })
            .then(function(response) {
                console.log(response.data)
                if(response.data['result']=='password error'){
                    currentObj.passwordErr = true
                } else {
                    currentObj.initialize()
                    currentObj.passwordErr = false
                }
            })
            .catch(function(error) {
                console.log("senserver error")
                console.log(error)
            });
        },
    }
}
</script>

<style lang="scss" scoped>

</style>