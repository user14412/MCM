<template>
  <nav class="navbar navbar-expand-lg bg-body-tertiary">
    <div class="container">
       <!-- 换掉实现切换页面不刷新(其实是一个页面) -->
      <router-link class="navbar-brand" :to="{name: 'home'}">MCM</router-link>
      <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <router-link :class="route_name == 'introduction_index' ? 'nav-link active':'nav-link'" :to="{name: 'introduction_index'}">本馆概况</router-link>
          </li>
          <!-- <li class="nav-item">
            <router-link :class="route_name == 'record_index' ? 'nav-link active':'nav-link'" :to="{name: 'record_index'}">对局记录</router-link>
          </li>
          <li class="nav-item">
            <router-link :class="route_name == 'ranklist_index' ? 'nav-link active':'nav-link'" :to="{name: 'ranklist_index'}">排行榜</router-link>
          </li> -->
        </ul>
        <ul class="navbar-nav">
          <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
            zanyyan123
          </a>
          <ul class="dropdown-menu">
            <li>
              <router-link class="dropdown-item" :to="{name: 'user_profile_index'}">个人中心</router-link>
            </li>
            <li><a class="dropdown-item" href="#">退出</a></li>
          </ul>
        </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script>
import { useRoute } from 'vue-router';
import { computed } from 'vue'

export default{
  setup(){
    const route = useRoute();
    let route_name = computed(() => route.name)
    return{
      route_name
    }
  }
}
</script>

<style scoped>
</style>
