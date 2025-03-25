<template>
  <div class="card">
    <div class="card-body">
      <UserProfileNavBar @setActive="setActive" />
      <!-- <div class="my-post-title">
          文章数量：{{posts.count}}
      </div> -->
      <div v-for="post in posts.posts" :key="post.id" class="article-item">
            <UserPostCard :article="post"/>
      </div>
    </div>
  </div>
</template>

<script>
import UserPostCard from './UserPostCard.vue';
import UserProfileNavBar from '@/components/UserProfileNavBar.vue';

export default {
  props: {
    posts: {
      type: Object,
      required: true,
    }
  },
  components: {
    UserPostCard,
    UserProfileNavBar,
  },
  setup(props){
    console.log("输出", props.posts);
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