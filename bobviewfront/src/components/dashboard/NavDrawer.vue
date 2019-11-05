<template>
  <v-navigation-drawer
    id="app-drawer"
    v-model="inputValue"
    :src="image"
    app
    color="blue-grey darken-2"
    dark
    floating
    mobile-break-point="991"
    persistent
    width="260"
  >
    <template v-slot:img="attrs">
      <v-img
        v-bind="attrs"
        gradient="to top, rgba(0, 0, 0, .7), rgba(0, 0, 0, .7)"
      />
    </template>

    <v-list-item two-line>
      <v-list-item-avatar color="white">
        <v-img
          src="https://cdn.vuetifyjs.com/images/logos/v.png"
          height="34"
          contain
        />
      </v-list-item-avatar>

      <v-list-item-title class="title-sm">
        <v-btn depressed color="blue-grey darken-2" to="/">Bobview</v-btn>
      </v-list-item-title>
    </v-list-item>

    <v-divider class="mx-3 mb-3" />

    <v-list nav>
      <!-- Bug in Vuetify for first child of v-list not receiving proper border-radius -->
      <div />

      <v-list-item
        v-for="(link, i) in links"
        :key="i"
        :to="link.to"
        active-class="primary white--text"
      >
        <v-list-item-action
        >
          <v-icon>{{ link.icon }}</v-icon>
        </v-list-item-action>

        <v-list-item-title
          v-text="link.text" />
      </v-list-item>
        <v-list-item
          v-for="(link, i) in restaurant_links"
          :key="i"
          :to="link.to"
          active-class="primary white--text"
        >
          <v-list-item-action
            v-if="is_owner"
          >
            <v-icon>{{ link.icon }}</v-icon>
          </v-list-item-action>

          <v-list-item-title
            v-if="is_owner"
            v-text="link.text" />
        </v-list-item>
    </v-list>

    <template v-slot:append>
      <v-list nav>
        <v-list-item
          to="/logout"
        >
          <v-list-item-action>
            <v-icon>mdi-logout</v-icon>
          </v-list-item-action>

          <v-list-item-title class="font-weight-light">
            Logout
          </v-list-item-title>
        </v-list-item>
      </v-list>
    </template>
  </v-navigation-drawer>
</template>

<script>
// Utilities
  import {
    mapMutations,
    mapState
  } from 'vuex'

  export default {
    props: {
      opened: {
        type: Boolean,
        default: false
      }
    },
    data: () => ({
      is_owner: false,
      links: [
        {
          to: '/dashboard/user_info',
          icon: 'mdi-account',
          text: 'User Profile',
        },
        {
          to: '/dashboard/orderlist',
          icon: 'mdi-menu',
          text: 'Order List',
        },
      ],
      restaurant_links: [
        {
          to: '/dashboard/restaurant_info',
          icon: 'mdi-silverware',
          text: 'Restaurant Profile',
        },
        {
          to: '/dashboard/menu_info',
          icon: 'mdi-rice',
          text: 'Menu Profile',
        },
        {
          to: '/dashboard/restaurant_rating',
          icon: 'mdi-star',
          text: 'Restaurant Rating',
        },
        {
          to: '/dashboard/menu_rating',
          icon: 'mdi-star-outline',
          text: 'Menu Rating',
        },
      ]
    }),
    mounted() {
      console.log("dashboard navbar updated")
      console.log("navbar is owner : "+this.is_owner)
      this.initialize()
    },
    computed: {
      ...mapState('app', ['image', 'color']),
      inputValue: {
        get () {
          let currentObj = this
          return currentObj.$store.state.app.drawer
        },
        set (val) {
          let currentObj = this
          currentObj.setDrawer(val)
        },
      },
    },

    methods: {
      initialize() {
        this.is_owner = this.$store.getters.isOwner
        console.log("initialize owner is : "+ this.is_owner)
      },
      ...mapMutations('app', ['setDrawer', 'toggleDrawer']),


    }
  }
</script>
