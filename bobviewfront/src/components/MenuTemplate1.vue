<template>
    <v-container>
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
    name:'menuTemplate1',
    props:['menuinfo'],
    methods:{
    }
}
</script>

<style lang="scss" scoped>

</style>