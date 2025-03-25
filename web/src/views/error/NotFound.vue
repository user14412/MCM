<template>
  <div>
    <div class="error-container">
      <i class="icon iconfont icon-icon-test" style="font-size: 100px;"></i>
      <h1 class="error-title">Not Found</h1>
    </div>
  </div>  
</template>

<style scoped>
.error-container {
  display: flex;
  flex-direction: column;
  align-items: center; /* 水平居中 */
  /* justify-content: center; 垂直居中 */
  height: 100vh; /* 全屏高度（可选） */
}

.icon-icon-test {
  font-size: 64px;
  margin-bottom: 20px;
}

.error-title {
  font-size: 3.5rem;
  color: #333;
  text-align: center;
}
</style>