<template>
  <div class="exhibit-container">
    <h1>name: {{ exhibit.name }}</h1>
  </div>
  
</template>

<script>
// 组件：根据藏品id显示藏品详情
// 参数：exhibitId
// import axios from 'axios';
// import { useStore } from 'vuex';

export default {
  props: {
    exhibitId: {
      type: Number,
      required: true,
    }
  },
  watch: {
    exhibitId: {
      immediate: true,
      handler(newVal){
        if(newVal){
          this.fetchExhibit();
        }
      }
    }
  },
  data() {
    return {
      exhibit: {
        name: '',
      },
    };
  },
  created() {
    this.fetchExhibit();
  },
  methods: {
    fetchExhibit() {
      // const store = useStore();
      // axios({
      //   url: "http://127.0.0.1:3000/article/get/",
      //   method: "GET",
      //   params: {
      //     ArticleId: this.articleId,
      //   },
      //   headers: {
      //     Authorization: "Bearer " + store.state.user.token,
      //   },
      // })
      //   .then((response) => {
      //     this.article = { // 直接整体赋值
      //       title: response.data.title,
      //       content: response.data.content,
      //       author: response.data.username,
      //       photo: response.data.photo,
      //       createtime: response.data.createtime,
      //     };
      //   })
      //   .catch((error) => {
      //     console.log(error);
      //   });
      this.exhibit = {
        name: '神山识'
      }
    },
  },
};
</script>

<style scoped>
.exhibit-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}
</style>