<template>
    <v-app>
        <v-container class="blue-grey lighten-2">
            <v-row v-if="getMenuData">
                  <v-col 
                    v-for="menuinfo in menuinfos"
                    :key="menuinfo.menu_name"
                    cols="12" sm="6">
                        <menuTemplate1 v-if="template_id==1" v-bind:menuinfo="menuinfo" @addBasket="addBasket"></menuTemplate1>
                        <menuTemplate2 v-if="template_id==2" v-bind:menuinfo="menuinfo" @addBasket="addBasket"></menuTemplate2>
                        <menuTemplate3 v-if="template_id==3" v-bind:menuinfo="menuinfo" @addBasket="addBasket"></menuTemplate3>
                  </v-col>
            </v-row>
            <v-row v-else>
                loading....
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
        MenuTemplate3
    },
    mounted() {
        console.log("order menu mounted")
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
            template_id:0
        }
    },
    methods: {
        initMenu(){
            let restaurant = this.$store.getters.RestaurantName
            let table = this.$store.getters.TableNumber
            if(restaurant == '' || table == undefined){
                this.$router.push("/")
            }
            console.log(restaurant, table)
            this.axios
            .get('localhost:8000/api/menutemplate/0/?restaurant_name='+restaurant)
            .then((result) => {
                console.log(result.data)
                this.template_id = result.data['menu_type']
            })
            .catch(function(error) {
                console.log("senserver error")
                console.log(error)
            });
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
        addBasket(item) {
            this.$store.commit('addBasketCount')
            let basketMenus = this.$store.getters.GetBasketMenus
            for(let [index] in basketMenus){
                if(basketMenus[index]['menu_name'] == item['menu_name']){
                    this.$store.commit('addItemCount', {
                        index:index
                    })
                    console.log(this.$store.getters.GetBasketMenus)
                    return
                }
            }
            this.$store.commit('addItem', {
                item:item
            })
            console.log(this.$store.getters.GetBasketMenus)
        }
    },
}
</script>

<style lang="scss" scoped>

</style>