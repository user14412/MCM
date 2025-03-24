<template>
  <div class="article-container">
    <h1>{{ article.title }}</h1>
    <div class="meta">
      <span>发布者：{{ article.author }}</span>
      <span>发布时间：{{ formatDate(article.createtime) }}</span>
    </div>
    <div class="content" v-html="article.content"></div>
  </div>
  
</template>

<script>
import $ from 'jquery';
import { useStore } from 'vuex';

export default {
  props: {
    articleId: {
      type: Number,
      required: true,
    }
  },
  watch: {
    articleId: {
      immediate: true,
      handler(newVal){
        if(newVal){
          this.fetchArticle();
        }
      }
    }
  },
  data() {
    return {
      article: {
        title: '',
        content: '',
        author: '',
        photo: '',
        createtime: '',
      },
    };
  },
  created() {
    this.fetchArticle();
  },
  methods: {
    fetchArticle() {
      const store = useStore();
      const self = this;
      $.ajax({
        url: "http://127.0.0.1:3000/article/get/",
        type: "GET",
        data: {
          ArticleId: this.articleId,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp){
          console.log(resp);
          self.article = { // 直接整体赋值
            title: resp.title,
            content: resp.content,
            author: resp.username,
            photo: resp.photo,
            createtime: resp.createtime,
          };
        },
        error(resp){
          console.log(resp);
        }
      })
    },
    formatDate(date) {
      if (!date) return '';
      return new Date(date).toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      });
    }
  },
};
</script>

<style scoped>
.article-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.meta {
  margin-bottom: 20px;
  color: #666;
}

.meta span {
  margin-right: 15px;
}

.content img {
  max-width: 100%;
  height: auto;
  margin: 10px 0;
}
</style>