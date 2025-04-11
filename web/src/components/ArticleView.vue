<template>
  <div class="article-container">
    <h1>{{ article.title }}</h1>
    <div class="meta">
      <div class="meta-row-1 d-flex align-items-center gap-3">
        <div class="meta-author d-flex align-items-center gap-0">
          <!-- 发布者标签 -->
          <span class="fw-bold">发布者:</span>
          <!-- 作者头像和名称 -->
          <router-link :to="{ name: 'user_profile_index' }"
            class="d-flex align-items-center gap-2 text-decoration-none text-dark">
            <img :src="article.photo" class="avatar rounded-circle"
              style="width: 32px; height: 32px; object-fit: cover;">
            <span>{{ article.author }}</span>
          </router-link>
        </div>
        <!-- 发布时间 -->
        <span>发布时间：{{ formatDate(article.createtime) }}</span>
      </div>

      <div class="d-flex gap-3 mb-0 align-items-center">
        <!-- 点赞、收藏 -->
        <div class="d-flex gap-3">
          <div class="like-box d-flex align-items-center gap-0" @click="toggleLike">
            <div class="img-box">
              <svg v-if="article.isLiked" t="1744265836846" class="icon" viewBox="0 0 1024 1024" version="1.1"
                xmlns="http://www.w3.org/2000/svg" p-id="1456" width="200" height="200">
                <path
                  d="M982.016 425.984l-2.005333 4.010667 2.005333 0 0 82.005333q0 16-6.016 32l-130.005333 299.989333q-20.010667 52.010667-77.994667 52.010667l-384 0q-34.005333 0-59.989333-25.984t-25.984-59.989333l0-425.984q0-34.005333 25.984-59.989333l280.021333-281.984 45.994667 45.994667q18.005333 18.005333 18.005333 43.989333l0 13.994667-41.984 196.010667 269.994667 0q34.005333 0 59.989333 25.002667t25.984 59.008zM41.984 896l0-512 171.989333 0 0 512-171.989333 0z"
                  fill="#444444" p-id="1457"></path>
              </svg>
              <svg v-else t="1744265659774" class="icon" viewBox="0 0 1024 1024" version="1.1"
                xmlns="http://www.w3.org/2000/svg" p-id="2744" width="200" height="200">
                <path
                  d="M640 384h206.08a64 64 0 0 1 62.08 79.552l-96 384a64 64 0 0 1-62.08 48.448H192a64 64 0 0 1-64-64V512a64 64 0 0 1 64-64h128l128-320h128a64 64 0 0 1 64 64v192zM384 460.352V832h366.08l96-384H576V192H491.328L384 460.352zM320 512H192v320h128V512z"
                  fill="#000000" fill-opacity=".9" p-id="2745"></path>
              </svg>
            </div>
            <span class="ms-2">{{ article.isLiked ? '已赞' : '点赞' }}</span>
          </div>

          <div class="like-box d-flex align-items-center gap-0" @click="toggleFavorite">
            <div class="img-box">
              <svg v-if="article.isFavorited" t="1744267313394" class="icon" viewBox="0 0 1024 1024"
                version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2282" width="200" height="200">
                <path
                  d="M485.173861 869.842745l-229.813553 120.819332a53.974999 53.974999 0 0 1-78.316666-56.898821l43.8912-255.899353a53.974999 53.974999 0 0 0-15.522222-47.777399L19.490266 448.857506a53.974999 53.974999 0 0 1 29.915556-92.06371l256.935108-37.338a53.974999 53.974999 0 0 0 40.639999-29.526088l114.909599-232.824864a53.974999 53.974999 0 0 1 96.802221 0l114.906776 232.824864a53.974999 53.974999 0 0 0 40.64 29.52891l256.93793 37.335178a53.974999 53.974999 0 0 1 29.915555 92.06371L815.170657 630.089326a53.974999 53.974999 0 0 0-15.522222 47.777399l43.888377 255.899353a53.974999 53.974999 0 0 1-78.316666 56.898821l-229.813552-120.819332a53.974999 53.974999 0 0 0-50.232733 0z"
                  fill="#231815" p-id="2283"></path>
              </svg>
              <svg v-else t="1744267377163" class="icon" viewBox="0 0 1024 1024" version="1.1"
                xmlns="http://www.w3.org/2000/svg" p-id="2438" width="200" height="200">
                <path
                  d="M530.88 79.488a42.666667 42.666667 0 0 1 19.370667 19.370667l118.485333 240.064 264.96 38.506666a42.666667 42.666667 0 0 1 23.637333 72.768l-191.722666 186.88L810.88 900.906667a42.666667 42.666667 0 0 1-61.909333 44.992L512 821.333333l-236.970667 124.586667A42.666667 42.666667 0 0 1 213.12 900.906667l45.269333-263.850667-191.722666-186.88a42.666667 42.666667 0 0 1 23.658666-72.768l264.938667-38.506667 118.485333-240.064a42.666667 42.666667 0 0 1 57.130667-19.370666z m95.36 317.930667L512 165.909333l-114.24 231.509334L142.293333 434.56l184.853334 180.181333-43.648 254.421334L512 749.034667l228.48 120.106666-43.626667-254.421333 184.832-180.181333-255.445333-37.12z"
                  fill="#333333" p-id="2439"></path>
              </svg>
            </div>
            <span class="ms-2">{{ article.isFavorited ? '已藏' : '收藏' }}</span>
          </div>
        </div>

        <!-- 删除 -->
        <div class="like-box d-flex ms-md-auto align-items-center gap-0">
          <div class="img-box">
            <svg t="1744269711841" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
              p-id="5284" width="200" height="200">
              <path
                d="M439.893333 96h144.213334c9.258667 0 17.322667 0 24.917333 1.194667a96 96 0 0 1 69.973333 50.474666c3.584 6.826667 6.101333 14.464 9.045334 23.210667l4.736 14.293333a53.333333 53.333333 0 0 0 53.888 38.826667h128a32 32 0 0 1 0 64h-725.333334a32 32 0 0 1 0-64h131.84a53.333333 53.333333 0 0 0 50.048-38.826667l4.778667-14.293333c2.901333-8.746667 5.418667-16.384 8.96-23.210667a96 96 0 0 1 70.016-50.474666c7.594667-1.194667 15.658667-1.194667 24.874667-1.194667z m-55.552 128a117.632 117.632 0 0 0 7.466667-18.261333l4.266667-12.8c3.882667-11.648 4.778667-13.994667 5.674666-15.701334a32 32 0 0 1 23.338667-16.853333c1.92-0.298667 4.437333-0.384 16.725333-0.384h140.373334c12.288 0 14.848 0.085333 16.725333 0.426667a32 32 0 0 1 23.338667 16.810666c0.896 1.706667 1.792 4.053333 5.674666 15.744l4.266667 12.8 1.664 4.821334c1.664 4.608 3.626667 9.088 5.802667 13.397333z"
                fill="#1C274C" p-id="5285"></path>
              <path
                d="M252.373333 360.533333a32 32 0 1 0-63.872 4.266667l19.797334 296.618667c3.626667 54.698667 6.570667 98.901333 13.482666 133.632 7.210667 36.053333 19.413333 66.176 44.672 89.813333 25.216 23.594667 56.106667 33.792 92.586667 38.570667 35.072 4.565333 79.36 4.565333 134.229333 4.565333h37.504c54.826667 0 99.157333 0 134.229334-4.565333 36.437333-4.778667 67.328-14.976 92.586666-38.570667 25.216-23.637333 37.418667-53.76 44.629334-89.813333 6.912-34.688 9.813333-78.933333 13.482666-133.632l19.797334-296.618667a32 32 0 0 0-63.872-4.266667l-19.626667 294.4c-3.84 57.472-6.570667 97.493333-12.544 127.573334-5.845333 29.226667-13.952 44.672-25.6 55.594666s-27.648 18.005333-57.173333 21.845334c-30.421333 3.968-70.528 4.053333-128.170667 4.053333h-33.024c-57.6 0-97.706667-0.085333-128.170667-4.053333-29.525333-3.84-45.482667-10.922667-57.173333-21.845334s-19.754667-26.368-25.6-55.552c-5.973333-30.122667-8.704-70.144-12.544-127.658666z"
                fill="#1C274C" p-id="5286"></path>
              <path
                d="M402.133333 437.504a32 32 0 0 1 35.029334 28.629333l21.333333 213.333334a32 32 0 0 1-63.658667 6.4l-21.333333-213.333334a32 32 0 0 1 28.629333-35.029333zM621.866667 437.504a32 32 0 0 1 28.629333 34.986667l-21.333333 213.333333a32 32 0 0 1-63.658667-6.357333l21.333333-213.333334a32 32 0 0 1 34.986667-28.629333z"
                fill="#1C274C" p-id="5287"></path>
            </svg>
          </div>
          <span class="ms-2">删除</span>
        </div>
      </div>
      <div class="border-bottom border-2 mb-0 mt-0"></div>
    </div>
    <div class="content" v-html="article.content"></div>
  </div>

