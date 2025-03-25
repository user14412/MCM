<!-- 个人中心导航栏 -->
<template>
  <nav class="navbar navbar-expand-lg navbar-light bg-white border-bottom">
    <div class="container-fluid">
      <div class="collapse navbar-collapse">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li 
            v-for="(item, index) in navItems" 
            :key="item.name" 
            class="nav-item position-relative"
          >
            <!-- 常规导航项 -->
            <template v-if="!item.dropdown">
              <a
                class="nav-link hover-effect"
                :class="{ 'active-nav': activeIndex === index }"
                href="#"
                @click.prevent="setActive(index)"
              >
                {{ item.name }}
              </a>
            </template>

            <!-- 带下拉的导航项 -->
            <div v-else class="dropdown">
              <a
                class="nav-link hover-effect dropdown-toggle"
                :class="{ 'active-nav': activeIndex === index }"
                href="#"
                role="button"
                data-bs-toggle="dropdown"
                @click.prevent="setActive(index)"
              >
                {{ item.name }}
              </a>
              <ul class="dropdown-menu">
                <li><a class="dropdown-item" href="#">系统通知</a></li>
                <li><a class="dropdown-item" href="#">我的回复</a></li>
              </ul>
            </div>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script>
export default {
  data() {
    return {
      activeIndex: 0,
      navItems: [
        { name: '发布' },
        { name: '收藏' },
        { name: '点赞' },
        { name: '消息', dropdown: true },
        { name: '设置' },
      ]
    }
  },
  methods: {
    setActive(index) {
      this.activeIndex = index
      // 触发父组件的事件setActive：调用父组件的setActive方法
      this.$emit('setActive', this.navItems[index].name)
    }
  }
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