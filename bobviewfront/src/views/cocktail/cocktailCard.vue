<template>
    <v-card>
        <v-img
            :src="cocktailInfo.drink_image"
            height="200px"
        >
        </v-img>
        <v-card-title primary-title>
            <div>
                <div class="headline">{{cocktailInfo.name}}</div>
                <v-icon :color="alcoholic">mdi-glass-cocktail</v-icon>
            </div>
        </v-card-title>
        <v-content class="justify-space-around px-6 py-2" v-if="detail">
            <v-row>
                <v-col>
                    <p>Category : </p>
                </v-col>
                <v-col>
                    {{cocktailInfo.category}}
                </v-col>
            </v-row>
            <v-row>
                <v-col>
                    <p>Glass : </p>
                </v-col>
                <v-col>
                    {{cocktailInfo.glass}}
                </v-col>
            </v-row>
            <v-row>
                <p>Instruction : </p>
            </v-row>
            <v-row>
                {{cocktailInfo.instruction}}
            </v-row>
        </v-content>
        <v-card-actions>
            <v-btn v-if="!detail" text depressed color="red lighten-3" @click="$emit('selectLike', cocktailInfo)">
                Like
            </v-btn>
            <v-btn v-if="!detail" text depressed color="red lighten-3" @click="$emit('selectUnlike', cocktailInfo)">
                Unlike
            </v-btn>
        </v-card-actions>
    </v-card>
</template>

<script>
export default {
    name : 'cocktailCard',
    props:['cocktailInfo', 'detail'],
    mounted(){
        if(this.cocktailInfo.alcoholic == true){
            this.alcoholic = 'red' 
        }
        else {
            this.alcoholic = 'green'
        }
    },
    data() {
        return {
            alcoholic : ''
        }
    },
    methods:{

    }
}
</script>

<style lang="scss" scoped>

</style>