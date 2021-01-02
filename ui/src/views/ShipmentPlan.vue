<template>
 <v-row>
    <v-col sm="8" offset-sm="2">
      <v-data-table
        :headers="headers"
        :items="plans"
        sort-by="planID"
        class="elevation-1"
      >
        <template v-slot:top>
          <v-toolbar flat color="white">
            <v-toolbar-title>Shipment Plans</v-toolbar-title>
          </v-toolbar>
        </template>
      </v-data-table>
    </v-col>
 </v-row>
</template>
<script>
//import axios from "axios";

let backendURL = "/api/v1/shipments/stream";

export default {
  data: () => ({
    plans: [],
    defaultPlan: {
      planID: "",
      orderID: "",
      from: "",
      departureDate: "",
      to: "",
      arrivalDate: "",
      quantity: "",
      reefers: "",
      cost: "",
    },
    headers: [
      { text: "PlanID", value: "planID", sortable: true },
      { text: "OrderID", value: "orderID", sortable: true },
      { text: "Departure Date", value: "departureDate", sortable: true },
      { text: "From", value: "from", sortable: true },
      { text: "to", value: "to", sortable: true },
      { text: "Arrival Date", value: "arrivalDate" },
      { text: "Quantity", value: "quantity", sortable: true },
      { text: "# of Reefers", value: "reefers" },
      { text: "Cost", value: "cost" },
    ],
  }),
  created() {
    this.initialize();
  },
  methods: {
    initialize() {
        let es = new EventSource(backendURL);
        es.addEventListener('message', event => {
            let data = JSON.parse(event.data);
            console.log(data)
            this.plans.push(data);
        },false);
        es.addEventListener('error', event => {
            if (event.readyState == EventSource.CLOSED) {
                console.log('Event was closed');
                console.log(EventSource);
            }
        }, false);
      // axios.get({url: backendURL,  responseType: 'stream'}).then((resp) => (this.plans = resp.data));
    }
  }
}
</script>