<template>
<v-container>
    <v-card class="justify-space-around" max-width="1000">
        <v-card-title primary-title class="justify-space-around ma-2">
            <div class="my-10">
                <h2 class="headline mb-0">결제가 완료되었습니다.</h2>
            </div>
        </v-card-title>
        <v-content>
            <v-row class="justify-space-around">
                <span>주문번호 : {{order_id}}</span>
            </v-row>
        </v-content>
        <v-card-actions >
            <v-container>
                <v-row class="justify-space-around mb-10">
                    <v-btn depressed text @click="routerGo('rating')">주문음식 리뷰하러 가기</v-btn>
                </v-row>
                <v-spacer></v-spacer>
                <v-row class="justify-space-around">
                    <v-btn depressed text @click="routerGo('main')">메인 화면 으로</v-btn>
                </v-row>
                <v-row class="justify-space-around">
                    <v-btn depressed text @click="routerGo('addOrder')">추가 주문 하기</v-btn>
                </v-row>
            </v-container>
        </v-card-actions>
    </v-card>
</v-container>

</template>

<script>
export default {
    mounted() {
        this.loadData()
        //save database - order_info + user
        //active table order
    },
    data() {
        return {
            currentOrderMenus:[],
            order_id:0,
        }
    },
    methods: {
        loadData() {
            console.log("load current order data")
            console.log("order_id : "+this.$store.getters.GetOrderId)
            this.axios
                .get('http://localhost:8000/api/ordercontents/0/?order_id='+this.$store.getters.GetOrderId)
                .then((result) => {
                    console.log(result.data)
                    this.currentOrderMenus=result.data
                })
                .catch(function(error) {
                    console.log("senserver error")
                    console.log(error)
                });
            this.order_id = this.$store.getters.GetOrderId
        },
        routerGo(to) {
            if(to == 'main'){
                this.$router.push('/order/main/?r='+this.$store.getters.RestaurantName+'&t='+this.$store.getters.TableNumber)
            }
            if(to == 'addOrder'){
                this.$router.push('/order/menu')
            }
            if(to == 'rating') {
                if(this.$store.getters.isLogined){
                    this.$router.push('/dashboard/orderlist')
                    return
                }
                else {
                    this.$store.commit('setRedirectDomain',{
                        redirectDomain:'/dashboard/orderlist'
                    })
                    this.$router.push('/login')
                }
            }
        }
    }
}
</script>

<style lang="scss" scoped>

</style>