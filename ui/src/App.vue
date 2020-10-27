<template>
  <v-app>
    <v-app-bar app color="primary" dark>
      <div class="d-flex align-center">
        <v-img
          alt="IBM Cloud Logo"
          class="shrink mr-2"
          contain
          src="./assets/cloud.png"
          transition="scale-transition"
          width="40"
        />
      </div>
      <v-toolbar-title
        >Vaccine Order Service Simulator - v 0.1.0
      </v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn to="/" text>
        <v-icon left>mdi-home</v-icon>
        Home
      </v-btn>
      <v-btn to="/Orders" text>
        <v-icon left>mdi-store</v-icon>
        Orders
      </v-btn>
      <v-btn
        href="https://github.com/ibm-cloud-architecture/vaccine-order-mgr"
        target="_blank"
        text
      >
        <span class="mr-2">Git</span>
        <v-icon>mdi-git</v-icon>
      </v-btn>
    </v-app-bar>
    <v-content>
      <v-container>
        <router-view />
      </v-container>
    </v-content>
  </v-app>
</template>

<script>
export default {
  name: "App",
  data: () => ({
    //
  }),
};
</script>
<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#nav {
  padding: 30px;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}
</style>
