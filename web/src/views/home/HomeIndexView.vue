<template>
  <ContentField>
    <!-- 轮播区 -->
    <div id="home-carousel" class="carousel slide mb-4" data-bs-ride="carousel">
      <div class="carousel-inner">
        <!-- 本馆概况 -->
        <div class="carousel-item active" @click="goTo('/introduction/')" style="cursor:pointer;">
          <img src="https://i.imgur.com/59nbNA6.jpeg" class="d-block w-100" alt="本馆概况">
          <div class="carousel-caption d-none d-md-block">
            <h5>本馆概况</h5>
            <p>了解HEU数字海洋文化馆</p>
          </div>
        </div>
        <!-- 新闻1 -->
        <div class="carousel-item" @click="goTo('/article/46/')" style="cursor:pointer;">
          <img src="https://i.imgur.com/g1fynHs.jpeg" class="d-block w-100" alt="新闻1">
          <div class="carousel-caption d-none d-md-block">
            <h5>海洋馆的珍宝：探索生命的奥秘</h5>
            <p>最新新闻资讯</p>
          </div>
        </div>
        <!-- 新闻2 -->
        <div class="carousel-item" @click="goTo('/article/33/')" style="cursor:pointer;">
          <img src="https://i.imgur.com/ybrpp3L.jpeg" class="d-block w-100" alt="新闻2">
          <div class="carousel-caption d-none d-md-block">
            <h5>新闻动态2</h5>
            <p>更多新闻内容</p>
          </div>
        </div>
        <!-- 宣传语图片，仅展示 -->
        <div class="carousel-item">
          <img src="https://i.imgur.com/F4jcwtG.png" class="d-block w-100" alt="宣传语">
          <div class="carousel-caption d-none d-md-block">
            <h5>数字海洋文化馆欢迎您</h5>
            <p>探索海洋文化的奥秘</p>
          </div>
        </div>
      </div>
      <button class="carousel-control-prev" type="button" data-bs-target="#home-carousel" data-bs-slide="prev">
        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        <span class="visually-hidden">上一张</span>
      </button>
      <button class="carousel-control-next" type="button" data-bs-target="#home-carousel" data-bs-slide="next">
        <span class="carousel-control-next-icon" aria-hidden="true"></span>
        <span class="visually-hidden">下一张</span>
      </button>
    </div>

    <!-- 分割线 -->
    <div class="divider my-5"></div>

    <!-- 特色功能区入口 -->
    <div class="feature-area py-4">
      <div class="feature-title-bar">
        <span class="feature-title">特色功能</span>
        <span class="feature-title-underline"></span>
      </div>
      <div class="container">
        <div class="row g-4 justify-content-center">
          <div class="col-12 col-sm-6 col-lg-4" v-for="item in features" :key="item.title">
            <div class="card feature-card h-100 text-center" @click="goTo(item.path)" style="cursor:pointer;">
              <div class="card-body d-flex flex-column align-items-center justify-content-center">
                <span :class="item.icon + ' feature-icon mb-3'" :style="{color: item.color}"></span>
                <h5 class="card-title feature-card-title">{{ item.title }}</h5>
                <p class="card-text feature-card-desc">{{ item.desc }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 分割线 -->
    <div class="divider my-5"></div>

    <!-- 新闻动态预览 -->
    <div class="news-area py-4">
      <div class="feature-title-bar">
        <span class="feature-title">新闻动态</span>
        <span class="feature-title-underline"></span>
      </div>
      <div class="container">
        <div class="row">
          <div v-if="loading" class="col-12 text-center py-5">
            <div class="spinner-border text-primary" role="status">
              <span class="visually-hidden">加载中...</span>
            </div>
          </div>
          <div v-else-if="newsList.length === 0" class="col-12 text-center py-5">
            <p class="text-muted">暂无新闻</p>
          </div>
          <div v-else class="col-12" v-for="news in newsList" :key="news.id">
            <div class="news-card" @click="goTo(`/article/${news.id}/`)" style="cursor:pointer;">
              <div class="news-content">
                <h5 class="news-title">{{ news.title }}</h5>
                <div class="news-meta mb-2">
                  <div class="d-flex align-items-center gap-2">
                    <img :src="news.photo" class="rounded-circle" style="width: 24px; height: 24px; object-fit: cover;">
                    <span class="news-author">{{ news.username }}</span>
                  </div>
                  <span class="news-date">{{ formatDate(news.createtime) }}</span>
                </div>
                <p class="news-desc">{{ news.content ? news.content.substring(0, 100) : '' }}...</p>
                <div class="news-more">
                  阅读更多 <i class="bi bi-arrow-right"></i>
                </div>
              </div>
            </div>
          </div>
          <div class="text-end mt-4">
            <span class="view-more" @click="goTo('/news/')" style="cursor:pointer;">
              查看更多 <i class="bi bi-arrow-right"></i>
            </span>
          </div>
        </div>
      </div>
    </div>
  </ContentField>
</template>

<script>
import ContentField from "@/components/ContentField.vue"
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';
import axios from 'axios';

export default {
  components: {
    ContentField
  },
  setup() {
    const router = useRouter();
    const store = useStore();
    return {
      router,
      store
    };
  },
  data() {
    return {
      features: [
        {
          title: '全景参观',
          desc: '沉浸式体验海洋文化馆',
          path: '/panorama/',
          icon: 'bi bi-globe2',
          color: '#007bff',
        },
        {
          title: '馆藏展品',
          desc: '浏览丰富的海洋藏品',
          path: '/exhibit/',
          icon: 'bi bi-box-seam',
          color: '#20c997',
        },
        {
          title: '参观须知',
          desc: '参观前必读信息',
          path: '/guide/',
          icon: 'bi bi-journal-text',
          color: '#fd7e14',
        },
        {
          title: '意见反馈',
          desc: '欢迎提出宝贵意见',
          path: '/feedback/',
          icon: 'bi bi-chat-left-dots',
          color: '#6f42c1',
        },
        {
          title: '填写问卷',
          desc: '参与问卷调查',
          path: '/survey/',
          icon: 'bi bi-clipboard-check',
          color: '#17a2b8',
        },
        {
          title: '个人中心',
          desc: '管理个人信息与收藏',
          path: '/user/profile/',
          icon: 'bi bi-person-badge',
          color: '#e83e8c',
        },
      ],
      newsList: [],
      loading: false
    };
  },
  created() {
    this.fetchNews();
  },
  methods: {
    goTo(path) {
      this.router.push(path);
    },
    async fetchNews() {
      this.loading = true;
      try {
        const response = await axios.get('http://127.0.0.1:3000/article/get/news/', {
          params: {
            page: 1,
            size: 3
          },
          headers: {
            Authorization: "Bearer " + this.store.state.user.token,
          }
        });
        this.newsList = response.data.articles;
      } catch (error) {
        console.error('获取新闻列表失败:', error);
      } finally {
        this.loading = false;
      }
    },
    formatDate(date) {
      if (!date) return '';
      const d = new Date(date);
      d.setHours(d.getHours() + 8);
      return d.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
      });
    }
  }
}
</script>
 
