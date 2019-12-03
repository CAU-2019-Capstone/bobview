<template>
    <v-card 
    class="elevation-5 px-12 my-5"
    max-width="500"
    >
        <v-card-title v-if="progressSuccess" primary-title class="justify-space-around">
            <div >
                <h2 class="headline mb-2">Rate Me!</h2>
                <span sm>{{restaurantRating['restaurant_name']}}</span>
            </div>
        </v-card-title>
        <v-container v-if="progressSuccess">
            <v-content>
                <v-row class="justify-space-around">
                    <v-img
                        :src="restaurantRating['restaurant_image']"
                        lazy-src="https://picsum.photos/id/11/10/6"
                        max-width="400"
                        max-height="100"
                    >
                    </v-img>
                </v-row>
                <v-row>
                    <v-content>
                        <v-row class="justify-center">
                            <span small>Order Menus</span>
                        </v-row>
                        <v-row
                            v-for="ordermenu in ordermenus"
                            :key="ordermenu['menu']['menu_id']"
                        >
                            <v-col cols="8">
                                <div class="d-flex align-center">
                                    <p class="text-left ml-5">{{ordermenu['menu']['menu_name']}}</p>
                                </div>
                            </v-col>
                            <v-col cols="2">
                                <div class="mx-5">
                                    <p class="text-right">{{ordermenu['menu_num']}}</p>
                                </div>
                            </v-col>
                            <v-col cols="2">
                                <v-btn small fab depressed @click="rateMenu(ordermenu)">
                                    <v-icon v-if="ordermenu.rated">mdi-star</v-icon>
                                    <v-icon v-else>mdi-star-outline</v-icon>
                                </v-btn>
                            </v-col>
                        </v-row>
                    </v-content>
                </v-row>
                <v-row class="justify-space-around my-10">
                    <v-rating
                        v-model="restaurantRating['rating']"
                        length="5"
                        half-increments
                        color="blue"
                        hover
                    ></v-rating>
                </v-row>
                <v-row class="mx-3">
                    <v-textarea 
                    full-width
                    single-line
                    outlined 
                    label="description" 
                    v-model="restaurantRating['desc']"></v-textarea>
                </v-row>
            </v-content>
            <v-content v-if="rateSuccess"
            >
                <p>Review Applied!</p>
            </v-content>
            <v-card-actions v-if="!rateSuccess">
                <v-spacer/>
                <v-btn depressed text @click="save">Apply</v-btn>
                <v-btn v-if="!forActive" depressed text @click="close">Cancel</v-btn>
            </v-card-actions>
            

            <v-dialog
            v-model="dialog"
            max-width="400"
            >
            <v-card>
                <v-card-title class="headline">{{editedItem['menu_name']}}</v-card-title>
                <v-content>
                    <v-row class="justify-space-around my-10">
                        <v-img
                            :src="editedItem['menu_image']"
                            lazy-src="https://picsum.photos/id/11/10/6"
                            max-width="350"
                            max-height="150"
                        >
                        </v-img>
                    </v-row>
                    <v-row class="justify-space-around my-10">
                        <v-rating
                            v-model="editedItem['menu_rating']"
                            length="5"
                            half-increments
                            color="blue"
                            hover
                        ></v-rating>
                    </v-row>
                    <v-row class="mx-3">
                        <v-textarea 
                        full-width
                        single-line
                        outlined 
                        label="description" 
                        v-model="editedItem['desc']"></v-textarea>
                    </v-row>
                </v-content>
                <v-card-actions>
                <v-spacer></v-spacer>

                <v-btn
                    color="green darken-1"
                    text
                    @click="saveItem"
                >
                    Save
                </v-btn>

                <v-btn
                    color="green darken-1"
                    text
                    @click="closeItem"
                >
                    Cancle
                </v-btn>
                </v-card-actions>
            </v-card>
            </v-dialog>
        </v-container>
        <v-progress-linear
        v-if="!progressSuccess"
        indeterminate
        color="teal"
        max-width="500"
        ></v-progress-linear>
    </v-card>
</template>

