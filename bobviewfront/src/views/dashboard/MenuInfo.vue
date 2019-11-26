<template>
<v-container>
    <v-select
      :items="restaurants"
      label="Restaurant list"
      outlined
      v-model="selectedRestaurant"
    ></v-select>
    <v-card class="justify-space-around mx-2 my-2"
      v-if="getRestaurantData"
    >
        <v-data-table
          :headers="headers"
          :items="currentMenuInfo"
          sort-by="name"
          class="elevation-1"
        >
          <template v-slot:top>
          <v-toolbar flat color="white">
              <v-toolbar-title>Menu List</v-toolbar-title>
              <v-divider
              class="mx-4"
              inset
              vertical
              ></v-divider>
              <div class="flex-grow-1"></div>
              <v-dialog v-model="dialog" max-width="500px">
              <template v-slot:activator="{ on }">
                  <v-btn color="blue-grey lighten-2" dark class="mb-2 mx-2" @click="commitRestaurant">Preview</v-btn>
                  <v-btn color="blue-grey lighten-2" dark class="mb-2" v-on="on">New Item</v-btn>
              </template>
              <v-card>
                  <v-card-title>
                  <span class="headline">{{ formTitle }}</span>
                  </v-card-title>

                  <v-card-text>
                  <v-container>
                      <v-row>
                          <v-text-field v-model="editedItem['menu_name']" label="Menu Name"></v-text-field>
                      </v-row>
                      <v-row>
                          <v-text-field v-model="editedItem['menu_price']" label="Price (won)"></v-text-field>
                      </v-row>
                      <v-row>
                          <v-text-field v-model="editedItem['menu_desc']" label="Description"></v-text-field>
                      </v-row>
                      <v-row>
                          <v-file-input
                            label="Menu Image"
                            accept="image/*"
                            prepend-icon="mdi-camera"
                            placeholder="select image if you want to change"
                            v-model ="editedItem['menu_new_image']"
                            />
                      </v-row>
                  </v-container>
                  </v-card-text>

                  <v-card-actions>
                  <div class="flex-grow-1"></div>
                  <v-btn color="blue darken-1" text @click="close">Cancel</v-btn>
                  <v-btn color="blue darken-1" text @click="save">Save</v-btn>
                  </v-card-actions>
              </v-card>
              </v-dialog>
              
              <v-dialog v-model="dialog2" max-width="500px">
                <v-card>
                    <v-card-title>
                    <span class="headline">Image</span>
                    </v-card-title>
                    <v-img
                      :src="image_src"
                      gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
                      contain
                    >
                    </v-img>
                </v-card>
              </v-dialog>
          </v-toolbar>
          </template>
          <template v-slot:item.action="{ item }">
            <v-icon
                small
                class="mr-2"
                @click="showImage(item)"
            >mdi-camera
            </v-icon>
            <v-icon
                small
                class="mr-2"
                @click="editItem(item)"
            >mdi-pencil
            </v-icon>
            <v-icon
                small
                @click="deleteItem(item)"
            >mdi-delete
            </v-icon>
          </template>
          <template v-slot:no-data>
            <v-btn color="primary" @click="initialize">Reset</v-btn>
          </template>
      </v-data-table>
    </v-card>
  </v-container>
