<template>
  <v-row>
    <v-row>
      <p align="left">
        This table presents the current transportation defined in the simulator.
        Push 'submit' button to send those records to Kafka. The Transit
        attribute represents the cost to go from one location to the other.
      </p>
      <v-col>
        <v-btn color="primary" dark class="mb-2" @click="submit">Submit</v-btn>
      </v-col>
    </v-row>
    <v-row>
      <v-col sm="" offset-sm="2">
        <v-data-table
          :headers="headers"
          :items="transports"
          sort-by="lane_id"
          class="elevation-1"
        >
        </v-data-table>
      </v-col>
    </v-row>
  </v-row>
</template>
<script>
import axios from "axios";

export default {
  data: () => ({
    transports: [],
    backendURL: process.env.VUE_APP_TRANSPORTATION_URL,
    headers: [
      { text: "id", value: "lane_id", sortable: true },
      { text: "From", value: "from_loc" },
      { text: "To", value: "to_loc" },
      { text: "Transit", value: "transit_time", sortable: true },
      { text: "Cost", value: "fixed_cost", sortable: true },
      { text: "Refrigerator Cost", value: "reefer_cost" },
    ],
  }),
  created() {
    this.initialize();
  },
  methods: {
    initialize() {
      axios.get(this.backendURL).then((resp) => (this.transports = resp.data));
    },
    submit() {
      console.log("Send records to kafka");
      axios.post(this.backendURL + "/start");
    },
  },
};
</script>
<style>
p {
  text-indent: 10px;
}
</style>