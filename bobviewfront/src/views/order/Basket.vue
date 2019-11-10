<template>
    <v-content>
        <v-card v-if="loaded">
            <v-card-title primary-title>
                <h3>Basket</h3>
            </v-card-title>
            <v-content class="px-5 py-2">
                <v-container>
                    <v-row
                        v-for="basket in basketMenus"
                        :key="basket.menu_name"
                        class="headline mb-0">                                
                        <v-col cols="8">
                            {{basket.menu_name}} x {{basket.count}}
                        </v-col>
                        <v-col cols="4">
                            <v-btn fab depressed small class="mr-3" @click="setItem(basket, 'plus')">
                                <v-icon>mdi-plus</v-icon>
                            </v-btn>
                            <v-btn fab depressed small @click="setItem(basket, 'minus')">
                                <v-icon>mdi-minus</v-icon>
                            </v-btn>
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
            <v-card-actions>
                <v-spacer/>
                <v-btn text depressed @click="Payment">결제</v-btn>
                <v-btn text depressed @click="cancel">취소</v-btn>
            </v-card-actions>
            <v-content v-if="basketEmpty">
                <p class="text-center">장바구니를 추가해 주세요</p>
            </v-content>
        </v-card>
    </v-content>
</template>

<script>
export default {
    name:'basket',
    props:['dialog'],
    data() {
        return {
            basketMenus: [],
            loaded:false,
            basketEmpty:false,
            totPrice:0,
            basketCount:0,
        }
    },
    created() {
        console.log("basket created")
        this.initMenus()
    },
    mounted() {
        console.log("basket mounted")
        let currentObj = this
        setInterval(function() {
            console.log("updated")
            this.basketMenus =  currentObj.$store.getters.GetBasketMenus
            this.basketCount = currentObj.$store.getters.GetBasketCount
        },1000)
    },
    watch: {
        dialog: function() {
            this.initMenus()
        },
        basketCount: function() {
            this.totPrice = 0
            for (let [index] in this.basketMenus) {
                this.totPrice = this.totPrice + this.basketMenus[index]['menu_price'] * this.basketMenus[index]['count']
            }
        },
    },
    methods: {
        initMenus() {
            this.loaded = false
            this.basketMenus =  this.$store.getters.GetBasketMenus
            this.basketCount = this.$store.getters.GetBasketCount
            this.loaded = true
        },
        Payment() {
            if(this.totPrice == 0) {
                this.basketEmpty = true
            } else {
                let payload = {
                    status:false,
                    to:'payment'
                }
                this.$emit('toggle',payload)
            }
        },
        cancel() {
            let payload = {
                status:false,
                to:'cancel'
            }
            this.$emit('toggle',payload)
        },
        setItem(item, status) {
            let currentItemIndex = this.basketMenus.indexOf(item)
            if(status == 'plus'){
                this.$store.commit('addBasketCount')
                this.$store.commit('addItemCount', {
                    index:currentItemIndex
                })
            }
            else if(status == 'minus'){
                this.$store.commit('subBasketCount')
                this.$store.commit('subItemCount', {
                    index:currentItemIndex
                })
            }
            this.initMenus()
        }
    }
}
</script>

<style lang="scss" scoped>

</style>