<template>
  <nav class="navbar navbar-expand-lg bg-body-tertiary fixed-top">
    <div class="container">
      <!-- 换掉实现切换页面不刷新(其实是一个页面) -->
      <router-link class="navbar-brand" :to="{name: 'home'}">
        <img src="@/assets/images/logo4.2.png" class="nav-logo" style="max-width: 80px; object-fit: cover;"
          alt="logo4.2">
      </router-link>
      <div class="collapse navbar-collapse" id="navbarText">

        <ul class="navbar-nav me-auto mb-2 mb-lg-0">

          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle"
              :class="(route_name == 'introduction_index' || route_name == 'news_index' || route_name == 'visit_guide_index') ? 'nav-link active' : 'nav-link'" href="#" role="button"
              data-bs-toggle="dropdown" aria-expanded="false">
              本馆概况
            </a>
            <ul class="dropdown-menu">
              <li>
                <router-link :class="route_name == 'introduction_index' ? 'nav-link active':'nav-link'"
                  :to="{name: 'introduction_index'}">本馆概况</router-link>
              </li>
              <li>
                <router-link :class="route_name == 'news_index' ? 'nav-link active':'nav-link'"
                  :to="{name: 'news_index'}">新闻动态</router-link>
              </li>
              <li>
                <router-link :class="route_name == 'visit_guide_index' ? 'nav-link active':'nav-link'"
                  :to="{name: 'visit_guide_index'}">参观须知</router-link>
              </li>
            </ul>
          </li>

          <li class="nav-item">
            <router-link :class="route_name == 'panorama_index' ? 'nav-link active':'nav-link'"
              :to="{name: 'panorama_index'}">全景参观</router-link>
          </li>
          <li class="nav-item">
            <router-link :class="route_name == 'exhibit_index' ? 'nav-link active':'nav-link'"
              :to="{name: 'exhibit_index'}">馆藏展品</router-link>
          </li>
          <li class="nav-item">
            <router-link :class="route_name == 'feedback_index' ? 'nav-link active':'nav-link'"
              :to="{name: 'feedback_index'}">问卷反馈</router-link>
          </li>

        </ul>

        <ul class="navbar-nav" v-if="$store.state.user.is_login">
          <li class="nav-item dropdown">
            <div class="d-flex align-items-center dropdown-toggle-container" role="button" data-bs-toggle="dropdown"
              aria-expanded="false">
              <img :src="store.state.user.photo" class="img-fluid rounded-circle mt-2 mx-auto"
                style="max-width: 38px; height: 38px; object-fit: cover;" alt="用户头像">
              <span class="nav-link user-menu dropdown-toggle text-reset">
                {{ $store.state.user.username }}
              </span>
            </div>

            <ul class="dropdown-menu">
              <li>
                <router-link class="dropdown-item" :to="{name: 'user_profile_index'}">个人中心</router-link>
              </li>
              <li>
                <a class="dropdown-item" href="#" @click="logout">退出</a>
              </li>
            </ul>
          </li>
        </ul>

        <ul class="navbar-nav" v-else-if="!$store.state.user.pulling_info">
          <li class="nav-item">
            <router-link class="nav-link user-menu" :to="{name: 'user_account_login'}" role="button">
              登录
            </router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link user-menu" :to="{name: 'user_account_register'}" role="button">
              注册
            </router-link>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script>
import { useRoute } from 'vue-router';
import { computed } from 'vue';
import { useStore } from 'vuex';
import router from '@/router/index'

export default{
  setup(){
    const store = useStore();
    const route = useRoute();
    let route_name = computed(() => route.name)

    // 退出函数
    const logout = () => {
      store.dispatch('logout');
      router.push({name: "user_account_login"});
    }

    return{
      store,
      route_name,
      logout
    }
  }
}
</script>

<style scoped>
 /* 确保导航栏在其他内容之上 */
.navbar {
  z-index: 1060;
}
.nav-logo {
  border-radius: 0 !important;
  /* 强制覆盖其他样式 */
  border: none !important;
  /* 新增：强制移除边框 */
}
/* 新增全局导航字体控制 */
.navbar {
  font-family: "SimHei", sans-serif !important;
  font-weight: 550 !important;
  font-size: 1.1em;
}

/* 导航链接文字继承全局字体 */
.nav-link {
  font-family: inherit;
  font-weight: inherit;
}
/* 基础悬停效果 */
.nav-link {
  transition: all 0.3s ease;
  border-radius: 4px;
  padding: 8px 12px !important;
  margin: 0 2px !important;
}

/* 主导航悬停状态(排除用户名) */
.nav-link:not(.user-menu).nav-link:not(.active):hover {
  /* background: #1E90FF !important;
  color: white !important; */
  transform: translateY(-2px);
}
/* 激活状态优化 */
.nav-link.active {
  /* background: #1E90FF !important; */
  /* color: white !important; */
  /* box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); */
  /* font-weight: 600; */
  color: #8b00ff !important;
}
/* 头像文字悬停颜色 */
.nav-link.dropdown-toggle:hover {
  color: #8b00ff !important;
}

@media (max-width: 992px) {
  .nav-link {
    margin: 4px 0 !important;
  }

  .dropdown-menu {
    background: rgba(13, 110, 253, 0.05);
  }
}
</style>
