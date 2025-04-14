<template>
    <div class="panorama-containner d-flex justify-content-center align-items-center min-vh-100">
        <div id="panorama"></div>
    </div>
</template>

<script>
import { onMounted } from 'vue';

export default{
    setup(){
        const initPannellum = () => {
            window.pannellum.viewer('panorama', {
                type: 'equirectangular',
                panorama: require('@/assets/panorama/sphere.jpg')
            });
        }
        onMounted(() => {
            // 确保脚本加载完成后初始化
            if(typeof window.pannellum != 'undefined'){
                initPannellum();
            }else{
                const script = document.createElement('script')
                script.src = 'https://cdn.jsdelivr.net/npm/pannellum@2.5.6/build/pannellum.js'
                script.onload = () => this.initPannellum();
                document.head.appendChild(script);
            }
        })
    }
}
</script>

<style scoped>
#panorama {
    width: 1200px;
    height: 800px;
}
</style>