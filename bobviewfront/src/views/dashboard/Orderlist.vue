<template>
  <v-container>
    <v-card class="justify-space-around mx-2 mb-10"
      v-if="getOrderData"
    >
        <v-data-table
          :headers="headers"
          :items="orderLists"
          sort-by="user_order_id"
          sort-desc
          class="elevation-1"
        >
          <template v-slot:top>
          <v-toolbar flat color="white">
              <v-toolbar-title>Order List</v-toolbar-title>
              <v-divider
              class="mx-4"
              inset
              vertical
              ></v-divider>
              <v-spacer/>
              <v-dialog v-model="dialog2" max-width="500px">
              <template v-slot:activator="{ on }">
                  <v-btn color="blue-grey lighten-2" dark class="mb-2 mx-2" @click="addOrder">Add Order</v-btn>
              </template>
              <v-card>
                  <v-card-title>
                    <span class="headline">Add Order by Order ID</span>
                  </v-card-title>
                  <v-card-text>
                    <v-container>
                        <v-row>
                            <v-text-field v-model="order_id" label="Order Id"></v-text-field>
                        </v-row>
                    </v-container>
                  </v-card-text>

                  <v-card-actions>
                  <div class="flex-grow-1"></div>
                  <v-btn color="blue darken-1" text @click="close">Cancel</v-btn>
                  <v-btn color="blue darken-1" text @click="save(order_id)">Save</v-btn>
                  </v-card-actions>
              </v-card>
              </v-dialog>
          </v-toolbar>
          </template>
          <template v-slot:item.action="{ item }">
            <v-icon
                small
                class="mr-2"
                @click="manageItem(item)"
            >mdi-circle-edit-outline
            </v-icon>
          </template>
          <template v-slot:no-data>
            <v-btn color="primary" @click="initialize">Reset</v-btn>
          </template>
      </v-data-table>
    </v-card>
    <v-card class="elevation-2 justify-space-around my-10">
      <v-card-title primary-title class="justify-space-around">
        Now you are eating here...
      </v-card-title>
      <v-row
      class="justify-space-around"
      v-for="order in activeOrders"
      :key="order.user_order_id"
      >
        <review v-bind:order_id="order.user_order_id"></review>
      </v-row>
    </v-card>
    
    <v-dialog
    v-model="dialog"
    max-width="500"
    >
      <review 
      v-bind:order_id="current_order_id" 
      v-bind:updated="newWindow"
      @toggle="toggleDialog"></review>
    </v-dialog>
  </v-container>
</template>
<script>
import Review from "@/components/Review"
export default {
  components:{
    Review
  },
  watch: {
    dialog (val) {
        val || this.close()
        this.newWindow=!this.newWindow
    },
    dialog2 (val) {
        val || this.close()
        this.newWindow=!this.newWindow
    },
  },
  data: () => ({
    dialog:false,
    dialog2: false,
    getOrderData:false,
    newWindow:false,
    activeOrders:[],
    order_id : 0,
    headers: [
      {
        text: 'Order ID',
        align: 'left',
        value: 'user_order_id',
      },
      { text: 'Restaurant', value: 'restaurant', align: 'right' },
      { text: 'Time', value: 'order_time', sortable: false, align: 'right', divider:true },
      { text: 'Rating', value: 'action', sortable: false, align: 'right' },
    ],
    current_order_id:0,
    orderLists:[],
  }),

  mounted () {
    console.log("order list mounted")
    console.log("current order id : " +this.$store.getters.GetOrderId)
    this.save(this.$store.getters.GetOrderId)
  },
  methods: {
    close() {
      this.dialog=false
      this.dialog2=false
      this.order_id=0
    },
    initialize () {
      this.getOrderData=false
      this.axios
      .get('localhost:8000/api/userorder/0/?username='+this.$store.state.userdata['username'])
      .then((result) => {
          console.log(result.data)
          this.orderLists = result.data
          for(let [index] in this.orderLists){
            this.orderLists[index]['restaurant'] = this.orderLists[index]['restaurant'].split('/')[5]
          }
          
          console.log(result.data) 
          this.getOrderData=true
      })
      .catch(function(error) {
          console.log("senserver error")
          console.log(error)
      });
      this.axios
        .get('http://localhost:8000/api/userorder/1/?username='+this.$store.getters.GetUserdata['username']+'&is_active=true')
        .then((result)=> {
          console.log(result.data)
          this.activeOrders = result.data
          
        })
        .catch(function(error) {
          console.log("senserver error")
          console.log(error)
        });
    },
    manageItem(item) {
      this.current_order_id = item.user_order_id
      this.dialog=true
      console.log(item)
    },
    toggleDialog(payload) {
      console.log("emit dialog : " + payload)
      this.dialog = payload['status']
    },
    addOrder(){
      this.order_id = 0
      this.dialog2 = true
    },
    save(id) {
      let currentObj = this
      if(id > 0){
        currentObj.axios.post('http://localhost:8000/api/order/change/',{
          username:this.$store.getters.GetUserdata['username'],
          order_id:id
        })
        .then(function(response){
          console.log(response.data)
          if(response.data['message'] == 'success'){
            currentObj.$store.commit('setOrderId',{
              orderId : 0,
            })
            currentObj.initialize()
          }
        })
        .catch(function(error){
          console.log(error)
        });
      }
      setTimeout(function(){
        currentObj.initialize()
        currentObj.getOrderData = true
      },1000)

    }
  },
}
</script>

<style lang="scss" scoped>

</style>