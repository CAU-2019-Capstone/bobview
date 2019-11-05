<template>
    <v-app>
        <v-content>
            <v-btn @click="postdata">post</v-btn>
            <v-btn @click="deletedata">delete</v-btn>
            <v-btn @click="putdata">put</v-btn>
            <p>testing</p>
            <p>{{testing}}</p>
        </v-content>
    </v-app>
</template>

<script>
export default {
    name :'testing',
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
                .post('http://localhost:8000/api/mymenu/p', {
                    restaurant_name: 'bob2',
                    menu_name : 'party',
                    menu_price: 3000000,
                    menu_desc: 'party is funny',
                    menu_image:  'http://localhost:8000/media/sample_afEc4VE.png',
                })
                .then(function(response) {
                    console.log(response.data)
                })
                .catch(function(error) {
                    console.log("senserver error")
                    console.log(error)
                });
        },
        
        putdata(){
            let menu_id = 12
            this.axios
                .put('http://localhost:8000/api/menuinfo/'+menu_id+'/', {
                    menu_name : 'pizza1000',
                    menu_price: 300,
                    menu_desc: 'put pizza1000',
                    menu_image:  'http://localhost:8000/media/sample_afEc4VE.png',
                })
                .then(function(response) {
                    console.log(response.data)
                })
                .catch(function(error) {
                    console.log("senserver error")
                    console.log(error)
                });
        },


        userinfodeletedata(){
            this.axios
                .delete('http://localhost:8000/api/userinfo/hell/?password=hell')
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