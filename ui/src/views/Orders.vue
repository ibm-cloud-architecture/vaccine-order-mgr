<template>
  <v-row>
    <v-col sm="8" offset-sm="2">
      <v-data-table
        :headers="headers"
        :items="orders"
        sort-by="id"
        class="elevation-1"
      >
        <template v-slot:top>
          <v-toolbar flat color="white">
            <v-toolbar-title>Orders</v-toolbar-title>
            <v-divider class="mx-4" inset vertical></v-divider>
            <v-spacer></v-spacer>
            <v-dialog v-model="dialog" max-width="1200px">
              <template v-slot:activator="{ on }">
                <v-btn color="primary" dark class="mb-2" v-on="on"
                  >New Order</v-btn
                >
              </template>
              <v-card>
                <v-card-title>
                  <span class="headline">{{ formTitle }}</span>
                </v-card-title>
                <v-card-text>
                  <v-container>
                    <v-row>
                      <v-col cols="12" sm="6" md="4">
                        <v-text-field
                          v-model="editedItem.askingOrganization"
                          label="Organization"
                          required
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="6" md="4">
                        <v-text-field
                          v-model="editedItem.deliveryLocation"
                          label="Delivery Location"
                          required
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="6" md="4">
                        <v-menu
                          ref="menu"
                          v-model="menu"
                          :close-on-content-click="false"
                          :return-value.sync="editedItem.deliveryDate"
                          transition="scale-transition"
                          offset-y
                          min-width="290px"
                        >
                          <template v-slot:activator="{ on, attrs }">
                            <v-text-field
                              v-model="editedItem.deliveryDate"
                              prepend-icon="mdi-calendar"
                              readonly
                              v-bind="attrs"
                              v-on="on"
                            ></v-text-field>
                          </template>
                          <v-date-picker
                            v-model="editedItem.deliveryDate"
                            label="Delivery Location"
                            required
                          >
                            <v-spacer></v-spacer>
                            <v-btn text color="primary" @click="menu = false">
                              Cancel
                            </v-btn>
                            <v-btn
                              text
                              color="primary"
                              @click="$refs.menu.save(editedItem.deliveryDate)"
                            >
                              OK
                            </v-btn>
                          </v-date-picker>
                        </v-menu>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col cols="12" sm="6" md="4">
                        <v-text-field
                          v-model="editedItem.quantity"
                          label="Quantity"
                          required
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="6" md="4">
                        <v-slider
                          label="Priority"
                          v-model="editedItem.priority"
                          max="5"
                          min="1"
                          thumb-label="always"
                        ></v-slider>
                      </v-col>
                      <v-col cols="12" sm="6" md="4">
                        <v-select
                          :items="vaccineTypes"
                          v-model="editedItem.type"
                          label="Vaccine Type"
                        ></v-select>
                      </v-col>
                    </v-row>
                  </v-container>
                </v-card-text>
                <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn color="blue darken-1" text @click="close"
                    >Cancel</v-btn
                  >
                  <v-btn color="blue darken-1" text @click="save">Save</v-btn>
                </v-card-actions>
              </v-card>
            </v-dialog>
          </v-toolbar>
        </template>
        <template v-slot:item.actions="{ item }">
          <v-icon small class="mr-2" @click="editItem(item)">
            mdi-pencil
          </v-icon>
          <v-icon small @click="deleteItem(item)"> mdi-delete </v-icon>
        </template>
      </v-data-table>
    </v-col>
  </v-row>
</template>

<script>
import axios from "axios";

let backendURL = "/api/v1/orders";

export default {
  name: "Orders",
  data: () => ({
    orders: [],
    vaccineTypes: ["COVID-19", "H1N1"],
    editedIndex: -1,
    editedItem: {
      askingOrganization: "",
      deliveryDate: new Date().toISOString().substr(0, 10),
      deliveryLocation: "",
      priority: 2,
      quantity: 150,
      type: "COVID-19",
    },
    defaultItem: {
      id: -1,
      askingOrganization: "",
      deliveryDate: new Date().toISOString().substr(0, 10),
      deliveryLocation: "",
      priority: 2,
      quantity: 150,
      type: "COVID-19",
    },
    dialog: false,
    menu: false,
    headers: [
      { text: "Organization", value: "askingOrganization", sortable: true },
      { text: "Location", value: "deliveryLocation", sortable: true },
      { text: "Target Date", value: "deliveryDate", sortable: true },
      { text: "Priority", value: "priority", sortable: true },
      { text: "Quantity", value: "quantity", sortable: true },
      { text: "Status", value: "status" },
      { text: "Actions", value: "actions", sortable: false },
    ],
  }),
  computed: {
    formTitle() {
      return this.editedIndex === -1 ? "New Order" : "Edit Order";
    },
  },
  watch: {
    dialog(val) {
      val || this.close();
    },
  },
  created() {
    this.initialize();
  },
  methods: {
    initialize() {
      axios.get(backendURL).then((resp) => (this.orders = resp.data));
    },
    editItem(item) {
      this.editedIndex = this.orders.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialog = true;
    },
    deleteItem(item) {
      console.log(item);
    },
    close() {
      this.dialog = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      });
    },
    save() {
      if (this.editedIndex > -1) {
        axios
          .put(backendURL + "/" + this.editedItem.id, this.editedItem)
          .then((resp) => (this.editedItem = resp.data));
        Object.assign(this.orders[this.editedIndex], this.editedItem);
      } else {
        axios
          .post(backendURL, this.editedItem)
          .then((resp) => (this.editedItem = resp.data));
        this.orders.push(this.editedItem);
      }
      this.close();
    },
  },
};
</script>