</template>

<script>
// 组件：根据文章id显示文章内容
// 参数：articleId
import axios from 'axios';
import { useStore } from 'vuex';

export default {
  props: {
    articleId: {
      type: Number,
      required: true,
    }
  },
  setup() {
    const store = useStore();
    return {
      store,
    };
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
        isLiked: false,
        isFavorited: false,
      },
    };
  },
  created() {
    this.fetchArticle();
  },
  methods: {
    fetchArticle() {
      axios({
        url: "http://127.0.0.1:3000/article/get/",
        method: "GET",
        params: {
          ArticleId: this.articleId,
        },
        headers: {
          Authorization: "Bearer " + this.store.state.user.token,
        },
      })
        .then((response) => {
          this.article = { // 直接整体赋值
            title: response.data.title,
            content: response.data.content,
            author: response.data.username,
            photo: response.data.photo,
            createtime: response.data.createtime,
            // 根据查询结果判断出来是否被点赞和收藏
            isLiked: response.data.isLiked === "true" ? true : false,
            isFavorited: response.data.isFavorited === "true" ? true : false,
          };
        })
        .catch((error) => {
          console.log(error);
        });

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
    },
    toggleLike() {
      console.log('toggleLike');
      // 通过文章id和用户id[添加/删除]点赞记录
      if(this.article.isLiked === false){
        console.log('add');
        // add
        axios({
          url: "http://127.0.0.1:3000/triclick/add/",
          method: "POST",
          params: {
            to_id: this.articleId.toString(),
            operation: "like",
            category: "article"
          },
          headers: {
            Authorization: "Bearer " + this.store.state.user.token,
          },
        })
        .then((resp) => {
          console.log("resp", resp); // 注意看msg
          this.article.isLiked = !this.article.isLiked; // 切换图标
        })
        .catch((error) => {
          console.log("error", error);
        });
      }
      else{
        // remove
        axios({
          url: "http://127.0.0.1:3000/triclick/remove/",
          method: "POST",
          params: {
            to_id: this.articleId.toString(),
            operation: "like",
            category: "article"
          },
          headers: {
            Authorization: "Bearer " + this.store.state.user.token,
          },
        })
          .then((resp) => {
            console.log("resp", resp);
            this.article.isLiked = !this.article.isLiked; // 切换图标
          })
          .catch((error) => {
            console.log("error", error);
          });
      }
    },
    toggleFavorite() {
      console.log('toggleFavorite');
      // 通过文章id和用户id[添加/删除]收藏记录
      if (this.article.isFavorited === false) {
        // add
        axios({
          url: "http://127.0.0.1:3000/triclick/add/",
          method: "POST",
          params: {
            to_id: this.articleId.toString(),
            operation: "favorite",
            category: "article"
          },
          headers: {
            Authorization: "Bearer " + this.store.state.user.token,
          },
        })
          .then((resp) => {
            console.log("resp", resp); // 注意看msg
            this.article.isFavorited= !this.article.isFavorited; // 切换图标
          })
          .catch((error) => {
            console.log("error", error);
          });
      }
      else {
        // remove
        axios({
          url: "http://127.0.0.1:3000/triclick/remove/",
          method: "POST",
          params: {
            to_id: this.articleId.toString(),
            operation: "favorite",
            category: "article"
          },
          headers: {
            Authorization: "Bearer " + this.store.state.user.token,
          },
        })
          .then((resp) => {
            console.log("resp", resp);
            this.article.isFavorited = !this.article.isFavorited; // 切换图标
          })
          .catch((error) => {
            console.log("error", error);
          });
      }
    },
  },
};
</script>

