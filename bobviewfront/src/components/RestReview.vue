<template>
    <v-card width="1000" height="auto" v-if="loadRating" class="elevation-7">
        <v-container primary-title>
            <v-row>
                <v-col cols="8">
                    <h4>Restaurant Review</h4>
                    <h3 class="headline mb-0">{{rest_rating['restaurant']['restaurant_name']}}</h3>
                </v-col>
                <v-col cols="4">
                    <h4>Location</h4>
                    <h3>{{rest_rating['restaurant']['restaurant_address']}}</h3>
                </v-col>
            </v-row>
            <v-row class="ml-3">
                <span><v-icon>mdi-star</v-icon>{{rest_review}}</span>
            </v-row>
        </v-container>
        <v-divider></v-divider>
        <v-img
            contain
            max-width="1000"
            max-height="200"
            :src="rest_rating['restaurant']['restaurant_image']"
        >
        </v-img>
        <div class="justify-center" v-if="rest_rating['restaurant']['restaurant_image']==''">No Image</div>
        <v-divider></v-divider>
        <div class="px-4 py-2">
            <h5>{{rest_rating['user']['username']}} 's Review</h5>
            <h4 class="mb-0">{{rest_rating['desc']}}</h4>
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
        <v-container v-if="reply">
            <v-row
            v-for="comment in comments"
            :key="comment.comment_list_id"
            >
                <reply 
                v-bind:comment_list_id="comment.comment_list_id">
                </reply>
            </v-row>
            <v-row
            v-if="comments.length == 0"
            >
            <span>No Comments...</span>
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
        </v-container>
    </v-card>
</template>

<script>
import Reply from "@/components/Reply"
export default {
    components:{
        Reply
    },
    name:'restReview',
    props:['rest_rating_id'],
    mounted() {
        this.initiate()
        this.initcomment()
    },
    data() {
        return{
            notUsed:false,
            loadRating:false,
            rest_rating:{},
            reply:false,
            isLiked:false,
            isUnliked:false,
            btnChanged:false,
            like:0,
            comments:[],
            rest_rating_list:[]
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
    computed:{
        rest_review : function(){
            var sum=0.0
            var count=0.0
            for(let [index] in this.rest_rating_list){
                sum = sum + this.rest_rating_list[index]['rating']
                count = count + 1
            }
            console.log(sum)
            console.log(count)
            if(count > 0.0){
                return sum/count
            }
            return 0.0
        }
    },
    methods: {
        initiate() {
            this.newComment=''
            this.loadRating = false
            let currentObj = this
            this.axios
                .get('https://www.bobview.org:8080/api/restrating/'+currentObj.rest_rating_id+'/')
                .then((result)=>{
                    console.log(result.data)
                    this.rest_rating = result.data[0]
                })
                .catch(function(error){
                    console.log(error)
                    console.log("senserver error")
                });
            setTimeout(function(){
                currentObj.axios
                    .get(currentObj.rest_rating['restaurant'])
                    .then((result)=>{   
                        console.log(result.data)
                        currentObj.rest_rating['restaurant'] = result.data[0]
                    })
                    .catch(function(error){
                        console.log(error)
                        console.log("senserver error")
                    });
                currentObj.axios
                    .get('https://www.bobview.org:8080/api/userinfo/'+currentObj.rest_rating['user'].split('/')[5]+'/')
                    .then((result)=>{   
                        console.log(result.data)
                        currentObj.rest_rating['user'] = result.data
                    })
                    .catch(function(error){
                        console.log(error)
                        console.log("senserver error")
                    });
            },500)
            setTimeout(function() {
                currentObj.axios
                .get('https://www.bobview.org:8080/api/restrating/0/?restaurant_name='+currentObj.rest_rating['restaurant']['restaurant_name'])
                .then((result)=>{
                    console.log(result.data)
                    currentObj.rest_rating_list = result.data
                })
                .catch(function(error){
                    console.log(error)
                    console.log("senserver error")
                });
                currentObj.loadRating = true
                currentObj.btnChanged =true
            },1000)
        },
        moreReply() {
            this.reply=!this.reply
        },
        setLike(val){
            this.like = val
        },
        initcomment() {
            let currentObj = this
            currentObj.axios
                .get('https://www.bobview.org:8080/api/commentlist/0/?rest_rating_id='+currentObj.rest_rating_id)
                .then((result)=>{
                    console.log(result.data)
                    currentObj.comments = result.data
                })
                .catch(function(error){
                    console.log(error)
                    console.log("senserver error")
                });
        },
        sendComment() {
            let currentObj = this
            currentObj.axios.post('https://www.bobview.org:8080/api/commentlist/',{
                rest_rating_id:currentObj.rest_rating_id,
                username:currentObj.$store.getters.GetUserdata['username'],
                comment:currentObj.newComment
            })
            .then(function(response){
                console.log(response.data)
                currentObj.newComment=''
            })
            .catch(function(error){
                console.log(error)
                console.log("senserver error")
            });
            setTiemout(function(){
                currentObj.initcomment()
            },500)
        },
    }
}
</script>

<style lang="scss" scoped>

</style>