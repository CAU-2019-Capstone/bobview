<template>
    <v-card max-width="1000" max-height="800">
        <v-card-media :src="menu_image" max-width="900" height="400px">
        </v-card-media>
        <v-card-title primary-title>
            <div>
                <h3 class="headline mb-0">Menu</h3>
                <div>{{menu_rating_id}}</div>
            </div>
        </v-card-title>
        <v-card-actions>
            <v-btn flat color="primary">text</v-btn>
            <v-btn flat color="primary">text</v-btn>
        </v-card-actions>
    </v-card>
</template>

<script>
export default {
    name:'menuReview',
    props:['menu_rating_id'],
    mounted() {

    },
    data() {
        return{
            menu_image:''
        }
    },
    methods: {

    }
}
</script>

<style lang="scss" scoped>

</style>