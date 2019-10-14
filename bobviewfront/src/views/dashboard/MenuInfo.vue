<template>
    <v-card class="justify-space-around mx-2 my-2">
        <v-data-table
        :headers="headers"
        :items="desserts"
        sort-by="price"
        class="elevation-1"
    >
        <template v-slot:top>
        <v-toolbar flat color="white">
            <v-toolbar-title>Menu List</v-toolbar-title>
            <v-divider
            class="mx-4"
            inset
            vertical
            ></v-divider>
            <div class="flex-grow-1"></div>
            <v-dialog v-model="dialog" max-width="500px">
            <template v-slot:activator="{ on }">
                <v-btn color="blue-grey lighten-2" dark class="mb-2" to="/preview">Preview</v-btn>
                <v-btn color="blue-grey lighten-2" dark class="mb-2" v-on="on">New Item</v-btn>
            </template>
            <v-card>
                <v-card-title>
                <span class="headline">{{ formTitle }}</span>
                </v-card-title>

                <v-card-text>
                <v-container>
                    <v-row>
                        <v-text-field v-model="editedItem.name" label="Menu Name"></v-text-field>
                    </v-row>
                    <v-row>
                        <v-text-field v-model="editedItem.price" label="Price (won)"></v-text-field>
                    </v-row>
                    <v-row>
                        <v-text-field v-model="editedItem.description" label="Description"></v-text-field>
                    </v-row>
                    <v-row>
                        <v-text-field v-model="editedItem.image" label="Iamge"></v-text-field>
                    </v-row>
                </v-container>
                </v-card-text>

                <v-card-actions>
                <div class="flex-grow-1"></div>
                <v-btn color="blue darken-1" text @click="close">Cancel</v-btn>
                <v-btn color="blue darken-1" text @click="save">Save</v-btn>
                </v-card-actions>
            </v-card>
            </v-dialog>
        </v-toolbar>
        </template>
        <template v-slot:item.action="{ item }">
        <v-icon
            small
            class="mr-2"
            @click="editItem(item)"
        >
            mdi-pencil
        </v-icon>
        <v-icon
            small
            @click="deleteItem(item)"
        >
            mdi-delete
        </v-icon>
        </template>
        <template v-slot:no-data>
        <v-btn color="primary" @click="initialize">Reset</v-btn>
        </template>
    </v-data-table>
    </v-card>
    
</template>
<script>
  export default {
    data: () => ({
      dialog: false,
      headers: [
        {
          text: 'Menu name',
          align: 'left',
          sortable: false,
          value: 'name',
        },
        { text: 'Price', value: 'price' },
        { text: 'Description', value: 'description', sortable: false },
        { text: 'Image', value: 'image', sortable: false},
        { text: 'Actions', value: 'action', sortable: false, align: 'right' },
      ],
      desserts: [],
      editedIndex: -1,
      editedItem: {
        name: '',
        price: 0,
        description: 0,
        image: 0,
      },
      defaultItem: {
        name: '',
        price: 0,
        description: 0,
        image: 0,
      },
    }),

    computed: {
      formTitle () {
        return this.editedIndex === -1 ? 'New Item' : 'Edit Item'
      },
    },

    watch: {
      dialog (val) {
        val || this.close()
      },
    },

    created () {
      this.initialize()
    },

    methods: {
      initialize () {
        this.desserts = [
          {
            name: 'Frozen Yogurt',
            price: '15,900',
            description: 6.0,
            image: 24,
          },
          {
            name: 'Ice cream sandwich',
            price: '23,700',
            description: 9.0,
            image: 37,
          },
          {
            name: 'Eclair',
            price: '26,200',
            description: 16.0,
            image: 23,
          },
          {
            name: 'Cupcake',
            price: '30,500',
            description: 3.7,
            image: 67,
          },
          {
            name: 'Gingerbread',
            price: '35,600',
            description: 16.0,
            image: 49,
          },
          {
            name: 'Jelly bean',
            price: '37,500',
            description: 0.0,
            image: 94,
          },
          {
            name: 'Lollipop',
            price: '39,200',
            description: 0.2,
            image: 98,
          },
          {
            name: 'Honeycomb',
            price: '40,800',
            description: 3.2,
            image: 87,
          },
          {
            name: 'Donut',
            price: '45,200',
            description: 25.0,
            image: 51,
          },
          {
            name: 'KitKat',
            price: '51,800',
            description: 26.0,
            image: 65,
          },
        ]
      },

      editItem (item) {
        this.editedIndex = this.desserts.indexOf(item)
        this.editedItem = Object.assign({}, item)
        this.dialog = true
      },

      deleteItem (item) {
        const index = this.desserts.indexOf(item)
        confirm('Are you sure you want to delete this item?') && this.desserts.splice(index, 1)
      },

      close () {
        this.dialog = false
        setTimeout(() => {
          this.editedItem = Object.assign({}, this.defaultItem)
          this.editedIndex = -1
        }, 300)
      },

      save () {
        if (this.editedIndex > -1) {
          Object.assign(this.desserts[this.editedIndex], this.editedItem)
        } else {
          this.desserts.push(this.editedItem)
        }
        this.close()
      },
    },
  }
</script>

<style lang="scss" scoped>

</style>