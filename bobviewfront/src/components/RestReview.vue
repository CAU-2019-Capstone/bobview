<template>
    <v-card>
        <v-card-media src="src" height="200px">
        </v-card-media>
        <v-card-title primary-title>
            <div>
                <h3 class="headline mb-0">Rest</h3>
                <div>{{rest_rating_id}}</div>
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
    name:'restReview',
    props:['rest_rating_id'],
    mounted() {

    },
    data() {
        return{

        }
    },
    methods: {

    }
}
</script>

<style lang="scss" scoped>

</style>