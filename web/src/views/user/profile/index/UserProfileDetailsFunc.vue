<!-- 个人中心 - 功能区子组件 -->
<template>
  <div class="card">
    <div class="card-body">
      <UserProfileDetailsNavBar :userId="$props.userId" @setActive="setActive" style="margin-top: -15px;" />

      <UserProfileDetailsPost v-if="func == 1" :userId="$props.userId" />
      <UserProfileDetailsFavoriteExhibit v-if="func == 21" :userId="$props.userId" />
      <UserProfileDetailsFavoriteArticle v-if="func == 22" :userId="$props.userId" />
      <UserProfileDetailsLikeExhibit v-if="func == 31" :userId="$props.userId" />
      <UserProfileDetailsLikeArticle v-if="func == 32" :userId="$props.userId" />
    </div>
  </div>
</template>

<script>
import UserProfileDetailsNavBar from '@/views/user/profile/index/UserProfileDetailsNavBar.vue';
import UserProfileDetailsPost from '@/views/user/profile/post/UserProfileDetailsPost.vue';
import UserProfileDetailsFavoriteExhibit from '@/views/user/profile/favorite/UserProfileDetailsFavoriteExhibit.vue';
import UserProfileDetailsFavoriteArticle from '@/views/user/profile/favorite/UserProfileDetailsFavoriteArticle.vue';
import UserProfileDetailsLikeExhibit from '@/views/user/profile/like/UserProfileDetailsLikeExhibit.vue';
import UserProfileDetailsLikeArticle from '@/views/user/profile/like/UserProfileDetailsLikeArticle.vue';
import { ref } from 'vue';


export default {
  props:{
    userId: {
      type: Number,
      required: true,
    }
  },
  components: {
    UserProfileDetailsNavBar,
    UserProfileDetailsPost, // 1
    UserProfileDetailsFavoriteExhibit, // 21
    UserProfileDetailsFavoriteArticle, // 22
    UserProfileDetailsLikeExhibit, // 31
    UserProfileDetailsLikeArticle, // 32
  },
  setup() {
    let func = ref(11);

    const setActive = (index) => {
      func.value = index;
    }

    return {
      func,
      setActive,
    }
  }
}
</script>

<style scoped>
.my-post-title {
  font-size: 20px;
  margin-bottom: 20px;
}

.article-item {
  border-bottom: 1px solid #eee;
  /* 修正拼写错误 */
  padding: 12px 0;
  /* 调整间距为12px */
}

.article-item:not(:last-child) {
  margin-bottom: 8px;
  /* 新增间隔控制 */
}
</style>