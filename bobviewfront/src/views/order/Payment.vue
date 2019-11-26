<template>
    <v-card>
        <v-container class="px-2 py-2">
            <v-row v-if="isLogined" class="justify-space-around my-2">
                <v-card outlined width="1000">
                    <v-card-title primary-title>
                        <h3>User Info</h3>
                    </v-card-title>
                    <v-content class="px-5 py-2">
                        <v-container>
                            <v-row>
                                <v-col cols="4">
                                    <span class>id</span>
                                </v-col>
                                <v-divider></v-divider>
                                <v-col cols="8">
                                    <p class="text-right">{{userinfo.username}}</p>
                                </v-col>
                            </v-row>
                            <v-row>
                                <v-col cols="4">
                                    <span class>email</span>
                                </v-col>
                                <v-divider></v-divider>
                                <v-col cols="8">
                                    <p class="text-right">{{userinfo.email}}</p>
                                </v-col>
                            </v-row>
                            <v-row>
                                <v-col cols="4">
                                    <span class>name</span>
                                </v-col>
                                <v-divider></v-divider>
                                <v-col cols="8">
                                    <p class="text-right">{{userinfo.first_name}}</p>
                                </v-col>
                            </v-row>
                        </v-container>
                    </v-content>
                </v-card>
            </v-row>
            <v-row class="justify-space-around my-2">
                <v-card outlined width="1000">
                    <v-card-title primary-title>
                        <h3>Basket</h3>
                    </v-card-title>
                    <v-content class="px-5 py-2">
                        <v-container>
                            <v-row
                                v-for="basket in basketMenus"
                                :key="basket.menu_name"
                                class="headline mb-0">                                
                                <v-col cols="10">
                                    <span class>{{basket.menu_name}}</span>
                                </v-col>
                                <v-col cols="2">
                                    <p class="text-right">{{basket.count}}</p>
                                </v-col>
                            </v-row>
                            <v-divider/>
                            <v-row>
                                <v-content>
                                    <h2>Total Price : {{totPrice}}</h2>
                                </v-content>
                            </v-row>
                        </v-container>
                    </v-content>
                </v-card>
            </v-row>
            <v-divider/>
            <v-row class="justify-space-around my-2">
                <v-card outlined width="1000">
                    <v-card-title primary-title>
                        <h3>Payment Method</h3>
                    </v-card-title>
                    <v-content class="px-5 py-2">
                        <v-container>
                            <v-row>
                                <v-radio-group v-model="selectedPayment" row>
                                    <v-radio label="Kakao Pay" value="easyPay"></v-radio>
                                    <v-radio disabled label="Card" value="card"></v-radio>
                                    <v-radio disabled label="Wrie" value="account"></v-radio>
                                    <v-radio disabled label="Phone" value="phone"></v-radio>
                                    <v-radio disabled label="CMS(Virtual Account)" value="abstractAccount"></v-radio>
                                </v-radio-group>
                            </v-row>
                        </v-container>
                    </v-content>
                    <v-card-actions>
                        <v-spacer/>
                        <v-btn :disabled="!methodSelected" text depressed @click="Payment">Continue</v-btn>
                    </v-card-actions>
                </v-card>
            </v-row>
        </v-container>
    </v-card>
</template>

card
<script>
export default {
    name: 'payment',
    mounted() {
        let restaurant = this.$store.getters.RestaurantName
        let table = this.$store.getters.TableNumber
        if(restaurant == '' || table == undefined){
            this.$router.push("/")
        }
        if(this.$store.getters.isLogined){
            this.initUserInfo()
        }
    },
    data() {
        return {
            isLogined : this.$store.getters.isLogined,
            basketMenus: this.$store.getters.GetBasketMenus,
            paymentMethods : [
                {
                    name:'card'
                }
            ],
            selectedPayment:null,
            resultFailed:false,
            userinfo:[],
        }
    },
    computed: {
        totPrice: function() {
            let price = 0;

            for(let [index] in this.basketMenus){
                price = price + this.basketMenus[index]['menu_price']*this.basketMenus[index]['count']
            }

            return price;
        },
        methodSelected: function() {
            if(this.selectedPayment != null){
                return true
            }
            return false
        }
    },
    methods: {
        initUserInfo() {
            let owner = this.$store.getters.GetUserdata['username']
            this.isLogined = false
            this.axios
                .get('localhost:8000/api/userinfo/0/?username='+owner)
                .then((result) => {
                    console.log(result.data)
                    this.userinfo = result.data
                    this.isLogined = true
                })
                .catch(function(error) {
                    console.log("senserver error")
                    console.log(error)
                });
        },
        Payment() {
            console.log("payment...")
            if(this.totPrice ==0){
                return
            }
            let currentObj = this
            var msg=''
            IMP.init('imp52136590')
            IMP.request_pay({
                pg : 'html5_inicis',
                pay_method : 'card',
                merchant_uid : 'merchant_' + new Date().getTime(),
                name : 'Order name:Payment Test',
                amount : this.totPrice,
                buyer_email : 'iamport@siot.do',
                buyer_name : this.$store.getters.GetUserdata['username'],
                buyer_tel : '010-0000-0000',
                buyer_addr : 'Hannam',
                buyer_postcode : '04747',
            }, function(rsp) {
                if ( rsp.success ) {
                    msg = 'Payment Success, \nGo to complete page';
                    currentObj.axios
                        .post("localhost:8000/api/order/create/",{
                            username:currentObj.$store.getters.GetUserdata['username'],
                            restaurant_name : currentObj.$store.getters.RestaurantName,
                            table_id : currentObj.$store.getters.TableNumber,
                            basket_menus:currentObj.$store.getters.GetBasketMenus
                        })
                        .then(function(response) {
                            console.log(response.data)
                            currentObj.$store.commit('setOrderId',{
                                orderId : response.data['order_id']
                            })
                            currentObj.$store.commit('initBasket')
                        })
                        .catch(function(error) {
                            console.log("senserver error")
                            console.log(error)
                        });
                    setTimeout(function(){
                        alert(msg);
                        currentObj.$router.push("/order/complete/")
                    }, 1000);
                } else {
                    msg = 'Payment Failed. \n Please check your error code';
                    msg += 'Error : ' + rsp.error_msg;
                    this.resultFailed=true
                    alert(msg);
                }
            });

        }
    }
}
</script>

<style lang="scss" scoped>

</style>