<!-- 个人中心 - 功能区 - 个人中心导航栏子组件 -->
<template>
  <nav class="navbar navbar-expand-lg navbar-light bg-white border-bottom">
    <div class="container-fluid">
      <div class="collapse navbar-collapse">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li v-for="(item, index) in navItems" :key="item.name" class="nav-item position-relative"
            :class="{ 'dropdown': item.dropdown }">
            <!-- 常规导航项 -->
            <div v-if="!item.dropdown">
              <a class="nav-link hover-effect" :class="{ 'active-nav': activeIndex === index }" href="#"
                @click="setActive(index)">
                {{ item.name }}
              </a>
            </div>

            <!-- 带下拉的导航项 -->
            <div v-else>
              <a class="nav-link hover-effect dropdown-toggle" type="button" data-bs-toggle="dropdown"
                data-bs-auto-close="true" aria-expanded="false" :class="{ 'active-nav': activeIndex === index }">
                {{ item.name }}
              </a>
              <ul class="dropdown-menu">
                <li v-if="index === 1"><a class="dropdown-item" href="#" @click="setActive((index + 1) * 10 + 1)">藏品</a>
                </li>
                <li v-if="index === 1"><a class="dropdown-item" href="#" @click="setActive((index + 1) * 10 + 2)">文章</a>
                </li>
                <li v-if="index === 2"><a class="dropdown-item" href="#" @click="setActive((index + 1) * 10 + 1)">藏品</a>
                </li>
                <li v-if="index === 2"><a class="dropdown-item" href="#" @click="setActive((index + 1) * 10 + 2)">文章</a>
                </li>
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
import { Dropdown } from 'bootstrap';

export default {
  emits: ['setActive'], // 声明自定义事件
  setup(props, context) {
    let activeIndex = ref(0);
    let navItems = reactive([
      {
        name: '文章',
        number: 1, // 1
      },
      {
        name: '收藏',
        dropdown: true,
        number: 2, // 21 // 22
      },
      {
        name: '点赞',
        dropdown: true,
        number: 3, // 31 // 32
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

      // 关闭所有打开的下拉菜单
      const dropdowns = document.querySelectorAll('.dropdown-toggle');
      dropdowns.forEach(toggle => {
        const dropdown = Dropdown.getInstance(toggle);
        if (dropdown) {
          dropdown.hide();
        }
      });
    }
    setActive(0);

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