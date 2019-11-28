<template>
    <v-app>
        <v-container>
            <v-toolbar>
                <v-btn text depressed outlined class="mx-2" @click="reset">
                    Cocktail Recommendation System
                </v-btn>
                <v-spacer/>
                <v-btn v-if="!recommended" :disabled="likeCocktails.length==0 && unlikeCocktails.length==0" text depressed outlined class="mx-2" @click="showRecomment">
                    show recommendation!
                </v-btn>
                <v-btn v-else text depressed outlined class="mx-2" @click="showRandom">
                    select More!
                </v-btn>
                <v-btn text depressed outlined @click="pass" class="red">
                    pass
                </v-btn>
            </v-toolbar>
            <v-row v-if="loading">
                <v-progress-linear :indeterminate="true"></v-progress-linear>
            </v-row>
            <v-row v-else>
                <v-col
                v-for="cocktailInfo in randomInfos"
                :key="cocktailInfo.cocktail_id"
                cols="12" lg="3" sm="6"
                >
                    <cocktailCard v-bind:detail="detail" v-bind:cocktailInfo="cocktailInfo" @selectLike="likeSelect" @selectUnlike="unlikeSelect"></cocktailCard>
                </v-col>
            </v-row>
            <v-container v-if="!recommended">
                <v-toolbar class="my-10">
                    <h3>Your Like in Selected Cocktail</h3>
                </v-toolbar>
                <v-row>
                    <v-col
                    v-for="cocktailInfo in likeCocktails"
                    :key="cocktailInfo.cocktail_id"
                    cols="12" lg="3" sm="6"
                    >
                        <cocktailCard v-bind:detail="!detail" v-bind:cocktailInfo="cocktailInfo"></cocktailCard>
                    </v-col>
                </v-row>
            </v-container>
            <v-container v-if="!recommended">
                <v-toolbar class="my-10">
                    <h3>Your Unlike in Selected Cocktail</h3>
                </v-toolbar>
                <v-row>
                    <v-col
                    v-for="cocktailInfo in unlikeCocktails"
                    :key="cocktailInfo.cocktail_id"
                    cols="12" lg="3" sm="6"
                    >
                        <cocktailCard v-bind:detail="!detail" v-bind:cocktailInfo="cocktailInfo"></cocktailCard>
                    </v-col>
                </v-row>
            </v-container>




            <v-row class="justify-between">
                <v-col cols="10">
                    <v-tabs
                    v-model="tab">
                    <v-tabs-slider></v-tabs-slider>
                        <v-tab 
                        v-for="template in templateMenus"
                        :key="template.id"
                        >
                        <p>{{template.desc}}</p>
                        </v-tab>
                    </v-tabs>
                </v-col>
            </v-row>
            <v-row>
                <v-tabs-items v-model="tab">
                    <v-tab-item
                        v-for="template in templateMenus"
                        :key="template.id"
                    >
                        <v-container v-if="template.id==1">
                            <v-toolbar class="my-10" v-if="recommended">
                                <h3>We Recomment These Cocktails to YOU!</h3>
                            </v-toolbar>
                            <v-row v-if="recommended" class="justify-center">
                                <v-col
                                v-for="cocktailInfo in recommendedInfos"
                                :key="cocktailInfo.cocktail_id"
                                cols="12" lg="3" sm="6"
                                >
                                    <cocktailCard v-bind:detail="!detail" v-bind:cocktailInfo="cocktailInfo" @selectLike="likeSelect" @selectUnlike="unlikeSelect"></cocktailCard>
                                </v-col>
                            </v-row>
                        </v-container>
                        <v-container v-if="template.id==2">
                            <v-toolbar class="my-10" v-if="recommended">
                                <h3>We Recomment These Cocktails to YOU! - CF (Item-Based)</h3>
                            </v-toolbar>
                            <v-row v-if="recommended" class="justify-center">
                                <v-col
                                v-for="cocktailInfo in recommendedInfosCFUser"
                                :key="cocktailInfo.cocktail_id"
                                cols="12" lg="3" sm="6"
                                >
                                    <cocktailCard v-bind:detail="!detail" v-bind:cocktailInfo="cocktailInfo" @selectLike="likeSelect" @selectUnlike="unlikeSelect"></cocktailCard>
                                </v-col>
                            </v-row>
                            <v-toolbar class="my-10" v-if="recommended">
                                <h3>We Recomment These Cocktails to YOU! - CF (User-Based)</h3>
                            </v-toolbar>
                            <v-row v-if="recommended" class="justify-center">
                                <v-col
                                v-for="cocktailInfo in recommendedInfosCFItem"
                                :key="cocktailInfo.cocktail_id"
                                cols="12" lg="3" sm="6"
                                >
                                    <cocktailCard v-bind:detail="!detail" v-bind:cocktailInfo="cocktailInfo" @selectLike="likeSelect" @selectUnlike="unlikeSelect"></cocktailCard>
                                </v-col>
                            </v-row>
                        </v-container>
                        <v-container v-if="template.id==3">
                            <v-toolbar class="my-10" v-if="recommended">
                                <h3>We Recomment These Cocktails to YOU! - CBF</h3>
                            </v-toolbar>
                            <v-row v-if="recommended" class="justify-center">
                                <v-col
                                v-for="cocktailInfo in recommendedInfosCBF"
                                :key="cocktailInfo.cocktail_id"
                                cols="12" lg="3" sm="6"
                                >
                                    <cocktailCard v-bind:detail="!detail" v-bind:cocktailInfo="cocktailInfo" @selectLike="likeSelect" @selectUnlike="unlikeSelect"></cocktailCard>
                                </v-col>
                            </v-row>
                        </v-container>
                        <v-container v-if="template.id==4">
                            <v-toolbar class="my-10">
                                <h3>Your Like in Selected Cocktail</h3>
                            </v-toolbar>
                            <v-row>
                                <v-col
                                v-for="cocktailInfo in likeCocktails"
                                :key="cocktailInfo.cocktail_id"
                                cols="12" lg="3" sm="6"
                                >
                                    <cocktailCard v-bind:detail="!detail" v-bind:cocktailInfo="cocktailInfo"></cocktailCard>
                                </v-col>
                            </v-row>
                        </v-container>
                        <v-container v-if="template.id==5">
                            <v-toolbar class="my-10">
                                <h3>Your Unlike in Selected Cocktail</h3>
                            </v-toolbar>
                            <v-row>
                                <v-col
                                v-for="cocktailInfo in unlikeCocktails"
                                :key="cocktailInfo.cocktail_id"
                                cols="12" lg="3" sm="6"
                                >
                                    <cocktailCard v-bind:detail="!detail" v-bind:cocktailInfo="cocktailInfo"></cocktailCard>
                                </v-col>
                            </v-row>
                        </v-container>
                    </v-tab-item>
                </v-tabs-items>
            </v-row>
        </v-container>
    </v-app>
