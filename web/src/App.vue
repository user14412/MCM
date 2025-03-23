<template>
  <NavBar></NavBar>
  <router-view></router-view>
</template>

<script>

import NavBar from "@/components/NavBar.vue";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap"
// import $ from 'jquery'
// import { useStore } from "vuex";

export default {
  components:{
    NavBar
  },
  setup() {
    // const store = useStore();
    // store.commit("logout")
  }
}
</script>

<style>
body {
  background-image: url("./assets/images/flower.jpg");
  background-size: cover;
}
</style>