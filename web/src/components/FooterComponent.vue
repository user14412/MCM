<template>
  <footer class="footer mt-auto py-3" style="margin-top: 56px;">
    <div class="container">
      <div class="row">
        <div class="col-md-4 text-center text-md-start">
          <span class="text-muted">京ICP备12345678号</span>
        </div> 
        <div class="col-md-4 text-center">
          <span class="text-muted">联系我们：zanyan2004@163.com</span>
        </div>
        <div class="col-md-4 text-center text-md-end">
          <span class="text-muted">地址：哈尔滨市南岗区南通大街145号哈尔滨工程大学</span>
        </div>
      </div>
    </div>
  </footer>
</template>

<script>
export default {
  name: 'FooterComponent',
};
</script>

<style scoped>
.footer {
  position: relative;
  bottom: 0;
  width: 100%;
  /* 与 Navbar 背景色一致 */
  /* background-color: #f8f9fa; */
  background-color: transparent;
  /* 添加顶部边框 */
  /* border-top: 1px solid #e9ecef;  */
}
/* 蓝色字体 */
/* .text-muted{
  color: #0d6efd !important;
} */
</style>