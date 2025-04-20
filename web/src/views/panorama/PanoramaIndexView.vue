<template>
    <ContentField>
        <div class="container mt-5">
          <h1 class="text-center mb-4 title-style">请选择展区开始游览</h1>
          <div class="row justify-content-center">
            <div class="col-md-4 mb-3" v-for="scene in scenes" :key="scene.id">
              <div class="card text-center">
                <img :src="scene.imageUrl" class="card-img-top exhibit-image" alt="展区图片">
                <div class="card-body">
                  <h5 class="card-title">{{ scene.name }}</h5>
                  <p class="card-text">{{ scene.description }}</p>
                  <router-link :to="{ name: 'PanoramaView', params: { sceneId: scene.id } }" class="btn btn-primary">
                    进入 {{ scene.name }}
                  </router-link>
                </div>
              </div>
            </div>
          </div>
        </div>
    </ContentField>
</template>

<script>
import { ref } from 'vue';
import ContentField from "@/components/ContentField.vue"
// 如果你的路由配置中 PanoramaView 的 name 不是 'PanoramaView'，请修改这里的导入和 router-link 中的 name

export default {
    components: {
        ContentField,
    },
    setup(){
        const scenes = ref([
            { id: 'scene1', name: '远古海洋', description: '探索三亿九千万年前的海洋生命。', imageUrl: 'https://pic.vjshi.com/2024-06-07/3e41ec28046d46179efa2f3a55e8d49e/online/main.jpg?x-oss-process=style/video_cover_20231101' },
            { id: 'scene2', name: '近代海洋', description: '了解近代海洋的变迁与生物。', imageUrl: 'https://pic.vjshi.com/2020-06-29/9ad6840759a1ff918f08b98d173e167b/online/main.jpg?x-oss-process=style/video_cover_20231101' },
            { id: 'scene3', name: '现代海洋', description: '观察当今海洋生态与面临的挑战。', imageUrl: require('@/assets/images/ocean.png') },
        ]);
        return {
            scenes,
        }
    }
}
</script>

<style scoped>
.card {
  cursor: default; /* 或者移除，因为按钮是可点击的 */
  transition: transform 0.2s ease-in-out;
}
.card:hover {
    transform: translateY(-5px);
    box-shadow: 0 4px 8px rgba(0,0,0,.12), 0 2px 4px rgba(0,0,0,.08);
}

.exhibit-image {
    width: 100%; /* 图片宽度占满卡片 */
    height: 300px; /* 固定图片高度，可以根据需要调整 */
    object-fit: cover; /* 保持图片比例，超出部分裁剪 */
}

.card-title {
  font-weight: bold;
  margin-top: 0.5rem; /* 图片和标题之间加点间距 */
  margin-bottom: 1rem;
}

.title-style {
    color: #0068ff;
    font-weight: bold;
}

.btn-primary {
    /* 可以根据你的主题调整颜色 */
    background-color: #007bff;
    border-color: #007bff;
}
</style>
