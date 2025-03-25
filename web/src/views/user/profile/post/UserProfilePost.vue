<template>
    <div v-for="post in posts.posts" :key="post.id" class="article-item">
          <UserPostCard :article="post"/>
    </div>
</template>

<script>
import UserPostCard from './UserPostCard.vue';
import { useStore } from 'vuex';
import { computed } from 'vue';
import { reactive } from 'vue';
import $ from 'jquery';


export default {
  components: {
    UserPostCard,
  },
  setup(){
    const store = useStore();
    let user = computed(() => store.state.user);
    const posts = reactive({
      count: 0,
      posts: [],
    });

    $.ajax({
      url: "http://127.0.0.1:3000/article/get/user/",
      type: "GET",
      headers: {
        Authorization: "Bearer " + store.state.user.token,
      },
      success(resp) {
        console.log("getArticleByUser 请求成功:", resp);
        posts.posts = resp;
        posts.count = posts.posts.length;
      },
      error(resp) {
        console.error("getArticleByUser 请求失败:", resp); // 修正后的错误处理
      }
    });

    return{
      user,
      posts,
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