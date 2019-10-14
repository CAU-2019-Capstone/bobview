<template>
    <v-app>
        <v-container class="blue-grey lighten-2">
            <v-row>
                  <v-col 
                    v-for="menuinfo in menuinfos"
                    :key="menuinfo.name"
                    cols="12" sm="6">
                        <v-card
                            class="mx-auto"
                            max-width="400"
                        >
                            <v-img
                            class="white--text align-end"
                            height="200px"
                            src="https://cdn.vuetifyjs.com/images/cards/docks.jpg"
                            >
                            <v-card-title justify-center>{{menuinfo.name}}</v-card-title>
                            </v-img>

                            <v-card-text class="text--primary">
                            <div>{{menuinfo.description}}</div>
                            </v-card-text>

                            <v-card-actions>
                            <v-btn
                                color="orange"
                                text
                            >
                                Order
                            </v-btn>
                            </v-card-actions>
                        </v-card>
                  </v-col>
            </v-row>
        </v-container>
    </v-app>
</template>

<script>
export default {
    data() {
        return {
            menuinfos: [
                {
                    name :'pasta',
                    price: '6,200',
                    description: 'pasta is very delicious',
                    image : null
                },
                {
                    name :'pizza',
                    price: '12,200',
                    description: 'pasta is not pizza',
                    image : null
                },
                {
                    name :'hamburger',
                    price: '16,200',
                    description: 'pasta is good for hamburger',
                    image : null
                },
                {
                    name :'newyork pizza',
                    price: '6,200',
                    description: 'newyork pizza is from NewYork',
                    image : null
                },
                {
                    name :'hot dog',
                    price: '6,200',
                    description: 'hot dog is new',
                    image : null
                }
            ]
        }
    }
}
</script>

<style lang="scss" scoped>

</style>