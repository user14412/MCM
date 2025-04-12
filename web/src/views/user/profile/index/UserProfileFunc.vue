<!-- 个人中心 - 功能区子组件 -->
<template>
  <div class="card">
    <div class="card-body">
      <UserProfileNavBar @setActive="setActive" style="margin-top: -15px;" />

      <UserProfilePost v-if="func == 1" />
      <UserProfileWrite v-if="func == 2" />
      <UserProfileFavoriteExhibit v-if="func == 31" />
      <UserProfileFavoriteArticle v-if="func == 32" />
      <UserProfileLikeExhibit v-if="func == 41" />
      <UserProfileLikeArticle v-if="func == 42" />
      <UserProfileSysMessage v-if="func == 51" />
      <UserProfileMessage v-if="func == 52" />
      <UserProfileSetting v-if="func == 6" />
    </div>
  </div>
</template>

<script>
import UserProfileNavBar from '@/views/user/profile/index/UserProfileNavBar.vue';
import UserProfilePost from '@/views/user/profile/post/UserProfilePost.vue';
import UserProfileWrite from '@/views/user/profile/post/UserProfileWrite.vue';
import UserProfileFavoriteExhibit from '@/views/user/profile/favorite/UserProfileFavoriteExhibit.vue';
import UserProfileFavoriteArticle from '@/views/user/profile/favorite/UserProfileFavoriteArticle.vue';
import UserProfileLikeExhibit from '@/views/user/profile/like/UserProfileLikeExhibit.vue';
import UserProfileLikeArticle from '@/views/user/profile/like/UserProfileLikeArticle.vue';
import UserProfileSysMessage from '@/views/user/profile/message/UserProfileSysMessage.vue';
import UserProfileMessage from '@/views/user/profile/message/UserProfileMessage.vue';
import UserProfileSetting from '@/views/user/profile/setting/UserProfileSetting.vue';
import { ref } from 'vue';


export default {
  components: {
    UserProfileNavBar,
    UserProfilePost, // 1
    UserProfileWrite, // 2
    UserProfileFavoriteExhibit, // 31
    UserProfileFavoriteArticle, // 32
    UserProfileLikeExhibit, // 41
    UserProfileLikeArticle, // 42
    UserProfileSysMessage, // 51
    UserProfileMessage, // 52
    UserProfileSetting, // 6
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