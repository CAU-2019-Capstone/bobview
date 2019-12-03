<template>
    <v-container>
        <v-card
            class="mx-auto py-2"
            max-width="450"
        >
            <v-img
            class="align-end"
            max-width="450"
            max-height="250"
            :src="menuinfo.menu_image"
            >
            <v-card-title justify-center class="white--text">{{menuinfo.menu_name}}</v-card-title>
            </v-img>

            <v-card-text class="text--primary">
                <div><v-icon>mdi-star</v-icon>x{{tot_rating}}</div>
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
            <v-spacer/>
            <v-btn
                color="orange"
                text
                @click="showDetailReviews"
                v-if="tot_rating != 0"
            >
                Show Review on This
            </v-btn>
            </v-card-actions>
        </v-card>
        <v-dialog
            v-model="dialog"
            max-width="500">
            <v-content>
                <v-row
                v-for="menurating in menuratingList"
                :key="menurating.menu_rating_id"
                >
                    <menuReview v-bind:menu_rating_id="menurating.menu_rating_id"></menuReview>
                </v-row>
            </v-content>
        </v-dialog>
    </v-container>
</template>

<script>

import MenuReview from "@/components/MenuReview"
export default {
    components: {
        MenuReview
    },
    name:'menuTemplate1',
    props:['menuinfo'],
    data() {
        return{
            dialog:false,
            menuratingList : []
        }
    },
    computed : {
        tot_rating : function(){
            var rating_num = 0.0
            var count = 0.0
            for(let [index] in this.menuratingList){
                rating_num = rating_num + this.menuratingList[index]['rating']
                count++
            }
            console.log(rating_num)
            console.log(count)
            if(count > 0){
                rating_num = rating_num / count
            }
            else {
                rating_num = 0
            }
            return rating_num
        },
    },
    watch: {
        dialog : function(val){
            val || this.close()
        }
    },
    mounted () {
        let currentObj = this
        currentObj.axios.get('https://www.bobview.org:8080/api/menurating/0/?menu_id='+currentObj.menuinfo.menu_id)
            .then(function(response){
                console.log(response.data)
                currentObj.menuratingList = response.data
            })
            .catch(function(error){
                console.log(error)
            });
    },
    methods:{
        showDetailReviews() {
            this.dialog = true
        },
        close() {
            this.dialog = false
        }
    }
}
</script>

<style lang="scss" scoped>

</style>