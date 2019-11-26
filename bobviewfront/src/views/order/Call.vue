<template>
    <v-container grid-list-xs>
        <v-card>
            <v-content class="px-5 py-2">
                <v-row>
                    <v-col 
                    v-for="CallMenu in CallMenus"
                    :key="CallMenu.name"
                    cols="12" sm="6"
                    class="d-flex justify-space-around"
                    >
                            <v-btn
                                text
                                @click="action(CallMenu.name)"
                            >
                                {{ CallMenu.name }}
                            </v-btn>
                    </v-col>
                </v-row>
                <v-row 
                    class="mx-10">
                    <v-text-field outlined label="label" v-model="requestText"></v-text-field>
                </v-row>
                <v-row v-if="oneMoreRequest" class="justify-center">
                    Really send the message? Please click one more time
                </v-row>
                <v-row>
                    <v-spacer></v-spacer>
                    <v-btn
                        text
                        @click="action('requestMsg')"
                        :disabled="buttonDisable"
                    >
                        Send
                    </v-btn>
                </v-row>
                <v-row v-if="sendSuccess">
                    Send Success! Please Wait
                </v-row>
            </v-content>
        </v-card>
    </v-container>
</template>

<script>
export default {
    name :'call',
    data() {
        return{
            CallMenus:[],
            requestText:'',
            moreRequest : false,
            sendSuccess : false,
            buttonDisable : false,
            oneMoreRequest : false,
            sendedMessage : ''
        }
    },
    mounted() {
        console.log("login mounted") 
        console.log(this.$store.getters.isLogined)
        if(this.$store.getters.isLogined){
            this.$router.push("/")
        }
        this.initMenus()
    },
    watch:{
        requestText:function(val){
            if(val == this.sendedMessage){
                this.buttonDisable = true
            }
        }
    },
    methods: {
        initMenus() {
            let restaurant = this.$store.getters.RestaurantName
            let table = this.$store.getters.TableNumber
            if(restaurant == '' || table == undefined){
                this.$router.push("/")
            }
            this.CallMenus = [
                {
                    name: 'Call Clerk',
                },
                {
                    name: 'Give Us Water',
                },
                {
                    name: 'Give Us Towel',
                },
                {
                    name: 'Give Us Tableware',
                },
                {
                    name: 'Clean the Table',
                },
            ]
        },
        action(event) {
            let currentObj = this
            if(event == 'requestMsg'){
                if(currentObj.oneMoreRequest == false){
                    currentObj.oneMoreRequest = true
                    return
                }
                currentObj.oneMoreRequest = false
                currentObj.axios
                    .post('https://www.bobview.org:8080/api/messages/',{
                        restaurant_name : currentObj.$store.getters.RestaurantName,
                        message : currentObj.requestText,
                        table_id : currentObj.$store.getters.TableNumber
                    })
                    .then(function(response){
                        console.log(response)
                        currentObj.sendSuccess = true
                        currentObj.sendedMessage = event
                    })
                    .catch(function(error){
                        console.log(error)
                        console.log("senserver error")
                    });
            }
            else {
                currentObj.requestText = event
            }
        }
    },
}
</script>

<style lang="scss" scoped>

</style>