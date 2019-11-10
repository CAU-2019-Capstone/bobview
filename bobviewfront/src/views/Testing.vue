<template>
    <v-container>
        <v-content>
            
        </v-content>
    </v-container>
</template>

<script>
export default {
    components: {
    },
    mounted(){
        this.axios.get('http://localhost:8000/api/first/community/')
        .then((result)=>{
            console.log(result.data)
        })
        .catch(function(error){
            console.log(error)
            console.log("senserver error")
        });
    }
}
</script>

<style lang="scss" scoped>

</style>