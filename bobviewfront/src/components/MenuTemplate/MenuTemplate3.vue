<template>
    <v-container>
        <v-card
        class="mx-auto"
        max-width="400"
        color="white"
        >
            <v-card-text class="text-center text--primary font-weight-black" style="font-size:2.5em"> 
                <div>{{menuinfo.menu_name}}</div>
            </v-card-text>
            <v-img
            class="align-end"
            max-height="200"
            max-width="400"
            :src="menuinfo.menu_image"
            >
            </v-img>

            <v-card-text class="text-center font-italic font-weight-light" style="font-size:1.2em">  
                <div>{{menuinfo.menu_price + "KRW"}}</div>
            </v-card-text>
            <v-card-text class="text-center text--primary font-weight-light" style="font-size:1.3em"> 
                <div>{{menuinfo.menu_desc}}</div>
            </v-card-text>

            <v-card-actions class="justify-center">
            <v-btn
                color="pink"
                text
                @click="$emit('addBasket', menuinfo)"
            >
                Order
            </v-btn>
            </v-card-actions>
        </v-card>
    </v-container>
</template>

<script>
export default {
    name:'menuTemplate3',
    props:['menuinfo'],
    methods:{
    }
}
</script>

<style lang="scss" scoped>

</style>