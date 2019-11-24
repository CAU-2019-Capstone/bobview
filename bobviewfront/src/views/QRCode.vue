<template>
    <v-row>
        <v-col
        v-if="loading"
        >
            QRcodes are loading....
            <v-progress-linear :indeterminate="true"></v-progress-linear>
        </v-col>
        <v-col
        v-else
        v-for="(qrcode,index) in qrcodeList"
        :key="qrcode"
        cols="3"
        >
            <v-card>
                <v-card-title primary-title>
                    table : {{index+1}}
                </v-card-title>
                <v-img
                :src="qrcode"
                ></v-img>   
            </v-card> 
            
        </v-col>
    </v-row>
</template>

<script>
export default {
    data () {
        return {
            domain : '',
            qrcodeList:[],
            Width:100,
            Height:100
        }
    },
    watch : {
        qrcodeList : function(val){
            if(val == []){
                this.loading=true
            }
            else{
                this.loading=false
            }
        }
    },
    mounted(){
        console.log(this.$route.query.restaurant_name)
        console.log(this.$route.query.table_num)
        this.qrcodeList = []
        for(let i=1 ;i <= this.$route.query.table_num;i++){
            this.qrcodeList.push(this.makeCode(this.$route.query.restaurant_name, i+1))
        }
        
    },
    methods: {
        makeCode(res_name, table_num){
            this.domain = 'http://localhost:8080/order/main/?r='+res_name+'&t='+table_num
            let encodedDomain = encodeURIComponent(this.domain);
            let uri = 'https://chart.googleapis.com/chart?cht=qr&chs='+this.Width+'x'+this.Height+'&chl='+encodedDomain
            return uri
        }
    }
}

</script>

<style lang="scss" scoped>

</style>