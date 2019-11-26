<template>
    <v-app>
        <v-container>
            <v-toolbar>
                <h3>Cocktail Recommendation System</h3>
                <v-spacer/>
                <v-btn v-if="!recommended" text depressed outlined class="mx-2" @click="showRecomment">
                    show recommendation!
                </v-btn>
                <v-btn v-else text depressed outlined class="mx-2" @click="showRandom">
                    select More!
                </v-btn>
                <v-btn text depressed outlined @click="pass">
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
                    <cocktailCard v-bind:detail="detail" v-bind:cocktailInfo="cocktailInfo" @select="selected"></cocktailCard>
                </v-col>
            </v-row>
            <v-toolbar class="my-10" v-if="recommended">
                <h3>We Recomment These Cocktails to YOU!</h3>
            </v-toolbar>
            <v-row v-if="recommended">
                <v-col
                v-for="cocktailInfo in recommendedInfos"
                :key="cocktailInfo.cocktail_id"
                cols="12" lg="3" sm="6"
                >
                    <cocktailCard v-bind:detail="!detail" v-bind:cocktailInfo="cocktailInfo" @select="selected"></cocktailCard>
                </v-col>
            </v-row>
            

            <v-toolbar class="my-10">
                <h3>Your Selected Cocktail</h3>
            </v-toolbar>
            <v-row>
                <v-col
                v-for="cocktailInfo in selectedCocktails"
                :key="cocktailInfo.cocktail_id"
                cols="12" lg="3" sm="6"
                >
                    <cocktailCard v-bind:detail="!detail" v-bind:cocktailInfo="cocktailInfo" @select="selected"></cocktailCard>
                </v-col>
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
            recommended:false,
            detail:false,
            loading:false,
            cocktailInfos:[],
            randomInfos:[],
            selectedCocktails:[]
        }
    },
    methods:{
        initCocktailDB(){
            this.loading = true
            let currentObj = this
            this.axios.get('http://localhost:8000/api/cocktail/')
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
                console.log(this.selectedCocktails)
                if(this.selectedCocktails.length > 0){
                    for(let [index] in this.selectedCocktails){
                        if(result == this.selectedCocktails[index]['cocktail_id']){
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
        selected(item){
            this.loading=true
            this.selectedCocktails.push(item)
            this.selectRandom(12)
            this.loading=false
        },
        pass(){
            this.loading=true
            this.selectRandom(12)
            this.loading=false
        },
        showRecomment(){
            this.recommended = true
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
            currentObj.axios.post('http://13.124.90.6:8080/api/cocktail/recommend',this.selectedCocktails)
                .then(function(response){
                    console.log(response.data)
                })
                .cathc(function(error){
                    console.log(error)
                    console.log("senserver error")
                })
            
        }
    }
}
</script>

<style lang="scss" scoped>

</style>