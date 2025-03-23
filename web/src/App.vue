<template>
  <div id="app" class="d-flex flex-column min-vh-100">
    <NavBar />
    <main class="flex-grow-1">
      <router-view />
    </main>
    <FooterComponent />
  </div>
</template>

<script>
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap"
import NavBar from "./components/NavBar.vue";
import FooterComponent from './components/FooterComponent.vue';


export default {
  name: 'App',
  components: {
    NavBar,
    FooterComponent,
  },
};
</script>

<style>
body {
  background-image: url("./assets/images/flower.jpg");
  background-size: cover;
}
</style>