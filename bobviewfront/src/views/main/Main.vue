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
          <v-row v-if="!sending" class="justify-space-around my-5">
            <v-btn text depressed @click="loadMore" class="blue-grey lighten-4">
              Load More
            </v-btn>
          </v-row>
          <v-row v-if="sending" class="justify-space-around">
            <v-progress-circular
            indeterminate
            color="primary"
            ></v-progress-circular>
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
      sending:false,
      search:'',
      reviewLists:[],
    };
  },

  mounted() {
    this.sending = true
    this.axios.get('http://localhost:8000/api/community/main/')
    .then((result)=>{
        console.log(result.data)
        for(let [index] in result.data['items']){
            if(result.data['items'][index].hasOwnProperty('rest_rating_id')){
                this.reviewLists.push({
                  rating_id:result.data['items'][index]['rest_rating_id'],
                  rest_rating_type:true
                })
            }
            if(result.data['items'][index].hasOwnProperty('menu_rating_id')){
                this.reviewLists.push({
                  rating_id:result.data['items'][index]['menu_rating_id'],
                  menu_rating_type:true
                })
            }
        }
        this.sending = false
        console.log(this.rest_rating_list)
        console.log(this.menu_rating_list)
    })
    .catch(function(error){
        console.log(error)
        console.log("senserver error")
    });
  },
  methods : {
    onScroll (e) {
      this.offsetTop = e.target.scrollTop
    },
    loadMore() {
      this.sending = true
      let rest_rating_list = []
      let menu_rating_list = []
      for(let [index] in this.reviewLists){
        if(this.reviewLists[index].hasOwnProperty('rest_rating_type')){
          rest_rating_list.push(this.reviewLists[index]['rating_id'])
        }
        if(this.reviewLists[index].hasOwnProperty('menu_rating_type')){
          menu_rating_list.push(this.reviewLists[index]['rating_id'])
        }
      }
      this.axios.post('http://localhost:8000/api/community/main/',{
          existing_rest_rating_list: rest_rating_list,
          existing_menu_rating_list: menu_rating_list
        })
        .then((result)=>{
          console.log(result.data)
          for(let [index] in result.data['items']){
            if(result.data['items'][index].hasOwnProperty('rest_rating_id')){
                this.reviewLists.push({
                  rating_id:result.data['items'][index]['rest_rating_id'],
                  rest_rating_type:true
                })
            }
            if(result.data['items'][index].hasOwnProperty('menu_rating_id')){
                this.reviewLists.push({
                  rating_id:result.data['items'][index]['menu_rating_id'],
                  menu_rating_type:true
                })
            }
          }
          this.sending = false
          console.log(this.rest_rating_list)
          console.log(this.menu_rating_list)
        })
        .catch(function(error){
            console.log(error)
            console.log("senserver error")
        });
    },
    searchKeyword() {
      if(this.search != ''){
        this.$router.push("/review/search/?keyword=" + this.search)
      }
    }
  }
}
</script>

<style lang="scss" scoped>

</style>