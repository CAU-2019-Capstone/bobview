<template>
    <v-container>
        <v-content>
            <v-btn text depressed @click="makeCode">click</v-btn>
            <v-img
                
                :src="qrcode"
            >

            </v-img>
        </v-content>
    </v-container>
</template>

<script>
export default {
    
    components: {

    },
    data () {
        return {
            domain : 'http://localhost:8080/order/main/?r=dalcomm&t=1',
            width:100,
            height:100,
            qrcode:'',
            responseComplete:false
        }
    },
    mounted(){
    },
    methods: {
        makeCode(){
            this.$router.push({ 
                path: '/restaurant/qrcode', 
                query: { 
                    restaurant_name: 'dalcomm', 
                    table_num : 15
                }
            })
        }
    }
}
</script>

<style lang="scss" scoped>

</style>