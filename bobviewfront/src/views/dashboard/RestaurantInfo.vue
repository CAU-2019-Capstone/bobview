<template>
    <v-app>
        <v-container
            v-if ="getSuccess"
        >
            <v-card
                 class="justify-center mx-2 mx-2"
                 v-for="(userdata,i) in userdatas"
                 :key="i"
            >
                <v-data-table
                    :headers="headers"
                    :items="userdata"
                    class="elevation-1 my-2"
                    hide-default-footer
                >
                    <template v-slot:top>
                        <v-toolbar flat color="white">
                            <v-toolbar-title>{{userdata[1]['data']}}</v-toolbar-title>
                            <v-divider
                            class="mx-4"
                            inset
                            vertical
                            ></v-divider>
                        <div class="flex-grow-1"></div>
                        </v-toolbar>
                    </template>
                </v-data-table>
            </v-card>
            <v-card
                class="d-flex elevation-1 justify-space-around mx-2 my-8">
                <v-dialog v-model="dialog" max-width="500px">
                    <template v-slot:activator="{ on }">
                        <v-btn
                            text
                            class="mb-5 ma-5"
                            v-on="on"
                        >
                            <span>Register Restaurant</span>
                        </v-btn>
                    </template>
                    <v-card>
                        <v-card-title>
                        <span class="headline">Restaurant Information</span>
                        </v-card-title>

                        <v-card-text>
                        <v-container>
                            <v-row>
                                <v-text-field v-model="editedItem.restaurant_name" label="Restaurant Name"></v-text-field>
                            </v-row>
                            <v-row>
                                <v-text-field v-model="editedItem.restaurant_address" label="Restaurant Address"></v-text-field>
                            </v-row>
                            <v-row>
                                <v-text-field v-model="editedItem.restaurant_latitude" label="latitude"></v-text-field>
                            </v-row>
                            <v-row>
                                <v-text-field v-model="editedItem.restaurant_longitude" label="longitude"></v-text-field>
                            </v-row>
                            <v-row>
                                <v-file-input :rules="rules" 
                                label="Restaurant Image" 
                                accept="image/png, image/jpeg, image/bmp"
                                prepend-icon="mdi-camera"
                                v-model ="editedItem.restaurant_image"
                                dense></v-file-input>
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
            </v-card>
        </v-container>
    </v-app>
</template>

<script>
export default {
    name :'restaurant_info',
    watch: {
      dialog (val) {
        val || this.close()
      },
    },
    created () {
        console.log("before created")
        console.log(this.userdatas)
        this.initialize()
    },
    data() {
        return {
            dialog: false,
            getSuccess : false,
            headers: [
                {
                text: 'Category',
                align: 'left',
                sortable: false,
                value: 'name',
                },
                { text: 'data', value: 'data', sortable: false},
            ],
            editedItem: {
                restaurant_name: '',
                restaurant_address:'',
                restaurant_latitude:'',
                restaurant_longitude:'',
                restaurant_image:null
            },
            defaultItem : {
                restaurant_name: '',
                restaurant_address:'',
                restaurant_latitude:'',
                restaurant_longitude:'',
                restaurant_image:null
            },
            userdatas : {},
            resultList: [],
        }
    },
    methods: {
        initialize () {
            this.axios
            .get('http://127.0.0.1:8000/api/restaurantinfo/'+this.$store.state.userdata['username']+'/')
            .then((result) => {
                console.log(result.data)
                console.log("results")
                this.resultList = result.data
                for(let [index] in this.resultList){
                    this.userdatas[index] =[
                        {
                            name: 'User ID',
                            data: this.$store.state.userdata['username'],
                        },
                        {
                            name: 'Restaurant name',
                            data: this.resultList[index]['restaurant_name'],
                        },
                        {
                            name: 'address',
                            data: this.resultList[index]['restaurant_address'],
                        },
                        {
                            name: 'restaurant_latitude',
                            data: this.resultList[index]['restaurant_latitude'],
                        },
                        {
                            name: 'restaurant_longitude',
                            data: this.resultList[index]['restaurant_longitude'],
                        },
                    ]
                }
                console.log(this.userdatas)
                console.log("userdatas")
                this.getSuccess = true
            })
            .catch(function(error) {
                console.log("senserver error")
                console.log(error)
            });
        },
        close () {
            this.dialog = false
            setTimeout(() => {
                this.editedItem = Object.assign({}, this.defaultItem)
            }, 300)
        },
        save () {
            this.getSuccess = false
            this.dialog = false
            let currentObj = this
            console.log(this.editedItem)
            this.axios
                .post('http://localhost:8000/api/myrestaurant/p', {
                    username: currentObj.$store.state.userdata['username'],
                    restaurant_name: currentObj.editedItem.restaurant_name,
                    restaurant_address: currentObj.editedItem.restaurant_address,
                    restaurant_latitude: currentObj.editedItem.restaurant_latitude,
                    restaurant_longitude: currentObj.editedItem.restaurant_longitude,
                    restaurant_image: currentObj.editedItem.restaurant_image
                })
                .then(function(response) {
                    console.log(response.data)
                    
                })
                .catch(function(error) {
                    console.log("senserver error")
                    console.log(error)
                });
            setTimeout(function(){
                currentObj.initialize()
            }, 1000);
        }
    },
    destroyed() {
        clearInterval();
    }
}
</script>

<style lang="scss" scoped>

</style>