<template>
    <v-app>
        <v-container class="blue-grey lighten-2">
            <v-row v-if="getMenuData">
                  <v-col 
                    v-for="menuinfo in menuinfos"
                    :key="menuinfo.menu_name"
                    cols="12" sm="6">
                        <menuTemplate1 v-bind:menuinfo="menuinfo"></menuTemplate1>
                  </v-col>
            </v-row>
            <v-row v-else>
                loading....
            </v-row>
        </v-container>
    </v-app>
</template>

<script>
import MenuTemplate1 from "@/components/MenuTemplate1"
export default {
    components:{
        MenuTemplate1
    },
    mounted() {
        console.log("preview menu mounted")
        this.initMenu()
    },
    data() {
        return {
            getMenuData : false,
            menuinfos: [
                {
                    name :'pasta',
                    price: '6,200',
                    description: 'pasta is very delicious',
                    image : 'https://cdn.vuetifyjs.com/images/cards/docks.jpg',
                },
            ],
            basketMenus: [],
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
            .get('http://127.0.0.1:8000/api/menuinfo/0/?restaurant_name='+restaurant)
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
    },
}
</script>

<style lang="scss" scoped>

</style>