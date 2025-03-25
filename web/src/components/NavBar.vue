<template>
  <nav class="navbar navbar-expand-lg bg-body-tertiary fixed-top">
    <div class="container">
       <!-- 换掉实现切换页面不刷新(其实是一个页面) -->
      <router-link class="navbar-brand" :to="{name: 'home'}">
        <img src="@/assets/images/Ciallo.jpg" alt="logo" width="30" height="24" class="d-inline-block align-text-top">
        MCM
      </router-link>
      <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
              本馆概况
            </a>
            <ul class="dropdown-menu">
              <li>
                <router-link :class="route_name == 'introduction_index' ? 'nav-link active':'nav-link'" :to="{name: 'introduction_index'}">本馆概况</router-link>
              </li>
              <li>
                <router-link :class="route_name == 'introduction_index' ? 'nav-link active':'nav-link'" :to="{name: 'news_index'}">新闻动态</router-link>
              </li>
              <li>
                <router-link :class="route_name == 'introduction_index' ? 'nav-link active':'nav-link'" :to="{name: 'visit_guide_index'}">参观须知</router-link>
              </li>
            </ul>
          </li>

          <li class="nav-item">
            <router-link :class="route_name == 'introduction_index' ? 'nav-link active':'nav-link'" :to="{name: 'introduction_index'}">全景参观</router-link>
          </li>
          <li class="nav-item">
            <router-link :class="route_name == 'introduction_index' ? 'nav-link active':'nav-link'" :to="{name: 'introduction_index'}">馆藏展品</router-link>
          </li>
          <li class="nav-item">
            <router-link :class="route_name == 'introduction_index' ? 'nav-link active':'nav-link'" :to="{name: 'introduction_index'}">问卷反馈</router-link>
          </li>
          
        </ul>

        <ul class="navbar-nav" v-if="$store.state.user.is_login">
          <li class="nav-item dropdown">
            <div class="d-flex align-items-center dropdown-toggle-container"
                role="button"
                data-bs-toggle="dropdown"
                aria-expanded="false">
              <img 
                  :src="store.state.user.photo" 
                  class="img-fluid rounded-circle mt-2 mx-auto"
                  style="max-width: 38px; height: 38px; object-fit: cover;"
                  alt="用户头像">
              <span class="nav-link dropdown-toggle text-reset">
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
          <router-link class="nav-link" :to="{name: 'user_account_login'}" role="button">
            登录
          </router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" :to="{name: 'user_account_register'}" role="button">
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
.navbar {
  z-index: 1000; /* 确保导航栏在其他内容之上 */
}
</style>
