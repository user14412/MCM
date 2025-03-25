<!-- 自适应大小的框 -->
<template>
  <div class="container content-field">
    <div class="card">
      <div class="card-body">
        <slot></slot>
      </div>
    </div>
  </div>
</template>

<script>

</script>

<style scoped>
div.content-field{
  margin-top: 20px;
}
/* div.content-field {
  margin-top: 20px;
  max-width: 2000px; 
  width: 100%;
  padding: 0 5%;     
}

@media (min-width: 1400px) {
  .content-field > .container {
    max-width: none;
  }
} */
</style>
