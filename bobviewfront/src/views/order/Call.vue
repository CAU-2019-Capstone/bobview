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
                                @click="action(CallMenu.text)"
                            >
                                {{ CallMenu.name }}
                            </v-btn>
                    </v-col>
                </v-row>
                <v-row 
                    v-if="moreRequest"
                    class="mx-10">
                    <v-text-field box label="label" v-model="requestText"></v-text-field>
                </v-row>
                <v-row
                    v-if="moreRequest"
                >
                    <v-spacer></v-spacer>
                    <v-btn
                        text
                        @click="action('requestMsg')"
                    >
                        Send
                    </v-btn>
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
            moreRequest : false
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
    methods: {
        initMenus() {
            this.CallMenus = [
                {
                    name: '점원 부르기',
                    text: 'callClerk'
                },
                {
                    name: '물 주세요',
                    text: 'water'
                },
                {
                    name: '물수건 주세요',
                    text: 'waterTissue'
                },
                {
                    name: '그 외..',
                    text: 'etc'
                },
                
            ]
        },
        action(event) {
            if(event == 'etc'){
                this.moreRequest=true
            }
            if(event == 'requestMsg'){
                console.log("")
            }
        }
    },
}
</script>

<style lang="scss" scoped>

</style>