<template>
    <v-container>
        <v-card
            class="mx-auto"
            max-width="450"
            max-height="400"
        >
            <v-img
            class="align-end"
            max-width="450"
            max-height="250"
            :src="menuinfo.menu_image"
            >
            <v-card-title justify-center>{{menuinfo.menu_name}}</v-card-title>
            </v-img>

            <v-card-text class="text--primary">
                <div><v-icon>mdi-star x{{tot_rating}}</v-icon></div>
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
            >
                Order
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
            var rating_num = 0
            var count = 0;
            for(let [index] in this.menuratingList){
                rating_num = rating_num + this.menuratingList[index]['rating']
                count++
            }
            rating_num = rating_num / count
            return rating_num.toFiexd(1)
        }
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