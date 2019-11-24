<template>
    <v-card
    max-width="1000"
    max-height="800"
    >
        <v-container class="d-flex align-start">
            <v-row>
                <v-col cols="8">
                    <v-img
                    max-width="660"
                    height="200"
                    :src="menuinfo.menu_image"
                    >
                    </v-img>
                </v-col>
                <v-col cols="4">
                    <v-row>
                        <v-card-title justify-center>{{menuinfo.menu_name}}</v-card-title>
                    </v-row>
                    <v-row>
                        <v-card-text>{{menuinfo.menu_price + "KRW"}}</v-card-text>
                    </v-row>
                    <v-row>
                        <v-card-text class="text--primary">{{menuinfo.menu_desc}}</v-card-text>
                    </v-row>
                    <v-row>
                        <v-card-actions>
                            <v-btn
                                color="red"
                                text
                                @click="$emit('addBasket', menuinfo)"
                            >
                                Order
                            </v-btn>
                        </v-card-actions>
                    </v-row>
                </v-col>
            </v-row>
        </v-container>
    </v-card>
</template>



<script>
export default {
    name:'menuTemplate2',
    props:['menuinfo'],
    methods:{
    }
}
</script>

<style lang="scss" scoped>

</style>

