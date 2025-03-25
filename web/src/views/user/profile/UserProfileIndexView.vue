<template>
  <ContentField>
    <div class="row">
      <div class="col-3">
        <UserProfileInfo :user="user" />
      </div>
      <div class="col-9">
        <UserProfileFunc />
      </div>
    </div>
  </ContentField>
</template>

<script>
import ContentField from "@/components/ContentField.vue"
import UserProfileInfo from "@/views/user/profile/UserProfileInfo.vue"
import UserProfileFunc from "@/views/user/profile/index/UserProfileFunc.vue"
import { computed } from "vue";
import { useStore } from "vuex";

export default{
  components :{
    ContentField,
    UserProfileInfo,
    UserProfileFunc,
  },
  setup(){
    const store = useStore();
    let user = computed(() => store.state.user);

    return {
      user,
    }
  },
}
</script>
 
<style scoped>

</style>
