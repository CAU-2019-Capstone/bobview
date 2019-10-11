<template>
    <v-container grid-list-xs
    fill-hegiht
    fluid>
        <v-card-text>
            <v-text-field label="search" v-model="search"></v-text-field>
        </v-card-text>
        
    </v-container>
</template>

<script>
export default {
  data() {
    return {
      search:null,
    };
  },

}
</script>

<style lang="scss" scoped>

</style>