<template>
    <v-container grid-list-xs>
        <v-card>
            <v-content v-if="tableIsActive" class="my-10">
                <v-row class="justify-center">
                    주문이 진행중인 테이블입니다!
                </v-row>
                <v-row class="justify-center">
                    주문번호 : {{this.order_id}}
                </v-row>
                <v-row class="justify-center">
                    <v-btn text depressed @click="toRating()">
                        <span>평가하러 가기</span>
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
                    >
                        <span>주문하기</span>
                    </v-btn>
                    <v-btn
                        text
                        depressed
                        to="/order/menu"
                        props:
                        v-if="tableIsActive"
                    >
                        <span>추가 주문하기</span>
                    </v-btn>
                </v-row>
                <v-row  class="justify-center">
                    <v-btn
                        text
                        depressed
                        to="/order/call"
                    >
                        <span>요청하기</span>
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
            restaurant : this.$route.query.r,
            table : this.$route.query.t,
            gettingLocation : false,
            location : false,
            errorStr : false,
            tableIsActive :false,
            activeOrders : [],
            order_id: 0,
            restaurant_img_src:'@/assets/logo.png',
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
        
        // get position
        navigator.geolocation.getCurrentPosition(pos => {
            this.gettingLocation = true;
            this.location = pos;
        }, err => {
            this.gettingLocation = false;
            this.errorStr = err.message;
        })
    },

    mounted() { 
        console.log("order main mounted") 
        console.log(this.$store.getters.isLogined)
        if(this.$route.query.r == undefined){
            this.$router.push("/")
        }
        this.$store.commit('SetOrderInfo',{
            restaurant_name : this.$route.query.r,
            table_number : this.$route.query.t,
        })
        console.log(this.$store.getters.RestaurantName)
        console.log(this.$store.getters.TableNumber)

        this.getOrderMenus()
    },

    methods: {
        getOrderMenus() {
            this.axios
                .post("http://localhost:8000/api/order/active/",{
                    restaurant_name:this.$route.query.r,
                    table_id:this.$route.query.t
                })
                .then((response) => {
                    console.log(response.data)
                    if(response.data['restaurant_image'] != ''){
                        this.restaurant_img_src = response.data['restaurant_image']
                    }
                    this.activeOrders = response.data['menus']
                    this.order_id = response.data['order_id']
                    console.log(this.activeOrders)
                    if(response.data['message'] == 'failed'){
                        this.tableIsActive = false
                    } else {
                        this.tableIsActive = true
                    }
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
        }
    },
}
</script>

<style lang="scss" scoped>

</style>