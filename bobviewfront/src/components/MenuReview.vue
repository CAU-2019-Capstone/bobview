<template>
    <v-card width="1000" height="auto" v-if="loadRating" class="elevation-7">
        <v-card-title primary-title>
            <div>
                <h5>Menu name</h5>
                <h3 class="headline mb-0">{{menu_rating['menu']['menu_name']}}</h3>
            </div>
        </v-card-title>
        <v-divider></v-divider>
        <v-img
            contain
            max-width="1000"
            max-height="200"
            :src="menu_rating['menu']['menu_image']"
        >
        </v-img>
        <v-divider></v-divider>
        <div class="px-4 py-2">
            <h5>Menu Description</h5>
            <h5 class="mb-0">{{menu_rating['desc']}}</h5>
        </div>
        <v-card-actions>
            <v-spacer/>
            <v-container v-if="notUsed">
                <v-btn  v-if="!isLiked" fab depressed small @click="setLike(1)">
                    <v-icon>mdi-thumb-up-outline</v-icon>
                </v-btn>
                <v-btn  v-if="isLiked" fab depressed small @click="setLike(0)">
                    <v-icon>mdi-thumb-up</v-icon>
                </v-btn>
                <v-btn v-if="!isUnliked" fab depressed small @click="setLike(-1)">
                    <v-icon>mdi-thumb-down-outline</v-icon>
                </v-btn>
                <v-btn v-if="isUnliked" fab depressed small @click="setLike(0)">
                    <v-icon>mdi-thumb-down</v-icon>
                </v-btn>
            </v-container>
            <v-btn fab depressed small @click="moreReply">
                <v-icon>mdi-chevron-down</v-icon>
            </v-btn>
        </v-card-actions>
        <v-divider></v-divider>
        <v-content v-if="reply">
            <v-row
            v-for="comment in comments"
            :key="comment.comment_list_id"
            >
                <reply 
                v-bind:comment_list_id="comment.comment_list_id"
                ></reply>
            </v-row>
            <v-divider></v-divider>
            <v-row class="mx-5 d-flex align-center"
            v-if="$store.getters.isLogined"
            >
                <v-text-field 
                outlined
                label="Comment"
                v-model="newComment"
                class="mt-5 mr-2"
                ></v-text-field>
                <v-btn small fab depressed @click="sendComment">
                    <v-icon>mdi-send</v-icon>
                </v-btn>
            </v-row>
        </v-content>
    </v-card>
</template>
<script>
import Reply from "@/components/Reply"
export default {
    components:{
        Reply
    },
    name:'menuReview',
    props:['menu_rating_id'],
    mounted() {
        this.initiate()
    },
    data() {
        return{
            notUsed:false,
            loadRating:false,
            menu_rating:{},
            reply:false,
            isLiked:false,
            isUnliked:false,
            btnChanged:false,
            like:0,
            comments:[],
            newComment:'',
        }
    },
    watch:{
        like :function(val){
            if(val == 0){
                this.isLiked = false
                this.isUnliked = false
            }
            if(val == 1){
                this.isLiked = true
                this.isUnliked = false
            }
            if(val == -1){
                this.isLiked = false
                this.isUnliked = true
            }
            
        }
    },
    methods: {
        initiate() {
            this.newComment = ''
            this.loadRating = false
            let currentObj = this
            this.axios
                .get('http://localhost:8000/api/menurating/'+currentObj.menu_rating_id+'/')
                .then((result)=>{
                    console.log(result.data)
                    this.menu_rating = result.data[0]
                })
                .catch(function(error){
                    console.log(error)
                    console.log("senserver error")
                });
            this.axios
                .get('http://localhost:8000/api/commentlist/0/?menu_rating_id='+this.menu_rating_id)
                .then((result)=>{
                    console.log(result.data)
                    this.comments = result.data
                })
                .catch(function(error){
                    console.log(error)
                    console.log("senserver error")
                });
            setTimeout(function(){
                currentObj.axios
                    .get(currentObj.menu_rating['menu'])
                    .then((result)=>{   
                        console.log(result.data)
                        currentObj.menu_rating['menu'] = result.data[0]
                    })
                    .catch(function(error){
                        console.log(error)
                        console.log("senserver error")
                    });
                currentObj.axios
                    .get('http://localhost:8000/api/userinfo/'+currentObj.menu_rating['user'].split('/')[5]+'/')
                    .then((result)=>{   
                        console.log(result.data)
                        currentObj.menu_rating['user'] = result.data
                    })
                    .catch(function(error){
                        console.log(error)
                        console.log("senserver error")
                    });
            },500)
            setTimeout(function() {
                currentObj.loadRating = true
                currentObj.btnChanged =true
                console.log(currentObj.menu_rating)
            },500)
        },
        moreReply() {
            this.reply=!this.reply
        },
        setLike(val){
            this.like = val
        },
        sendComment() {
            this.axios.post('http://localhost:8000/api/commentlist/',{
                menu_rating_id:this.menu_rating_id,
                username:this.$store.getters.GetUserdata['username'],
                comment:this.newComment
            })
            .then(function(response){
                console.log(response.data)
                this.initiate()
            })
            .catch(function(error){
                console.log(error)
                console.log("senserver error")
            });
        },
    }
}
</script>

<style lang="scss" scoped>

</style>