</template>
<script>
export default {
  data: () => ({
    dialog: false,
    dialog2: false,
    getRestaurantData:false,
    isUpdated:false,
    headers: [
      {
        text: 'Menu Name',
        align: 'left',
        value: 'menu_name',
      },
      { text: 'Price', value: 'menu_price', align: 'right' },
      { text: 'Description', value: 'menu_desc', sortable: false, align: 'right' },
      { text: 'Actions', value: 'action', sortable: false, align: 'right' },
    ],
    editedIndex: -1,
    editedItem: {
      menu_name: '',
      menu_price: 0,
      menu_desc: 0,
      menu_image: '',
      menu_new_image:null
    },
    defaultItem: {
      menu_name: '',
      menu_price: 0,
      menu_desc: 0,
      menu_image: '',
      menu_new_image:null
    },
    restaurants: [],
    selectedRestaurant:'',
    currentMenuInfo:[],
    image_src: '',
    imageUrl:''
  }),

  computed: {
    formTitle () {
      return this.editedIndex === -1 ? 'New Item' : 'Edit Item'
    },
  },

  watch: {
    dialog (val) {
      val || this.close()
    },
    selectedRestaurant: function () {
      this.getMenuInfo()
    },
    isUpdated: function() {
      this.getMenuInfo()
    }
  },

  created () {
    this.initialize()
  },

  methods: {
    initialize () {
      this.axios
      .get('http://localhost:8000/api/restaurantinfo/0/?owner='+this.$store.state.userdata['username'])
      .then((result) => {
          console.log(result.data)
          for(let [index] in result.data){
            this.restaurants.push(result.data[index]['restaurant_name'])
          }
          this.selectedRestaurant = this.restaurants[0]
          this.getMenuInfo()
          this.getRestaurantData = true
      })
      .catch(function(error) {
          console.log("senserver error")
          console.log(error)
      });
    },
    getMenuInfo() {
      this.getRestaurantData =false
      console.log("restaurants")
      console.log(this.restaurants)
      console.log("selectedRestaurant")
      console.log(this.selectedRestaurant)
      let currentObj = this
      currentObj.getRestaurantData = false
      this.axios
          .get('http://localhost:8000/api/menuinfo/0/?restaurant_name='+this.selectedRestaurant)
          .then((result) => {
              console.log("menuinfo data")
              console.log(result.data)
              this.currentMenuInfo = result.data
              currentObj.getRestaurantData = true
          })
          .catch(function(error) {
              console.log("senserver error")
              console.log(error)
          });
    },

    showImage (item) {
      this.editedIndex = this.currentMenuInfo.indexOf(item)
      this.image_src = this.currentMenuInfo[this.editedIndex]['menu_image']
      this.dialog2 = true
      this.editedItem = this.defaultItem
      this.editedIndex = -1
    },
    editItem (item) {
      this.editedIndex = this.currentMenuInfo.indexOf(item)
      this.editedItem = this.currentMenuInfo[this.editedIndex]
      this.dialog = true
    },
    deleteItem (item) {
      let index = this.currentMenuInfo.indexOf(item)
      let currentObj = this
      this.editedItem = this.currentMenuInfo[index]
      confirm('Are you sure you want to delete this item?') && 
      this.axios
        .delete('http://localhost:8000/api/menuinfo/'+this.editedItem['menu_id']+'/')
        .then(function(response) {
            console.log("delete complete")
            console.log(response.data)
            currentObj.isUpdated = !currentObj.isUpdated
            currentObj.editedItem = currentObj.defaultItem
            currentObj.editedIndex = -1
        })
        .catch(function(error) {
            console.log("senserver error")
            console.log(error)
        });
      
    },

    close () {
      this.dialog = false
      setTimeout(() => {
        this.editedItem = Object.assign({}, this.defaultItem)
        this.editedIndex = -1
      }, 300)
    },

    save () {
      console.log("editedItem saved")
      console.log(this.editedItem)
      let currentObj = this
      let config = { headers: { 'Content-Type': 'multipart/formed-data' } }
      if (this.editedIndex > -1) {
        console.log("modified updated")
        console.log(this.editedItem)
        this.editedItem['menu_image'] = ''
        if(this.editedItem['menu_new_image'] != null){
          let frm = new FormData()
          frm.append('image', this.editedItem['menu_new_image'])
          this.axios
            .post('http://localhost:8000/api/upload/image/',frm,config)
            .then((response) => {
              console.log(response.data)
              this.editedItem['menu_image'] = response.data['url'].split('/')[5]
              console.log("response image url : " + this.editedItem['menu_image'])
            })
            .catch((error) => {
                console.log("senserver error")
                console.log(error)
            })
        }
        setTimeout(function(){
          currentObj.axios
            .put('http://localhost:8000/api/menuinfo/'+currentObj.currentMenuInfo[currentObj.editedIndex]['menu_id']+'/', {
              menu_name: currentObj.editedItem['menu_name'],
              menu_price: currentObj.editedItem['menu_price'],
              menu_desc: currentObj.editedItem['menu_desc'],
              menu_image: currentObj.editedItem['menu_image']
            })
            .then(function(response) {
              console.log("save complete")
              console.log(response.data)
              currentObj.isUpdated = !currentObj.isUpdated
              currentObj.close()
            })
            .catch(function(error) {
              console.log("senserver error")
              console.log(error)
            });
        }, 1000);
      } else {
        console.log("create new menu")
        console.log(this.editedItem)
        this.editedItem['menu_image']=''
        if(this.editedItem['menu_new_image'] != ""){
          let frm = new FormData()
          frm.append('image', this.editedItem['menu_new_image'])
          this.axios
            .post('http://localhost:8000/api/upload/image/',frm,config)
            .then((response) => {
              console.log(response.data)
              this.editedItem['menu_image'] = response.data['url'].split('/')[5]
              console.log("response image url : " + this.editedItem['menu_image'])
            })
            .catch((error) => {
                console.log("senserver error")
                console.log(error)
            })
        }
        setTimeout(function(){
          currentObj.axios.post('http://localhost:8000/api/menuinfo/', {
              restaurant_name:currentObj.selectedRestaurant,
              menu_name: currentObj.editedItem['menu_name'],
              menu_price: currentObj.editedItem['menu_price'],
              menu_desc: currentObj.editedItem['menu_desc'],
              menu_image: currentObj.editedItem['menu_image']
            })
            .then(function(response) {
                console.log(response.data)
                currentObj.isUpdated = !currentObj.isUpdated
                currentObj.close()
                currentObj.editedItem = currentObj.defaultItem
                currentObj.editedIndex = -1
            })
            .catch(function(error) {
                console.log("senserver error")
                console.log(error)
            });
        }, 1000);
      }
    },
    commitRestaurant() {
      this.$store.commit('SetOrderInfo',{
        restaurant_name : this.selectedRestaurant,
        table_number :0
      })
      this.$router.push("/preview")
    }
  },
}
</script>

<style lang="scss" scoped>

</style>