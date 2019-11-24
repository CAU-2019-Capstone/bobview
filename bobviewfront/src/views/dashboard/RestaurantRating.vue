<template>
<v-container>
    <v-select
      :items="restaurants"
      label="Restaurant list"
      outlined
      v-model="selectedRestaurant"
    ></v-select>
    <v-card class="justify-space-around mx-2 my-2"
      v-if="getRaingData"
    >
        <v-data-table
          :headers="headers"
          :items="currentRatingInfo"
          sort-by="rest_rating_id"
          class="elevation-1"
        >
          <template v-slot:top>
          <v-toolbar flat color="white">
              <v-toolbar-title>Restaurant Rating List</v-toolbar-title>
              <v-divider
              class="mx-4"
              inset
              vertical
              ></v-divider>
          </v-toolbar>
          </template>
          <template v-slot:item.action="{ item }">
            <v-icon
                small
                class="mr-2"
                @click="manageItem(item)"
            >mdi-circle-edit-outline
            </v-icon>
            <v-dialog
            v-model="dialog"
            max-width="1000"
            >
              <restReview v-bind:rest_rating_id="item['rest_rating_id']"></restReview>
            </v-dialog>
          </template>
          <template v-slot:no-data>
            <v-btn color="primary" @click="initialize">Reset</v-btn>
          </template>
      </v-data-table>
    </v-card>
  </v-container>
</template>
<script>
import RestReview from "@/components/RestReview"
export default {
  components:{
    RestReview
  },
  data: () => ({
    dialog:false,
    getRaingData:true,
    headers: [
      {
        text: 'Rating_id',
        align: 'left',
        value: 'rest_rating_id',
      },
      { text: 'Rating', value: 'rating', align: 'right' },
      { text: 'Description', value: 'desc', sortable: false, align: 'right', divider:true },
      { text: 'Reply', value: 'action', sortable: false, align: 'right' },
    ],
    restaurants: [],
    selectedRestaurant:'',
    currentRatingInfo:[],
    tempRatingData: [
        {
            restaurant:'',
            user:'',
            rest_rating_id:1,
            rating:5.0,
            desc:'bobview is good restaurant'
        },
        {
            restaurant:'',
            user:'',
            rest_rating_id:2,
            rating:5.0,
            desc:'bobview is good restaurant longggggggg ggggggggggggg gggggggg ggggggggggg ggggggggggggggggg gggggggggggggg gggggggggggggg ggggg'
        },
    ],
  }),

  created () {
    this.initialize()
  },
  watch: {
    selectedRestaurant: function () {
      this.getRatingInfo()
    },
    dialog : function(val) {
      val || this.close()
    }
  },
  methods: {
    initialize () {
      this.axios
      .get('localhost:8000/api/restaurantinfo/0/?owner='+this.$store.state.userdata['username'])
      .then((result) => {
          console.log(result.data)
          for(let [index] in result.data){
            this.restaurants.push(result.data[index]['restaurant_name'])
          }
          this.selectedRestaurant = this.restaurants[0]
          this.getRatingInfo()
          this.getRestaurantData = true
      })
      .catch(function(error) {
          console.log("senserver error")
          console.log(error)
      });
    },
    getRatingInfo() {
      this.getRestaurantData =false
      console.log("restaurants")
      console.log(this.restaurants)
      console.log("selectedRestaurant")
      console.log(this.selectedRestaurant)
      let currentObj = this
      //get restaurant rating
      this.axios
          .get('http://localhost:8000/api/restrating/0/?restaurant_name='+this.selectedRestaurant)
          .then((result) => {
              console.log("menuinfo data")
              console.log(result.data)
              this.currentRatingInfo = result.data
              currentObj.getRestaurantData = true
          })
          .catch(function(error) {
              console.log("senserver error")
              console.log(error)
          });

        //get menu's rating
    },
    manageItem(item) {
      this.dialog=true
      console.log(item)
    },
    close(){
      this.dialog=false
    }
  },
}
</script>

<style lang="scss" scoped>

</style>