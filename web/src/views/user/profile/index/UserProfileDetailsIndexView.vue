<!-- 个人中心页面 -->
<template>
  <ContentField>
    <div class="row">
      <div class="col-3">
        <!-- 确保数据就绪后再渲染子组件，不加v-if：可能出现子组件props.user正常（后续数据就绪），而user为undefined（未就绪就渲染） -->
        <UserProfileInfo v-if="user" :user="user" />
      </div>
      <div class="col-9">
        <!-- <UserProfileFunc /> -->
         功能区
      </div>
    </div>
  </ContentField>
</template>

<script>
import ContentField from "@/components/ContentField.vue"
import UserProfileInfo from "@/views/user/profile/index/UserProfileInfo.vue"
// import UserProfileFunc from "@/views/user/profile/index/UserProfileFunc.vue"
// import { computed } from "vue";
import { useStore } from "vuex";
import { ref } from "vue";
import axios from "axios";
import { useRoute } from "vue-router";

export default{
  components :{
    ContentField,
    UserProfileInfo,
    // UserProfileFunc,
  },
  setup(){
    const store = useStore();
    const route = useRoute();
    let user = ref(null);
    let userId = parseInt(route.params.userId)
    console.log("userId", userId)
    axios({
      // 根据userId获取user对象
      url: "http://127.0.0.1:3000/user/profile/get/",
      method: "GET",
      params: {
        userId: parseInt(userId),
      },
      headers: {
        Authorization: "Bearer " + store.state.user.token,
      },
    })
    .then(response => {
      console.log("getArticleByUser 请求成功:", response.data);
      user.value = response.data;

    })
    .catch(error =>{
      console.error("getArticleByUser 请求失败:", error.response.data);
    })

    return {
      user,
    }
  },
}
</script>
 
<style scoped>

</style>
