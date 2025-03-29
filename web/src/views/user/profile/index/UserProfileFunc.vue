<!-- 个人中心 - 功能区子组件 -->
<template>
  <div class="card">
    <div class="card-body">
      <UserProfileNavBar @setActive="setActive" style="margin-top: -15px;" />

      <UserProfilePost v-if="func == 11" />
      <UserProfileWrite v-if="func == 12" />
      <!-- <UserProfileFavorite v-if="func == 2" /> -->
      <UserProfileLike v-if="func == 3" />
      <UserProfileSysMessage v-if="func == 41" />
      <UserProfileMessage v-if="func == 42" />
      <UserProfileSetting v-if="func == 5" />
    </div>
  </div>
</template>

<script>
import UserProfileNavBar from '@/views/user/profile/index/UserProfileNavBar.vue';
import UserProfilePost from '@/views/user/profile/post/UserProfilePost.vue';
import UserProfileWrite from '@/views/user/profile/post/UserProfileWrite.vue';
// import UserProfileFavorite from '@/views/user/profile/favorite/UserProfileFavorite.vue';
import UserProfileLike from '@/views/user/profile/like/UserProfileLike.vue';
import UserProfileSysMessage from '@/views/user/profile/message/UserProfileSysMessage.vue';
import UserProfileMessage from '@/views/user/profile/message/UserProfileMessage.vue';
import UserProfileSetting from '@/views/user/profile/setting/UserProfileSetting.vue';
import { ref } from 'vue';

export default {
  components: {
    UserProfileNavBar,
    UserProfilePost, // 11
    UserProfileWrite, // 12
    // UserProfileFavorite, // 2
    UserProfileLike, // 3
    UserProfileSysMessage, // 41
    UserProfileMessage, // 42
    UserProfileSetting, // 5
  },
  setup(){
    let func = ref(11);

    const setActive = (index) => {
      func.value = index;
    }

    return{
      func,
      setActive,
    }
  }
}
</script>

<style scoped>
.my-post-title {
    font-size: 20px;
    margin-bottom:20px;
}
.article-item{
  border-bottom: 1px solid #eee;  /* 修正拼写错误 */
  padding: 12px 0;               /* 调整间距为12px */
}
.article-item:not(:last-child) {
  margin-bottom: 8px;  /* 新增间隔控制 */
}
</style>