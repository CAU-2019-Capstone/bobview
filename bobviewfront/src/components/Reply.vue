<template>
    <v-container max-width="1000" max-height="800">
        <v-row class="mx-5 d-flex align-center elevation-1">
            <v-col cols="3">
                <p font-weight-thin>{{hiddenId(comment_data['user'])}}</p>
            </v-col>
            <v-col cols="9">
                <p>{{comment_data['comment']}}</p>    
            </v-col>
        </v-row>
    </v-container>
</template>

<script>
export default {
    name:'reply',
    props:['comment_list_id'],
    mounted() {
        console.log("reply")
        console.log(this.comment_list_id)
        this.initialize()
    },
    data(){
        return {   
            comment_data:{},
            isRestReview:false,
            isMenuReview:false,
            sampleComment:'this is samplw'
        }
    },
    methods: {
        initialize(){
            this.axios.get('http://localhost:8000/api/commentlist/'+this.comment_list_id+'/')
            .then((result)=>{
                console.log(result.data)
                this.comment_data = result.data
            })
            .catch(function(error){
                console.log(error)
                console.log("senserver error")
            });
            let currentObj = this
            setTimeout( function() {
                currentObj.axios.get(currentObj.comment_data['user'])
                    .then((result)=>{
                        console.log(result.data)
                        currentObj.comment_data['user'] = result.data
                    })
                    .catch(function(error){
                        console.log(error)
                        console.log("senserver error")
                    });
                if(currentObj.comment_data.rest_rating!=null){
                    currentObj.axios.get(currentObj.comment_data['rest_rating'])
                        .then((result)=>{
                            console.log(result.data)
                            currentObj.comment_data['rest_rating'] = result.data[0]
                            currentObj.isRestReview=true
                        })
                        .catch(function(error){
                            console.log(error)
                            console.log("senserver error")
                        });
                }
                if(currentObj.comment_data.menu_rating!=null){
                    currentObj.axios.get(currentObj.comment_data['menu_rating'])
                        .then((result)=>{
                            console.log(result.data)
                            currentObj.comment_data['menu_rating'] = result.data[0]
                            currentObj.isMenuReview=true
                        })
                        .catch(function(error){
                            console.log(error)
                            console.log("senserver error")
                        });
                }
            },500)
        },
        hiddenId(user){
            let hiddenId = user['username'].substr(0,3)+"***"
            return user['first_name'] + "(" + hiddenId + ")"
        }
    }
}
</script>

<style lang="scss" scoped>

</style>