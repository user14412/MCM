<template>
  <div class="exhibit-container container mt-5">
    <div class="row">
      <!-- 图片轮播区 -->
      <div class="col-md-4 mb-4">
        <div class="carousel slide" @click.stop="handleCarouselClick" @mouseover="stopAutoPlay"
          @mouseout="startAutoPlay">
          <!-- 鼠标悬停时暂停自动轮播 -->
          <div class="carousel-inner" style="max-height: 500px;">
            <div v-for="(img, index) in sortedImages" :key="img.id"
              :class="['carousel-item', { active: currentIndex === index }]">
              <img :src="img.url" class="d-block w-100 h-100 object-fit-cover" alt="展品图片">
            </div>
          </div>

          <!-- 轮播指示器 -->
          <div class="carousel-indicators">
            <button v-for="(img, index) in sortedImages" :key="img.id" type="button"
              :class="{ active: currentIndex === index }" @click="currentIndex = index"></button>
          </div>
        </div>
      </div>

      <!-- 文字信息区 -->
      <div class="col-md-8">
        <h2 class="mb-4">{{ exhibit.name }}</h2>

        <!-- 点赞、收藏 -->
        <div class="d-flex gap-3 mb-4">
          <div class="like-box d-flex align-items-center gap-2" @click="toggleLike">
            <div class="img-box">
              <svg v-if="exhibit.isLiked" t="1744265836846" class="icon" viewBox="0 0 1024 1024" version="1.1"
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
            <span class="ms-2">{{ exhibit.isLiked ? '已赞' : '点赞' }}</span>
          </div>

          <div class="like-box d-flex align-items-center gap-2" @click="toggleFavorite">
            <div class="img-box">
              <svg v-if="exhibit.isFavorited" t="1744267313394" class="icon" viewBox="0 0 1024 1024" version="1.1"
                xmlns="http://www.w3.org/2000/svg" p-id="2282" width="200" height="200">
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
            <span class="ms-2">{{ exhibit.isFavorited ? '已藏' : '收藏' }}</span>
          </div>
        </div>

        <div class="bg-light p-4 rounded">
          <p class="lead" style="white-space: pre-wrap;">{{ exhibit.comment }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { useStore } from 'vuex';
import axios from 'axios';
// import { ref } from 'vue';

export default {
  props: {
    exhibitId: {
      type: Number,
      required: true
    }
  },
  setup() {
    const store = useStore();
    return {
      store,
    };
  },
  data() {
    return {
      exhibit: {
        name: '',
        comment: '',
        images: [],
        isLiked: false,
        isFavorited: false, 
      },
      currentIndex: 0,
      autoPlayTimer: null,
    }
  },
  computed: {
    // 按id排序后的图片列表
    sortedImages() {
      return [...this.exhibit.images].sort((a, b) => a.id - b.id)
    }
  },
  watch: {
    exhibitId: {
      immediate: true,
      handler() {
        this.fetchExhibit()
      }
    }
  },
  methods: {
    // 获取藏品详情数据
    fetchExhibit() {
      axios({
        url: "http://127.0.0.1:3000/exhibit/get/",
        method: "GET",
        params: {
          ExhibitId: this.exhibitId,
        },
        headers: {
          Authorization: "Bearer " + this.store.state.user.token,
        },
      })
        .then((resp) => {
          this.exhibit = {
            name: resp.data.name,
            comment: resp.data.comment,
            images: JSON.parse(resp.data.images_string),
            isLiked: resp.data.isLiked === "true" ? true : false,
            isFavorited: resp.data.isFavorited === "true" ? true : false,
          };
        })
        .catch((error) => {
          console.log("error", error);
        });
    },
    // 图片自动轮播
    startAutoPlay() {
      this.autoPlayTimer = setInterval(() => {
        this.currentIndex = (this.currentIndex + 1) % this.sortedImages.length
      }, 5000) // 每5秒切换一张图片
    },
    // 停止自动轮播
    stopAutoPlay(){
      clearInterval(this.autoPlayTimer);
    },
    // 图片点击切换
    handleCarouselClick(e) {
      const rect = e.currentTarget.getBoundingClientRect()
      const clickX = e.clientX - rect.left
      const width = rect.width

      if (clickX < width * 0.3) {
        this.currentIndex = (this.currentIndex - 1 + this.sortedImages.length) % this.sortedImages.length
      } else if (clickX > width * 0.7) {
        this.currentIndex = (this.currentIndex + 1) % this.sortedImages.length
      }
    },
    toggleLike(){
      console.log('toggleLike');
      // 通过文章id和用户id[添加/删除]点赞记录
      if (this.exhibit.isLiked === false) {
        console.log('add');
        // add
        axios({
          url: "http://127.0.0.1:3000/triclick/add/",
          method: "POST",
          params: {
            to_id: this.exhibitId.toString(),
            operation: "like",
            category: "exhibit"
          },
          headers: {
            Authorization: "Bearer " + this.store.state.user.token,
          },
        })
          .then((resp) => {
            console.log("resp", resp); // 注意看msg
            this.exhibit.isLiked = !this.exhibit.isLiked; // 切换图标
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
            to_id: this.exhibitId.toString(),
            operation: "like",
            category: "exhibit"
          },
          headers: {
            Authorization: "Bearer " + this.store.state.user.token,
          },
        })
          .then((resp) => {
            console.log("resp", resp);
            this.exhibit.isLiked = !this.exhibit.isLiked; // 切换图标
          })
          .catch((error) => {
            console.log("error", error);
          });
      }
    },
    toggleFavorite(){
      console.log('toggleFavorite');
      // 通过文章id和用户id[添加/删除]收藏记录
      if (this.exhibit.isFavorited === false) {
        // add
        axios({
          url: "http://127.0.0.1:3000/triclick/add/",
          method: "POST",
          params: {
            to_id: this.exhibitId.toString(),
            operation: "favorite",
            category: "exhibit"
          },
          headers: {
            Authorization: "Bearer " + this.store.state.user.token,
          },
        })
          .then((resp) => {
            console.log("resp", resp); // 注意看msg
            this.exhibit.isFavorited = !this.exhibit.isFavorited; // 切换图标
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
            to_id: this.exhibitId.toString(),
            operation: "favorite",
            category: "exhibit"
          },
          headers: {
            Authorization: "Bearer " + this.store.state.user.token,
          },
        })
          .then((resp) => {
            console.log("resp", resp);
            this.exhibit.isFavorited = !this.exhibit.isFavorited; // 切换图标
          })
          .catch((error) => {
            console.log("error", error);
          });
      }
    },
  },
  mounted(){
    this.startAutoPlay(); // 组件加载时启动自动轮播
  },
  beforeUnmount(){
    this.stopAutoPlay(); // 组件卸载时停止自动轮播
  }
}
</script>

<style scoped>
.carousel {
  position: relative;
  cursor: pointer;
}

.carousel-item {
  transition: opacity 0.6s ease;
}

.carousel-item:not(.active) {
  opacity: 0;
  display: block !important;
}

.object-fit-cover {
  object-fit: cover;
}

.carousel-indicators {
  position: absolute;
  bottom: 10px;
}

.carousel-indicators button {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  border: 2px solid white;
  background: transparent;
  margin: 0 5px;
}

.carousel-indicators button.active {
  background: white;
}
/* 新增按钮组样式 */
.like-box {
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 8px;
  transition: all 0.3s;
  /* 使用Flex布局 */
  display: flex;
  align-items: center;
  gap: 8px;
  /* 图标与文字间距 */
}

/* .like-box:hover {
  background-color: #f0f0f0;
} */

/* 图标尺寸统一 */
.like-box .icon {
  width: 24px !important;
  height: 24px !important;
}
/* 文字样式 */
.like-box .text {
  font-size: 14px;
  line-height: 1.5;
}
/* 图标与文字间距 */
.like-box .img-box {
  display: flex;
  align-items: center;
}
</style>