<style scoped>
.article-container {
  max-width: 900px;
  margin: 0 auto;
  padding: 20px;
}

.meta {
  margin-bottom: 10px;
  color: #666;
}
.meta span {
  margin-right: 15px;
}
.meta-row-1{
  display: flex;
  align-items: center;
  /* 垂直居中对齐 */
  gap: 5px;
  /* 元素间间距 */
  margin-bottom: 0px;
  color: #666;
}
/* 作者头像和名称的间距 */
.router-link {
  display: flex;
  align-items: center;
  gap: 1px;
}
/* 发布时间的样式 */
.meta span:last-child {
  flex-shrink: 0;
  /* 防止文字被压缩 */
}

.content{
  margin-top: 20px;
}
/* 新增按钮组样式 */
.like-box {
  cursor: pointer;
  padding: 6px 10px;
  border-radius: 8px;
  transition: all 0.3s;
  /* 使用Flex布局 */
  display: flex;
  align-items: center;
  /* 图标与文字间距 */
  gap: 8px;
}

/* 图标尺寸统一 */
.like-box .icon {
  width: 20px !important;
  height: 20px !important;
}

/* 文字样式 */
.like-box .text {
  font-size: 16px;
  line-height: 1.2;
}

/* 图标与文字间距 */
.like-box .img-box {
  display: flex;
  align-items: center;
}
/* 新增头像样式 */
.avatar {
  border: 1px solid #dee2e6;
  transition: transform 0.2s;
}

.avatar:hover {
  transform: scale(1.1);
}
</style>