<script>
export default {
    name:'review',
    props:['order_id', 'updated', 'active'],
    mounted() {
        //console.log("review mounted, order_id : " + this.order_id)
        this.initMenus()
        if(active=true){
            this.forActive = true
        }
    },
    watch: {
        order_id: function() {
            this.initMenus()
        },
        dialog (val) {
            val || this.closeItem()
        },
        updated: function() {
            this.initMenus()
        },
    },
    data() {
        return {
            forActive:false,
            progressSuccess:false,
            dialog:false,
            restaurantRating:{},
            editedItem:{},
            defaultItem : {
                menu_name:'',
                menu_image:'',
                desc:'',
                menu_rating:0,
            },
            tempItem: {},
            editedRating:0,
            editedDesc:'',
            ordermenus: [],
            rateSuccess:false,
        }
    },
    methods : {
        save() {
            //console.log(this.ordermenus)
            //console.log(this.restaurantRating)
            let currentObj = this
            if(currentObj.restaurantRating['rating'] == undefined){
                return
            }
            if(currentObj.restaurantRating['rating']>0){
                for(let [index] in  currentObj.ordermenus){
                    if(currentObj.ordermenus[index]['rated'] == false){
                        currentObj.ordermenus[index]['menu_rating']['rating'] = currentObj.restaurantRating['rating']
                    }
                    if(this.ordermenus[index]['menu_rating']['menu_rating_id'] != undefined){
                        currentObj.axios.put('https://www.bobview.org:8080/api/menurating/'+currentObj.ordermenus[index]['menu_rating']['menu_rating_id']+'/', {
                            rating:currentObj.ordermenus[index]['menu_rating']['rating'],
                            desc:currentObj.ordermenus[index]['menu_rating']['desc'],
                        })
                        .then((result) => {
                            console.log(result.data)
                        })
                        .catch(function(error) {
                            console.log("senserver error")
                            console.log(error)
                        });
                    } else {
                        currentObj.axios.post('https://www.bobview.org:8080/api/menurating/', {
                            menu_id : currentObj.ordermenus[index]['menu']['menu_id'],
                            username : currentObj.$store.getters.GetUserdata['username'],
                            rating:currentObj.ordermenus[index]['menu_rating']['rating'],
                            desc:currentObj.ordermenus[index]['menu_rating']['desc'],
                        })
                        .then((result) => {
                            console.log(result.data)
                        })
                        .catch(function(error) {
                            console.log("senserver error")
                            console.log(error)
                        });
                    }
                }
            }
            if(currentObj.restaurantRating['rest_rating_id'] == undefined){
                currentObj.axios.post('https://www.bobview.org:8080/api/restrating/', {
                    restaurant_name : currentObj.restaurantRating['restaurant_name'],
                    username : currentObj.$store.getters.GetUserdata['username'],
                    rating:currentObj.restaurantRating['rating'],
                    desc:currentObj.restaurantRating['desc'],
                })
                .then((result) => {
                    console.log(result.data)
                })
                .catch(function(error) {
                    console.log("senserver error")
                    console.log(error)
                });
            } else {
                currentObj.axios.put('https://www.bobview.org:8080/api/restrating/'+currentObj.restaurantRating['rest_rating_id']+'/', {
                    rating:currentObj.restaurantRating['rating'],
                    desc:currentObj.restaurantRating['desc'],
                })
                .then((result) => {
                    console.log(result.data)
                })
                .catch(function(error) {
                    console.log("senserver error")
                    console.log(error)
                });
            }
            this.rateSuccess = true
            
            setTimeout(function() {
                    let payload = {
                    status:false,
                }
                currentObj.$emit('toggle',payload)
                
            }, 1000)
            
        },
        close() {
            let payload = {
                status:false,
            }
            this.$emit('toggle',payload)
        },
        initMenus(){
            this.progressSuccess = false
            this.restaurantRating = {}
            let currentObj = this
            let restaurant=''
            currentObj.axios.get('https://www.bobview.org:8080/api/userorder/'+this.order_id+'/')
                .then((result) => {
                    console.log(result.data)
                    restaurant=result.data[0]['restaurant']
                })
                .catch(function(error) {
                    console.log("senserver error")
                    console.log(error)
                });
            currentObj.axios.get('https://www.bobview.org:8080/api/ordercontents/0/?order_id='+this.order_id)
                .then((result) => {
                    console.log(result.data)
                    this.ordermenus=result.data
                    //console.log(this.ordermenus)
                })
                .catch(function(error) {
                    console.log("senserver error")
                    console.log(error)
                });
            setTimeout(() => {
                let username=currentObj.$store.getters.GetUserdata['username']
                let menu_id = 0
                currentObj.axios
                    .get(restaurant)
                    .then((result) => {    
                        //console.log("restaurant info data")
                        console.log(result.data)
                        currentObj.restaurantRating = result.data[0]
                    })
                    .catch(function(error) {
                        console.log("senserver error")
                        console.log(error)
                    }); 
                currentObj.axios
                    .get('https://www.bobview.org:8080/api/restrating/0/?restaurant_name='+restaurant.split('/')[5]+'&username='+username)
                    .then((result) => {
                        //console.log("rest rating data")
                        console.log(result.data)
                        if(result.data[0]['rating'] != undefined){
                            currentObj.restaurantRating['rating'] = result.data[0]['rating']
                            currentObj.restaurantRating['desc'] = result.data[0]['desc']
                            currentObj.restaurantRating['rest_rating_id'] = result.data[0]['rest_rating_id']
                        }
                    })
                    .catch(function(error) {
                        console.log("senserver error")
                        console.log(error)
                    }); 

                for(let [index] in currentObj.ordermenus){
                    menu_id = currentObj.ordermenus[index]['menu'].split('/')[5]
                    currentObj.axios.get(currentObj.ordermenus[index]['menu'])
                        .then((result) => {
                            currentObj.ordermenus[index]['menu'] = result.data[0]
                        })
                        .catch(function(error) {
                            console.log("senserver error")
                            console.log(error)
                        }); 

                    currentObj.axios
                    .get('https://www.bobview.org:8080/api/menurating/0/?username='+username+'&menu_id='+menu_id)
                    .then((result) => {
                        //console.log("menu rating data")
                        console.log(result.data)
                        currentObj.ordermenus[index]['menu_rating'] = result.data[0]
                        if(currentObj.ordermenus[index]['menu_rating'] == undefined){
                            currentObj.ordermenus[index]['rated']=false
                            currentObj.ordermenus[index]['menu_rating'] = {
                                desc:'',
                                rating:0
                            }
                        }
                        else{
                            currentObj.ordermenus[index]['rated']=true
                        }
                    })
                    .catch(function(error) {
                        console.log("senserver error")
                        console.log(error)
                    }); 
                }
            }, 1000)
            setTimeout(() => {
                //console.log("ordermenus")
                //console.log(currentObj.ordermenus)
                //console.log("restaurant rating")
                //console.log(currentObj.restaurantRating)
                currentObj.progressSuccess = true
            }, 2000)
        },
        rateMenu(item){
            //console.log(item)
            this.editedItem = {
                menu_name : item['menu']['menu_name'],
                menu_image : item['menu']['menu_image'],
                desc : item['menu_rating']['desc'],
                menu_rating : item['menu_rating']['rating']
            }
            this.editedIndex = this.ordermenus.indexOf(item)
            this.dialog=true
        },
        saveItem() {
            
            //console.log(this.editedItem)
            if(this.editedItem['menu_rating']>0){
                this.ordermenus[this.editedIndex]['rated'] = true
            }
            this.ordermenus[this.editedIndex]['menu_rating']['desc'] = this.editedItem['desc']
            this.ordermenus[this.editedIndex]['menu_rating']['rating'] = this.editedItem['menu_rating']

            this.editedIndex=-1
            this.editedItem = this.defaultItem
            this.dialog = false
            
        },
        closeItem() {
            //console.log("close")
            this.dialog = false
        }
    },

}
</script>

<style lang="scss" scoped>

</style>