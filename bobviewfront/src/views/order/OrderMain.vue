<template>
    <v-container grid-list-xs>
        <v-card>
            <v-card-title primary-title>
                restaurant : {{ this.$route.query.r }}     table : {{this.$route.query.t}}
            </v-card-title>
            <v-content class="px-5 py-2">
                <v-row class="justify-center">
                    <v-img
                        src="@/assets/logo.png"
                        max-height="300"
                    ></v-img>
                </v-row>
                <v-row  class="justify-center py-5"> 
                    <v-btn
                        text
                        depressed
                        to="/order/menu"
                        props:
                    >
                        <span>Order</span>
                    </v-btn>
                </v-row>
                <v-row  class="justify-center py-5">
                    <v-btn
                        text
                        depressed
                        to="/order/call"
                    >
                        <span>Call</span>
                    </v-btn>
                </v-row>
                <v-row v-if="gettingLocation">
                    Your location data is {{ location.coords.latitude }}, {{ location.coords.longitude}}
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
            errorStr : false
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
    },

    methods: {
        hello() {

        }
    },
}
</script>

<style lang="scss" scoped>

</style>