</template>

<script>
import cocktailCard from "@/views/cocktail/cocktailCard"
export default {
    name : 'cocktail',
    components:{
        cocktailCard
    },
    mounted(){
        this.initCocktailDB()
        let currentObj = this
        setTimeout(function(){
            currentObj.selectRandom(12)
        },1000)
        
    },
    data() {
        return {
            tab:0,
            recommended:false,
            detail:false,
            loading:false,
            cocktailInfos:[],
            randomInfos:[],
            likeCocktails:[],
            unlikeCocktails:[],
            recommendedInfosCFItem:[],
            recommendedInfosCFUser:[],
            recommendedInfosCBF:[],
            recommendedInfos:[],
            templateMenus:[
                {
                    id:1,
                    desc:'Our Recommend'
                },
                {
                    id:2,
                    desc:'Recommend CF'
                },
                {
                    id:3,
                    desc:'Recommend CBF'
                },
                {
                    id:4,
                    desc:"Your Like Selected Item's info"
                },
                {
                    id:5,
                    desc:"Your Unlike Selected Item's info"
                }
            ]
        }
    },
    methods:{
        initCocktailDB(){
            this.loading = true
            let currentObj = this
            this.axios.get('https://www.bobview.org:8080/api/cocktail/')
            .then(function(response){
                console.log(response.data)
                currentObj.cocktailInfos = response.data
            })
            .catch(function(error){
                console.log(error)
                console.log("senserver error")
            })
        },
        selectRandom(number){
            this.randomInfos = []
            var result
            let duplicated = false
            for(var i=0;i<number;i++){
                result = Math.floor(Math.random() * 546);
                console.log("random")
                console.log(this.randomInfos)
                console.log("selected")
                console.log(this.likeCocktails)
                if(this.likeCocktails.length > 0){
                    for(let [index] in this.likeCocktails){
                        if(result == this.likeCocktails[index]['cocktail_id']){
                            duplicated=true
                        }
                    }
                }
                if(this.unlikeCocktails.length > 0){
                    for(let [index] in this.unlikeCocktails){
                        if(result == this.unlikeCocktails[index]['cocktail_id']){
                            duplicated=true
                        }
                    }
                }
                if(this.randomInfos.length > 0){
                    for(let [index] in this.randomInfos){
                        if(result == this.randomInfos[index]['cocktail_id']){
                            duplicated=true
                        }
                    }
                }
                if(!duplicated){
                    this.randomInfos.push(this.cocktailInfos[result])
                }
                else{
                    i--
                }
                duplicated=false
                console.log(this.randomInfos)
            }
            this.loading=false
        },
        likeSelect(item){
            this.loading=true
            this.likeCocktails.push(item)
            this.selectRandom(12)
            this.loading=false
        },
        unlikeSelect(item){
            this.loading=true
            this.unlikeCocktails.push(item)
            this.selectRandom(12)
            this.loading=false
        },
        pass(){
            this.loading=true
            this.selectRandom(12)
            this.loading=false
        },
        showRecomment(){
            this.loading = true
            this.recommended = true
            this.tab = 0
            this.randomInfos = []
            this.getRecommendInfo()
        },
        showRandom() {
            this.recommended = false
            this.selectRandom(12)
        },
        
        getRecommendInfo(){
            this.loading = true
            let currentObj = this
            currentObj.axios.post('https://www.bobview.org:8080/api/cocktail/recommend/CF/',{
            like_list : currentObj.likeCocktails,
            unlike_list : currentObj.unlikeCocktails,
            })
            .then(function(response){
                console.log("CF Result")
                console.log(response.data)
                currentObj.recommendedInfosCFUser = response.data['user_based_recommend']
                currentObj.recommendedInfosCFItem = response.data['item_based_recommend']
                currentObj.loading = false
            })
            .catch(function(error){

            console.log(error)
            console.log("senserver error")
            })
            currentObj.axios.post('https://www.bobview.org:8080/api/cocktail/recommend/CBF/',{
                like_list : currentObj.likeCocktails,
                count : 10
            })
            .then(function(response){
                console.log("CBF Result")
                console.log(response.data['result_lists'])
                currentObj.recommendedInfosCBF = response.data['result_lists']
                currentObj.loading = false
            })
            .catch(function(error){
                console.log(error)
                console.log("senserver error")
            })

            setTimeout(function(){
                currentObj.recommendedInfos = []
                for(let [index] in currentObj.recommendedInfosCFUser){
                    currentObj.recommendedInfos.push(currentObj.recommendedInfosCFUser[index])
                }
                for(let [index] in currentObj.recommendedInfosCFItem){
                    currentObj.recommendedInfos.push(currentObj.recommendedInfosCFItem[index])
                }
                console.log("rec infos")
                console.log(currentObj.recommendedInfos)
                if(currentObj.recommendedInfos.length == 0){
                    console.log("length 0")
                    currentObj.recommendedInfos = currentObj.recommendedInfosCBF
                }
                else if(currentObj.recommendedInfos.length > 0 && currentObj.recommendedInfos.length < 20){
                    console.log("length 1~20")
                    currentObj.axios.post('https://www.bobview.org:8080/api/cocktail/recommend/CBF/',{
                        like_list : currentObj.recommendedInfos,
                        count : 20 - currentObj.recommendedInfos.length
                    })
                    .then(function(response){
                        console.log("CF and CBF Result")
                        console.log(response.data)
                        for(let [index] in response.data['result_lists']){
                            currentObj.recommendedInfos.push(response.data['result_lists'][index])
                        }
                        currentObj.loading = false
                        console.log("our result")
                        console.log(currentObj.recommendedInfos)
                    })
                    .catch(function(error){
                        console.log(error)
                        console.log("senserver error")
                    })
                }
                else {
                    console.log("length 21~")
                }

            },3000)

            
        },
        reset() {
            let currentObj = this
            currentObj.likeCocktails = []
            currentObj.unlikeCocktails = []
            currentObj.recommendedInfosCFUser = []
            currentObj.recommendedInfosCFItem = []
            currentObj.recommendedInfosCBF = []
            currentObj.recommendedInfos = []
            currentObj.recommended = false
            currentObj.randomInfos = []
            currentObj.selectRandom(12)
        }
    }
}
</script>

<style lang="scss" scoped>

</style>