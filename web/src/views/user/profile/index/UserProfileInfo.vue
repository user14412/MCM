<!-- 个人中心 - 个人资料子组件 -->
<template>
  <div class="profile-container-fluid">
    <div class="row justify-content-center"> <!-- 新增居中布局 -->
      <div class="col-12 d-flex flex-column align-items-center"> <!-- 改为垂直布局 -->
        <!-- 头像容器 -->
        <div class="avatar-container-fluid">
          <img 
            :src="user.photo" 
            alt="个人头像"
            class="profile-avatar"
          >
        </div>

        <!-- 分割线 -->
        <div class="divider-line my-2"></div>

        <!-- 用户名区域 -->
        <div class="username text-center-fluid">{{ user.username }}</div>

        <!-- 分割线 -->
        <div class="divider-line my-2"></div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
    props:{
        user: {
            type: Object,
            required: true,
        }
    },
}
</script>

<style scoped>
/* .profile-container {
  padding: 2rem !important;
} */

.avatar-container {
  width: 240px;
  height: 240px;
  margin-top: 10px;
  margin-bottom: 0.5rem;  /* 增加头像底部间距 */
}

.profile-avatar {
  width: 100%;
  height: 100%;
  border-radius: 12px; /* 调整圆角程度[1,2](@ref) */
  object-fit: cover;   /* 保持图片比例[4,6](@ref) */
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1); /* 添加柔和阴影 */
}

.divider-line {
  width: 90%;  /* 控制分割线长度 */
  height: 1px;
  background-color: #eee;
  margin: rem auto;  /* 垂直间距控制 */
}

.username {
  font-weight: bold;
  font-size: 1.25rem;
  margin-top: 0rem;
}

/* 响应式调整 */
@media (max-width: 576px) {
  .avatar-container {
    width: 100px;
    height: 100px;
  }
  
  .divider-line {
    width: 50%;
    margin: 0.8rem auto;
  }
  
  .username {
    font-size: 1.3rem;
  }
}
</style>