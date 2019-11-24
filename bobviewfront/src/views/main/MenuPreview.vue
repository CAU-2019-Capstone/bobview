<template>
    <v-app>
        <v-container >
            <v-row  class="justify-between">
                <v-col cols="10">
                    <v-tabs
                    v-model="tab">
                    <v-tabs-slider></v-tabs-slider>
                        <v-tab 
                        v-for="template in templateMenus"
                        :key="template.id"
                        >{{template.desc}}</v-tab>
                    </v-tabs>
                </v-col>
                <v-col>
                    <v-btn text depressed @click="saveTemplate">
                        APPLY
                    </v-btn>
                </v-col>
            </v-row>
            <v-row v-if="selected" class="justify-center my-3">
                APLLIED!
            </v-row>
            <v-row>
                <v-tabs-items v-model="tab">
                    <v-tab-item
                        v-for="template in templateMenus"
                        :key="template.id"
                    >
                        <v-container class="blue-grey lighten-2">
                            <v-row v-if="getMenuData">
                                <v-col 
                                    v-for="menuinfo in menuinfos"
                                    :key="menuinfo.menu_name"
                                    cols="12" sm="6">
                                        <menuTemplate1 v-if="template.id==1" v-bind:menuinfo="menuinfo"></menuTemplate1>
                                        <menuTemplate2 v-if="template.id==2" v-bind:menuinfo="menuinfo"></menuTemplate2>
                                        <menuTemplate3 v-if="template.id==3" v-bind:menuinfo="menuinfo"></menuTemplate3>
                                </v-col>
                            </v-row>
                            <v-row v-else>
                                loading....
                            </v-row>
                        </v-container>
                    </v-tab-item>
                </v-tabs-items>
            </v-row>
        </v-container>
    </v-app>
</template>

<script>
import MenuTemplate1 from "@/components/MenuTemplate/MenuTemplate1"
import MenuTemplate2 from "@/components/MenuTemplate/MenuTemplate2"
import MenuTemplate3 from "@/components/MenuTemplate/MenuTemplate3"
export default {
    components:{
        MenuTemplate1,
        MenuTemplate2,
        MenuTemplate3,
    },
    mounted() {
        console.log("preview menu mounted")
        this.initMenu()
    },
    data() {
        return {
            tab:'',
            getMenuData : false,
            selected:false,
            menuinfos: [
                {
                    name :'pasta',
                    price: '6,200',
                    description: 'pasta is very delicious',
                    image : 'https://cdn.vuetifyjs.com/images/cards/docks.jpg',
                },
            ],
            basketMenus: [],
            templateMenus:[
                {
                    id:1,
                    desc:'Template 1'
                },
                {
                    id:2,
                    desc:'Template 2'
                },
                {
                    id:3,
                    desc:'Template 3'
                }
            ]
        }
    },
    methods: {
        initMenu(){
            let restaurant = this.$store.getters.RestaurantName
            let table = this.$store.getters.TableNumber
            if(restaurant == ''){
                this.$router.push("/")
            }
            console.log(restaurant, table)
            this.axios
            .get('localhost:8000/api/menuinfo/0/?restaurant_name='+restaurant)
            .then((result) => {
                console.log(result.data)
                this.menuinfos = result.data
                this.getMenuData = true
            })
            .catch(function(error) {
                console.log("senserver error")
                console.log(error)
            });
        },
        saveTemplate(){
            let menu_type = this.tab + 1
            this.axios
                .post('localhost:8000/api/menutemplate/',{
                    restaurant: this.$store.getters.RestaurantName,
                    menu_type: menu_type,
                })
                .then((result) => {
                    console.log(result.data)
                    this.getMenuData = true
                    this.selected = true
                })
                .catch(function(error) {
                    console.log("senserver error")
                    console.log(error)
                });
        }
    },
}
</script>

<style lang="scss" scoped>

</style>