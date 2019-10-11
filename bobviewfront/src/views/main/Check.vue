<template>
    <v-container grid-list-xs>
        <v-card>
            <p></p>
            <p>{{ message }}</p>
            <p></p>
        </v-card>
        
    </v-container>
</template>
<script>
export default {
    name :'check',
    props: ["message"]
}
</script>

<style lang="scss" scoped>

</style>