<template>
    <p>logout...</p>
</template>

<script>
export default {
    name :'login',
    data() {
        return{

        }
    },
    mounted() {
        if(this.$store.getters.isLogined){
        console.log("logout mounted") 
        this.$store.commit('logout')
        console.log("login state : "+this.$store.getters.isLogined)
        }
        this.gotomain()
    },
    updated() { 
        if(this.$store.getters.isLogined){
        console.log("logout mounted") 
        this.$store.commit('logout')
        console.log("login state : "+this.$store.getters.isLogined)
        }
        this.gotomain()
    },
    methods: {
        gotomain(){
            console.log("goto main")
            this.$router.push("/")
        }
    },
}
</script>

<style lang="scss" scoped>

</style>