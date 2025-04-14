<template>
    <div class="d-flex justify-content-center align-items-center min-vh-100">
        <!-- 
            d-flex → 启用 Flexbox
            justify-content-center → 水平居中
            align-items-center → 垂直居中
            min-vh-100 → 占满整个视口高度
        -->
        <!-- the viewer container must have a defined size -->
        <div id="viewer" class="viewer" style="width: 80vw; height: 80vh;"></div>
    </div>
</template>

<script>
// import ContentField from "@/components/ContentField.vue"
import { Viewer } from '@photo-sphere-viewer/core'; 
import { onMounted } from "vue";
import '@photo-sphere-viewer/core/index.css'; // 不引入会导致图片自动无限放大

export default {
    components: {
        // ContentField,
    },
    setup(){
        onMounted(() => {
            const imgURL = require('@/assets/panorama/sphere.jpg');
            const viewer = new Viewer({
                container: document.querySelector('#viewer'),
                panorama: imgURL,
            });
            viewer
        });
        return{
        };
    }
}
</script>

<style scoped>
</style>