<style scoped>
.carousel {
  max-width: 1100px;
  width: 100%;
  height: 420px;
  margin: 0 auto;
}
.carousel img {
  height: 420px;
  object-fit: cover;
}

.divider {
  border-bottom: 2px solid #e0e0e0;
  width: 100%;
  max-width: 1100px;
  margin: 32px auto 0 auto;
}

.feature-area {
  background: #f8fbff;
  border-radius: 24px;
  max-width: 1100px;
  margin: 0 auto 40px auto;
  box-shadow: 0 2px 16px rgba(0,0,0,0.04);
  padding-top: 0;
}
.feature-title-bar {
  display: flex;
  align-items: center;
  margin-left: 0px;
  margin-bottom: 18px;
  margin-top: 12px;
}
.feature-title {
  font-size: 1.15rem;
  font-weight: bold;
  color: #007bff;
  letter-spacing: 1px;
  text-align: left;
}
.feature-title-underline {
  display: inline-block;
  height: 2px;
  width: 48px;
  background: linear-gradient(90deg, #007bff 60%, #b3d8ff 100%);
  margin-left: 12px;
  border-radius: 2px;
}
.feature-card {
  border: none;
  border-radius: 20px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.07);
  transition: transform 0.22s, box-shadow 0.22s;
  min-height: 260px;
  background: #fff;
}
.feature-card:hover {
  transform: translateY(-10px) scale(1.04);
  box-shadow: 0 8px 32px rgba(0,123,255,0.13);
}
.feature-icon {
  font-size: 3.2rem;
  display: inline-block;
  margin-bottom: 10px;
}
.feature-card-title {
  font-weight: 600;
  font-size: 1.2rem;
  margin-bottom: 0.5rem;
}
.feature-card-desc {
  color: #888;
  font-size: 0.98rem;
  margin-bottom: 0;
}
@media (max-width: 991px) {
  .carousel {
    height: 260px;
  }
  .carousel img {
    height: 260px;
  }
  .feature-card {
    min-height: 220px;
  }
  .feature-title-bar {
    margin-left: 12px;
  }
}
@media (max-width: 575px) {
  .feature-area {
    border-radius: 0;
    box-shadow: none;
    padding: 0 0.5rem;
  }
  .carousel {
    height: 160px;
  }
  .carousel img {
    height: 160px;
  }
  .feature-card {
    min-height: 160px;
  }
  .feature-title-bar {
    margin-left: 6px;
    margin-bottom: 10px;
    margin-top: 8px;
  }
  .feature-title {
    font-size: 1rem;
  }
}

.news-area {
  background: #f8fbff;
  border-radius: 24px;
  max-width: 1100px;
  margin: 0 auto 40px auto;
  box-shadow: 0 2px 16px rgba(0,0,0,0.04);
  padding: 40px 20px;
}

.news-card {
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.07);
  transition: transform 0.22s, box-shadow 0.22s;
}

.news-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 32px rgba(0,123,255,0.13);
}

.news-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #333;
  margin-bottom: 12px;
}

.news-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #666;
  font-size: 0.9rem;
}

.news-author {
  color: #666;
}

.news-date {
  color: #888;
}

.news-desc {
  color: #666;
  font-size: 1rem;
  margin-bottom: 16px;
  line-height: 1.6;
}

.news-more {
  color: #007bff;
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 0.9rem;
}

.news-more i {
  transition: transform 0.2s;
}

.news-card:hover .news-more i {
  transform: translateX(4px);
}

.view-more {
  color: #666;
  font-size: 0.95rem;
  display: inline-flex;
  align-items: center;
  gap: 4px;
  transition: color 0.2s;
}

.view-more:hover {
  color: #007bff;
}

.view-more i {
  transition: transform 0.2s;
}

.view-more:hover i {
  transform: translateX(4px);
}

@media (max-width: 575px) {
  .news-area {
    border-radius: 0;
    box-shadow: none;
    padding: 20px 12px;
  }
  
  .news-card {
    padding: 16px;
  }
  
  .news-title {
    font-size: 1.1rem;
  }
  
  .news-desc {
    font-size: 0.95rem;
  }
}
</style>
