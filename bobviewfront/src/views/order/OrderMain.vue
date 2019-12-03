<template>

    <v-container grid-list-xs>
        <v-card v-if="loading">
            <v-contnet v-if="farFromRes" class="my-10">
                <v-row class="justify-center">
                   <h2>Now We Collect Your Location... Please click YES to your gps Permission</h2>
                </v-row>
            </v-contnet>
        </v-card>
        <v-card v-if="!loading">
            <v-contnet v-if="farFromRes" class="my-10">
                <v-row class="justify-center">
                   <h2>your location is far from restaurant, please connect correctly</h2>
                </v-row>
            </v-contnet>
            <v-content v-if="tableIsActive && !farFromRes" class="my-10">
                <v-row class="justify-center">
                    Table's Current Order!
                </v-row>
                <v-row class="justify-center">
                    Order Number : {{this.order_id}}
                </v-row>
                <v-row class="justify-center">
                    <v-btn text depressed @click="toRating()">
                        <span>Review Your Current Order</span>
                    </v-btn>
                </v-row>
            </v-content>
            <v-content class="px-5 py-2">
                <v-row class="justify-center">
                    <v-img
                        :src="restaurant_img_src"
                        max-height="400"
                        max-width="700"
                    ></v-img>
                </v-row>
                <v-row  class="justify-center py-5"> 
                    <v-btn
                        text
                        depressed
                        to="/order/menu"
                        props:
                        v-if="!tableIsActive"
                        :disabled="farFromRes"
                    >
                        <span>Order</span>
                    </v-btn>
                    <v-btn
                        text
                        depressed
                        to="/order/menu"
                        props:
                        v-if="tableIsActive"
                        :disabled="farFromRes"
                    >
                        <span>Order More</span>
                    </v-btn>
                </v-row>
                <v-row  class="justify-center">
                    <v-btn
                        text
                        depressed
                        to="/order/call"
                        :disabled="farFromRes"
                    >
                        <span>Request Something</span>
                    </v-btn>
                </v-row>
            </v-content>
        </v-card>

    </v-container>
    
</template>
<script>
export default {
    name :'ordermain',
    data() {
        return{
            loading : false,
            restaurant : this.$route.query.r,
            table : this.$route.query.t,
            gettingLocation : false,
            location : false,
            errorStr : false,
            tableIsActive :false,
            farFromRes: true,
            activeOrders : [],
            order_id: 0,
            restaurant_img_src:'@/assets/logo.png',
            restaurantInfo : {}
        }
    },
    created() {
        //do we support geolocation
        if(!("geolocation" in navigator)) {
            this.errorStr = 'Geolocation is not available.';
            console.log(this.errorStr)
            return;
        }
        console.log("success")
    },

    mounted() { 
        console.log("order main mounted") 
        console.log(this.$store.getters.isLogined)
        // get position
        navigator.geolocation.getCurrentPosition(pos => {
            this.gettingLocation = true;
            this.location = pos;
        }, err => {
            this.gettingLocation = false;
            this.errorStr = err.message;
        })
        let currentObj = this
        setTimeout(function(){
            if(currentObj.$route.query.r == undefined){
                currentObj.$router.push("/")
            }
            currentObj.$store.commit('SetOrderInfo',{
                restaurant_name : currentObj.$route.query.r,
                table_number : currentObj.$route.query.t,
            })
            console.log(currentObj.$store.getters.RestaurantName)
            console.log(currentObj.$store.getters.TableNumber)

            currentObj.getOrderMenus()
        }, 1000)

    },
    computed: {
        distance : function() {
            console.log(this.location.coords.latitude)
            console.log(this.location.coords.longitude)
            console.log(this.restaurantInfo.restaurant_latitude)
            console.log(this.restaurantInfo.restaurant_longitude)
            let X = this.location.coords.latitude - this.restaurantInfo.restaurant_latitude
            let Y = this.location.coords.longitude - this.restaurantInfo.restaurant_longitude
            
            let Xm = X * 6400 * 2 * Math.PI / 360 * 1000
            let Ym = Y * Math.cos(this.location.coords.latitude) * 6400 * 2 * Math.PI / 360 * 1000
            
            let distances = Math.sqrt(Xm * Xm + Ym * Ym)
            console.log(distances)
            if(distances < 100){
                this.farFromRes = false
            }
            return distances
        },
    },
    methods: {
        getOrderMenus() {
            this.axios
                .post("https://www.bobview.org:8080/api/order/active/",{
                    restaurant_name:this.$route.query.r,
                    table_id:this.$route.query.t
                })
                .then((response) => {
                    console.log(response.data)
                    if(response.data['restaurant']['restaurant_image'] != ''){
                        this.restaurant_img_src = response.data['restaurant']['restaurant_image']
                    }
                    this.restaurantInfo = response.data['restaurant']
                    this.activeOrders = response.data['menus']
                    this.order_id = response.data['order_id']
                    console.log(this.activeOrders)
                    if(response.data['message'] == 'failed'){
                        this.tableIsActive = false
                    } else {
                        this.tableIsActive = true
                    }
                    console.log(this.distance)
                })
                .catch((error) => {
                    console.log("senserver error")
                    console.log(error)
                })
        },
        toRating() {
            if(this.$store.getters.isLogined){
                this.$router.push('/dashboard/orderlist')
                return
            }
            else {
                console.log(this.order_id)
                this.$store.commit('setOrderId', {
                    orderId : this.order_id,
                })
                this.$store.commit('setRedirectDomain',{
                    redirectDomain:'/dashboard/orderlist'
                })
                this.$router.push('/login')
            }
        },
    },
}
</script>

<style lang="scss" scoped>

</style>