<template>
    <v-app>
        <v-content>
            <v-btn @click="getdata">getdata</v-btn>
            <p>testing</p>
            <p>{{testing}}</p>
        </v-content>
    </v-app>
</template>

<script>
export default {
    name :'user_info',
    daata() {
        return {
            testing : null,
            hello :'sdsd'
        }
    },
    mounted() {

    },
    methods: {
        getdata(){
        this.axios.get('http://localhost:8000/api/menuinfo/bobview222/')
            .then((result) => {
                console.log("get response : " + result.data)
                this.testing = result.data
            })
            .catch(function(error) {
                console.log("senserver error")
                console.log(error)
            });
        },
        postdata(){
        this.axios
            .post('http://127.0.0.1:8000/api/mymenu/p', {
                restaurant_name: 'bobview',
                menu_name : 'pizza',
                menu_price: 20000,
                menu_desc:'pizza',
                menu_image : null
            })
            .then(function(response) {
                console.log(response.data)
            })
            .catch(function(error) {
                console.log("senserver error")
                console.log(error)
            });
        }
    },
}
</script>

<style lang="scss" scoped>

</style>