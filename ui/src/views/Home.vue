<template>
  <div>
    <h3>Welcome to the {{ title }} {{ version }}</h3>
    <div class="outer">
      
      <div class="inner">
      <h4 style="line-height: 50px;">Demonstration script</h4>
      <p>
        This application is for demonstration purpose to create Vaccine orders
        for a governement agencies.
      </p>
      <ul>
        <li>
          Go to the orders menu to see the list of current order or create new
          order
        </li>
        <li>Using the new order button add a new vaccine request.</li>
      </ul>
      </div>
    </div>
  </div>
</template>
<style scoped>
.outer {
  width: 100%;
  margin: 0 auto;
  line-height: 30px;
}

.inner {
  text-align: left;
  margin-left: 50px;
}
</style>

<script>
export default {
  name: "Home",
  data: () => { 
      return {
        version: process.env.VUE_APP_VERSION, 
        title: process.env.VUE_APP_TITLE 
      }
  }
};
</script>
