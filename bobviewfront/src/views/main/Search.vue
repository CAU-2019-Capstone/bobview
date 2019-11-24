<template>
    <v-container grid-list-xs
    fill-hegiht
    fluid>
        <v-card-text class="justify-space-around">
          <v-text-field label="search" v-model="search"
          append-outer-icon="mdi-send"
          @click:append-outer="searchKeyword"
          ></v-text-field>
        </v-card-text>
        <v-container>
          <v-row
          class="justify-space-around mb-2 mx-2"
          v-for="review in reviewLists"
          :key="review.rating_id"
          >
            <restReview v-bind:rest_rating_id="review.rating_id" v-if="review.rest_rating_type"></restReview>
            <menuReview v-bind:menu_rating_id="review.rating_id" v-if="review.menu_rating_type"></menuReview>
          </v-row>
        </v-container>
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
      search:'',
      reviewLists:[],
    };
  },

  mounted() {
    console.log("search keyword" + this.$route.query.keyword)
    this.search = this.$route.query.keyword
    this.axios.post('http://localhost:8000/api/community/search/', {
        search_string : this.$route.query.keyword
    })
      .then((result)=>{
        console.log(result.data)
        let rest_review_list = result.data['search_contents_rest']
        let menu_review_list = result.data['search_contents_menu']
        for(let [index] in rest_review_list){
          this.reviewLists.push({
            rating_id:rest_review_list[index]['rest_rating_id'],
            rest_rating_type:true
          })
        }
        for(let [index] in menu_review_list){
          this.reviewLists.push({
            rating_id:menu_review_list[index]['menu_rating_id'],
            menu_rating_type:true
          })
        }
      })
      .catch(function(error){
        console.log(error)
        console.log("senserver error")
      });
  },
  methods : {
    searchKeyword() {
      if(this.search != ''){
        this.reviewLists = []
        this.axios.post('http://localhost:8000/api/community/search/', {
            search_string : this.search
        })
          .then((result)=>{
            console.log(result.data)
            let rest_review_list = result.data['search_contents_rest']
            let menu_review_list = result.data['search_contents_menu']
            for(let [index] in rest_review_list){
              this.reviewLists.push({
                rating_id:rest_review_list[index]['rest_rating_id'],
                rest_rating_type:true
              })
            }
            for(let [index] in menu_review_list){
              this.reviewLists.push({
                rating_id:menu_review_list[index]['menu_rating_id'],
                menu_rating_type:true
              })
            }
          })
          .catch(function(error){
            console.log(error)
            console.log("senserver error")
          });
      }
    }
  }
}
</script>

<style lang="scss" scoped>

</style>