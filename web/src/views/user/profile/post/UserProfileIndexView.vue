<template>
  <ContentField>
    <div class="row">
      <div class="col-3">
        <UserProfileInfo :user="user" />
      </div>
      <div class="col-9">
        <UserProfilePost :posts="posts" />
      </div>
    </div>
  </ContentField>
</template>

<script>
import ContentField from "@/components/ContentField.vue"
import UserProfileInfo from "@/views/user/profile/post/UserProfileInfo.vue"
import UserProfilePost from "./UserProfilePost.vue";
import { computed } from "vue";
import { useStore } from "vuex";
import $ from "jquery";
import { reactive } from "vue";

export default{
  components :{
    ContentField,
    UserProfileInfo,
    UserProfilePost,
  },
  setup(){
    const store = useStore();
    let user = computed(() => store.state.user);
    const posts = reactive({
      count: 0,
      posts: [],
    })

    const getArticleByUser = () => {
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
      })
    }

    getArticleByUser();

    return {
      user,
      posts,
    }
  },
}
</script>
 
<style scoped>

</style>
