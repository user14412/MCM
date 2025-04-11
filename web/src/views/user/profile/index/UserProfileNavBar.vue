<!-- 个人中心 - 功能区 - 个人中心导航栏子组件 -->
<template>
  <nav class="navbar navbar-expand-lg navbar-light bg-white border-bottom">
    <div class="container-fluid">
      <div class="collapse navbar-collapse">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li v-for="(item, index) in navItems" :key="item.name" class="nav-item position-relative"
            :class="{ 'dropdown': activeIndex === index }">
            <!-- 常规导航项 -->
            <div v-if="!item.dropdown">
              <a class="nav-link hover-effect" :class="{ 'active-nav': activeIndex === index }" href="#"
                @click.prevent="setActive(index)">
                {{ item.name }}
              </a>
            </div>

            <!-- 带下拉的导航项 -->
            <div v-else>
              <a class="nav-link hover-effect dropdown-toggle" :class="{ 'active-nav': activeIndex === index }" href="#"
                role="button" data-bs-toggle="dropdown" @click.prevent="setActive((index + 1) * 10 + 1)">
                {{ item.name }}
              </a>
              <ul class="dropdown-menu">
                <li v-if="index === 0"><a class="dropdown-item" href="#"
                    @click.prevent="setActive((index + 1) * 10 + 1)">我的发布</a></li>
                <li v-if="index === 0"><a class="dropdown-item" href="#"
                    @click.prevent="setActive((index + 1) * 10 + 2)">编辑文章</a></li>
                <li v-if="index === 2"><a class="dropdown-item" href="#"
                    @click.prevent="setActive((index + 1) * 10 + 1)">藏品</a></li>
                <li v-if="index === 2"><a class="dropdown-item" href="#"
                    @click.prevent="setActive((index + 1) * 10 + 2)">文章</a></li>
                <li v-if="index === 3"><a class="dropdown-item" href="#"
                    @click.prevent="setActive((index + 1) * 10 + 1)">系统通知</a></li>
                <li v-if="index === 3"><a class="dropdown-item" href="#"
                    @click.prevent="setActive((index + 1) * 10 + 2)">我的回复</a></li>
              </ul>
            </div>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script>
import { ref, reactive } from 'vue'

export default {
  emits: ['setActive'], // 声明自定义事件
  setup(props, context) {
    let activeIndex = ref(0);
    let navItems = reactive([
      {
        name: '发布',
        dropdown: true,
        number: 1, // 11 // 12
      },
      {
        name: '收藏',
        number: 2,
      },
      {
        name: '点赞',
        dropdown: true,
        number: 3, // 31 // 32
      },
      {
        name: '消息',
        dropdown: true,
        number: 4, // 41 // 42
      },
      {
        name: '设置',
        number: 5,
      },
    ]);

    const setActive = (index) => {
      // 非下拉菜单
      if (index <= 10) {
        activeIndex.value = index;
        // 触发父组件的事件setActive：调用父组件的setActive方法
        context.emit('setActive', navItems[index].number);
      }
      else {
        // 下拉菜单
        // 取出十位，高亮显示菜单栏
        activeIndex.value = Math.floor(index / 10) - 1; // 下标从0开始
        context.emit('setActive', index);
      }
    }

    return {
      activeIndex,
      navItems,
      setActive,
    }
  },
}
</script>

<style scoped>
.nav-link {
  color: #333 !important;
  font-weight: 500;
  padding: 0.75rem 1.5rem !important;
  transition: all 0.3s ease;
}

.active-nav {
  color: #007bff !important;
  position: relative;
}

.active-nav::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 50%;
  transform: translateX(-50%);
  width: 70%;
  height: 3px;
  background-color: #007bff;
  border-radius: 2px;
}

.hover-effect:hover {
  background-color: #f8f9fa;
  border-radius: 4px;
}

.dropdown-menu {
  margin-top: 0.5rem !important;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}
</style>