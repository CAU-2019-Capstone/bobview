<template>
    <v-container grid-list-xs
    fill-hegiht
    fluid>
        <v-card-text>
            <v-text-field label="search" v-model="search"></v-text-field>
        </v-card-text>
        <v-content>
          <v-row
          class="justify-space-around mb-2"
          v-for="review in reviewLists"
          :key="review.rating_id"
          >
            <restReview v-bind:rest_rating_id="review.rating_id" v-if="review.rest_rating_type"></restReview>
            <menuReview v-bind:menu_rating_id="review.rating_id" v-if="review.menu_rating_type"></menuReview>
          </v-row>
        </v-content>
    </v-container>
</template>

<script>
import RestReview from "@/components/RestReview"
import MenuReview from "@/components/MenuReview"
export default {
  components:{
    RestReview,
    MenuReview,
  },
  data() {
    return {
      search:null,
      reviewLists:[
        {
          rating_id:1,
          rest_rating_type:true,
        },
        {
          rating_id:2,
          menu_rating_type:true,
        },
      ],
    };
  },

}
</script>

<style lang="scss" scoped>

</style>