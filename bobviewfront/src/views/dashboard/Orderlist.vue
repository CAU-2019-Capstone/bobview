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
    <v-content>
      <span>현재 식사중인 식당</span>
    </v-content>
    <v-content
    class="justify-space-around"
    v-for="order in activeOrders"
    :key="order.user_order_id"
    >
      <review v-bind:order_id="order.user_order_id"></review>
    </v-content>
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
  },
  data: () => ({
    dialog:false,
    getOrderData:false,
    newWindow:false,
    activeOrders:[],
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
    if(this.$store.getters.GetOrderId > 0){
      this.axios.post('http://localhost:8000/api/order/change/',{
        username:this.$store.getters.GetUserdata['username'],
        order_id:this.$store.getters.GetOrderId
      })
      .then(function(response){
        console.log(response.data)
        if(response.data['message'] == 'success'){
          this.$store.commit('setOrderId',{
            orderId : 0,
          })
          this.initialize()
        }
      })
      .catch(function(error){
        console.log(error)
      });
    }
    let currentObj = this
    setTimeout(function(){
      currentObj.initialize()
      currentObj.getOrderData = true
    },1000)
  },
  methods: {
    close() {
      this.dialog=false
    },
    initialize () {
      this.getOrderData=false
      this.axios
      .get('http://127.0.0.1:8000/api/userorder/0/?username='+this.$store.state.userdata['username'])
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
    }
  },
}
</script>

<style lang="scss" scoped>

</style>