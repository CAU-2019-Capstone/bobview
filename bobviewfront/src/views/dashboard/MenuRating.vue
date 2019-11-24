<template>
<v-container class="mb-10">
    <v-select
      :items="restaurants"
      label="Restaurant list"
      outlined
      v-model="selectedRestaurant"
    ></v-select>
    <v-container v-if="!isUpdated">
        <v-progress-linear
            indeterminate
            color="yellow darken-2"
        ></v-progress-linear>
    </v-container>
    <v-container v-if="isUpdated">
        <v-card class="justify-space-around mx-2 my-5"
        v-for="ratingList in currentRatingInfo"
        :key="ratingList.menu_id"
        >
            <v-data-table
            :headers="headers"
            :items="ratingList.ratings"
            sort-by="menu_rating_id"
            class="elevation-1"
            >
            <template v-slot:top>
            <v-toolbar flat color="white">
                <v-toolbar-title>{{ratingList.menu_name}}</v-toolbar-title>
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
                    <menuReview v-bind:menu_rating_id="item['menu_rating_id']"></menuReview>
                </v-dialog>
            </template>
            <template v-slot:no-data>
                <v-btn color="primary" @click="initialize">Reset</v-btn>
            </template>

        </v-data-table>
        </v-card>
    </v-container>
  </v-container>
</template>
<script>
import MenuReview from "@/components/MenuReview"
export default {
    components:{
        MenuReview
    },
    data: () => ({
        dialog:false,
        isUpdated:false,
        headers: [
            {
            text: 'Rating_id',
            align: 'left',
            value: 'menu_rating_id',
            },
            { text: 'Rating', value: 'rating', align: 'right' },
            { text: 'Description', value: 'desc', sortable: false, align: 'right', divider:true },
            { text: 'Reply', value: 'action', sortable: false, align: 'right' },
        ],
        restaurants: [],
        selectedRestaurant:'',
        currentRatingInfo:[],
    }),

    created () {
        this.initialize()
    },
    watch: {
        dialog: function(val){
            val || this.close()
        },
        selectedRestaurant: function () {
            this.getRatingInfo()
        },
    },
    methods: {
        initialize () {
            this.isUpdated = false
            this.axios
            .get('http://localhost:8000/api/restaurantinfo/0/?owner='+this.$store.state.userdata['username'])
            .then((result) => {
                console.log(result.data)
                for(let [index] in result.data){
                this.restaurants.push(result.data[index]['restaurant_name'])
                }
                this.selectedRestaurant = this.restaurants[0]
                this.getRatingInfo()
                this.isUpdated = true
            })
            .catch(function(error) {
                console.log("senserver error")
                console.log(error)
            });
        },
        getRatingInfo() {
            this.isUpdated =false
            console.log("restaurants")
            console.log(this.restaurants)
            console.log("selectedRestaurant")
            console.log(this.selectedRestaurant)
            let currentObj = this
            //get restaurant rating
            if(this.selectedRestaurant == undefined){
                currentObj.isUpdated=true
                return
            }
            this.axios
            .get('http://localhost:8000/api/menuinfo/0/?restaurant_name='+this.selectedRestaurant)
            .then((result) => {
                console.log("menuinfo data")
                console.log(result.data)
                this.currentRatingInfo=[]
                for(let [index] in result.data){
                    this.currentRatingInfo.push({
                        menu_name:result.data[index]['menu_name'],
                        menu_id:result.data[index]['menu_id']
                    })
                }
            })
            .catch(function(error) {
                console.log("senserver error")
                console.log(error)
            });
            setTimeout(function(){
                for(let [index] in currentObj.currentRatingInfo){
                    currentObj.axios
                        .get('http://localhost:8000/api/menurating/1/?menu_id='+currentObj.currentRatingInfo[index]['menu_id'])
                        .then((result) => {
                            console.log(currentObj.currentRatingInfo[index]['menu_name']+'\'s rating')
                            console.log(result.data)
                            currentObj.currentRatingInfo[index]['ratings']=result.data
                        })
                        .catch(function(error) {
                            console.log("senserver error")
                            console.log(error)
                        });
                }
                setTimeout(function(){
                    console.log(currentObj.currentRatingInfo)
                    currentObj.isUpdated=true
                },1000)
            }, 1000);
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