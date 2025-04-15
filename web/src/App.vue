<template>
  <div id="app" class="d-flex flex-column min-vh-100">
    <NavBar />
    <main class="flex-grow-1" style="padding-top: 56px;">
      <router-view />
    </main>
    <FooterComponent />
    
  </div>
</template>

<script>
import NavBar from "./components/NavBar.vue";
import FooterComponent from './components/FooterComponent.vue';
// import "bootstrap/dist/css/bootstrap.min.css";
// import "bootstrap/dist/js/bootstrap";

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
  /* background-image: url("./assets/images/sp_contents.jpg"); */
  background-image: url("./assets/images/sp_contents.jpg");
  background-size: cover;
  background-attachment: fixed;
    /* 新增这行 */
    min-height: 100vh;
    /* 确保body始终撑满视口 */
}
</style>