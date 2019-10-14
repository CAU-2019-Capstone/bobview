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
                        <v-toolbar-title>Restaurant Info</v-toolbar-title>
                        <v-divider
                        class="mx-4"
                        inset
                        vertical
                        ></v-divider>
                    <div class="flex-grow-1"></div>
                    </v-toolbar>
                </template>
            </v-data-table>
        </v-container>
    </v-app>
</template>

<script>
export default {
    name :'user_info',
    mounted() {
        
    },
    updated() {
    },
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
            .get('http://127.0.0.1:8000/api/restaurantinfo/'+currentObj.$store.state.userdata['username']+'/')
            .then((result) => {
                console.log(result.data)
                currentObj.userinfoResponse = result.data
                currentObj.userdatas =[
                    {
                        name: 'User ID',
                        data: currentObj.$store.getters.getUserdata['username'],
                    },
                    {
                        name: 'Restaurant name',
                        data: result.data['restaurant_name'],
                    },
                    {
                        name: 'address',
                        data: result.data['restaurant_address'],
                    },
                    {
                        name: 'restaurant_latitude',
                        data: result.data['restaurant_latitude'],
                    },
                    {
                        name: 'restaurant_longitude',
                        data: result.data['restaurant_longitude'],
                    },
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

    }
}
</script>

<style lang="scss" scoped>

</style>