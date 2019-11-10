<template>
    <v-app>
        <v-container class="blue-grey lighten-2">
            <v-row v-if="getMenuData">
                  <v-col 
                    v-for="menuinfo in menuinfos"
                    :key="menuinfo.menu_name"
                    cols="12" sm="6">
                        <v-card
                            class="mx-auto"
                            max-width="400"
                        >
                            <v-img
                            class="align-end"
                            height="200px"
                            :src="menuinfo.menu_image"
                            >
                            <v-card-title justify-center>{{menuinfo.menu_name}}</v-card-title>
                            </v-img>

                            <v-card-text class="text--primary">
                                <div>Price : {{menuinfo.menu_price}}</div>
                                <div>Description : {{menuinfo.menu_desc}}</div>
                            </v-card-text>

                            <v-card-actions>
                            <v-btn
                                color="orange"
                                text
                                @click="addBasket(menuinfo)"
                            >
                                Order
                            </v-btn>
                            </v-card-actions>
                        </v-card>
                  </v-col>
            </v-row>
            <v-row v-else>
                loading....
            </v-row>
        </v-container>
    </v-app>
</template>

<script>
export default {
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